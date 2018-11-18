package org.limmen.mystart.server.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.util.Collection;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.limmen.mystart.DomainUtil;
import org.limmen.mystart.Link;
import org.limmen.mystart.Storage;
import org.limmen.mystart.cleanup.CleanupContext;
import org.limmen.mystart.cleanup.CleanupTaskManager;

@Slf4j
public class LinkServlet extends AbstractServlet {

  private static final long serialVersionUID = 1L;

  public LinkServlet(Storage storage,
                     MultipartConfigElement multipartConfigElement,
                     Path temporaryDirectory) {
    super(storage, multipartConfigElement, temporaryDirectory);
  }

  private void scheduleCleanup(HttpServletRequest req, Long userId) {

    log.info("Starting a new thread for checking...");

    new Thread(new CleanupTaskManager(
        getLinkStorage(),
        userId,
        CleanupContext.builder()
            .assumeHttps(getBool(req, "assumeHttps"))
            .markAsPrivateNetworkOnDomainError(getBool(req, "markAsPrivateNetworkOnDomainError"))
            .maximumTimeoutInSeconds(getInt(req, "maximumTimeoutInSeconds"))
            .userId(userId)
            .build())
    ).start();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    super.doGet(req, res);
    Long userId = (Long) req.getSession().getAttribute(USER_ID);

    if (userId == null) {
      return;
    }

    if (exists(req, "reg")) {

      Long id = Long.parseLong(req.getParameter("reg"));
      Link link = getLinkStorage().get(userId, id);
      link.visited();
      getLinkStorage().update(userId, link);
      getVisitStorage().visit(link.getId());
      res.sendRedirect(link.getRedirectUrl());

    } else if (exists(req, "edit")) {

      String type = "normal";
      Link link = null;
      if (req.getParameter("id") != null && req.getParameter("id").length() > 0) {
        // edit by id
        Long id = Long.parseLong(req.getParameter("id"));
        link = getLinkStorage().get(userId, id);
      } else if (req.getParameter("url") != null && req.getParameter("url").length() > 0) {
        // new (or edit) by url        
        type = "popup";
        String url = req.getParameter("url");
        link = getLinkStorage().getByUrl(userId, url);
      }

      if (link == null) {
        link = new Link();
      }

      if (exists(req, "url")) {
        if (StringUtils.isBlank(link.getUrl())) {
          link.setUrl(req.getParameter("url"));
        }
      }
      if (exists(req, "title")) {
        if (StringUtils.isBlank(link.getTitle())) {
          link.setTitle(req.getParameter("title"));
        }
      }

      req.setAttribute("labels", getLinkStorage().getAllLabels(userId));
      req.setAttribute("referer", getOrignalParameters(req));
      req.setAttribute("link", link);
      req.setAttribute("editlabels", DomainUtil.formatLabels(link));
      req.setAttribute("type", type);
      if (type.equals("normal")) {
        req.getRequestDispatcher("/edit.jsp").include(req, res);
      } else {
        req.getRequestDispatcher("/popup.jsp").include(req, res);
      }
    } else if (exists(req, "details")) {

      Long id = Long.parseLong(req.getParameter("id"));
      Link link = getLinkStorage().get(userId, id);
      req.setAttribute("link", link);
      req.setAttribute("visits", getVisitStorage().getLast20Visists(id));
      req.getRequestDispatcher("/details.jsp").include(req, res);

    } else if (exists(req, "delete")) {

      if (exists(req, "id")) {

        Long id = Long.parseLong(req.getParameter("id"));
        getLinkStorage().remove(userId, id);
        res.sendRedirect("/home?" + getOrignalParameters(req));

      } else if (exists(req, "lbl")) {

        String label = req.getParameter("lbl");
        Collection<Link> links = getLinkStorage().getAllByLabel(userId, label);
        links.forEach(link -> {
          link.removeLabel(label);
          getLinkStorage().update(userId, link);
        });

        res.sendRedirect("/home?show=labels");
      }
    } else if (exists(req, "move")) {

      req.setAttribute("label", req.getParameter("lbl"));
      req.setAttribute("labels", getLinkStorage().getAllLabels(userId));
      req.getRequestDispatcher("/move.jsp").include(req, res);

    } else if (exists(req, "delall")) {

      String selection = getOrignalParameters(req);
      if (selection.contains("searchButton=")) {
        selection = selection.substring(0, selection.indexOf("searchButton=") - 1);
      }
      String key = selection.split("=")[0];
      String value = URLDecoder.decode(selection.split("=")[1], "UTF-8");

      if (key.equals("search")) {
        getLinkStorage().getAll(userId).stream()
            .filter(link -> link.hasKeyword(value))
            .forEach(link -> {
              getLinkStorage().remove(userId, link.getId());
            });
      } else if (key.equals("label")) {
        getLinkStorage().getAllByLabel(userId, value)
            .forEach(link -> {
              getLinkStorage().remove(userId, link.getId());
            });
      }

      res.sendRedirect("/home");
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    super.doPost(req, res);
    Long userId = (Long) req.getSession().getAttribute(USER_ID);

    if (userId == null) {
      return;
    }

    if (exists(req, "checkButton")) {

      scheduleCleanup(req, userId);

      res.sendRedirect("/home");

    } else if (exists(req, "moveButton")) {

      String oldLabel = req.getParameter("old-label");
      String labels = req.getParameter("labels");

      Collection<Link> links = getLinkStorage().getAllByLabel(userId, oldLabel);

      links.forEach(link -> {
        link.moveLabel(oldLabel, labels);

        getLinkStorage().update(userId, link);
      });

      res.sendRedirect("/home?show=labels");

    } else if (exists(req, "saveButton")) {

      Link link = new Link();
      if (hasValue(req, "id")) {
        Long id = Long.parseLong(req.getParameter("id"));
        link = getLinkStorage().get(userId, id);
      }

      link.setDescription(req.getParameter("description"));
      link.setTitle(req.getParameter("title"));
      link.setUrl(req.getParameter("url"));

      if (hasValue(req, "labels")) {
        String label = req.getParameter("labels");
        link.setLabels(DomainUtil.parseLabels(label));
      }

      if (link.getId() == null) {
        link.setSource("MyStart");
        getLinkStorage().create(userId, link);
      } else {
        getLinkStorage().update(userId, link);
      }

      if (hasValue(req, "type", "popup")) {
        req.getRequestDispatcher("/close.jsp").include(req, res);
      } else {
        res.sendRedirect("/home?" + req.getParameter("referer"));
      }

    } else if (exists(req, "cancelMoveButton")) {

      res.sendRedirect("/home?show=labels");

    } else if (exists(req, "cancelButton")) {

      if (hasValue(req, "type", "popup")) {
        req.getRequestDispatcher("/close.jsp").include(req, res);
      } else {
        res.sendRedirect("/home?" + req.getParameter("referer"));
      }
    }
  }
}
