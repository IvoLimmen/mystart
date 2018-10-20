package org.limmen.mystart.server.servlet;

import java.io.IOException;
import static java.lang.String.CASE_INSENSITIVE_ORDER;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.limmen.mystart.DomainUtil;
import org.limmen.mystart.Link;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.Parser;
import org.limmen.mystart.UserStorage;
import org.limmen.mystart.cleanup.CleanupContext;
import org.limmen.mystart.cleanup.CleanupTaskManager;

@Slf4j
public class LinkServlet extends AbstractServlet {

  private static final long serialVersionUID = 1L;

  public LinkServlet(
      Parser parser,
      LinkStorage linkStorage,
      UserStorage userStorage,
      MultipartConfigElement multipartConfigElement,
      Path temporaryDirectory) {
    super(parser, linkStorage, userStorage, multipartConfigElement, temporaryDirectory);
  }

  private void scheduleCleanup(HttpServletRequest req, Long userId) {

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

      req.setAttribute("referer", getOrignalParameters(req));
      req.setAttribute("link", link);
      req.setAttribute("labels", DomainUtil.formatLabels(link));
      req.setAttribute("type", type);
      req.getRequestDispatcher("/edit.jsp").include(req, res);

    } else if (exists(req, "delete")) {

      Long id = Long.parseLong(req.getParameter("delete"));
      getLinkStorage().remove(userId, id);
      res.sendRedirect("/home?" + getOrignalParameters(req));

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

    } else if (exists(req, "stats")) {

      String key = req.getParameter("stats");
      Collection<Link> links = getLinkStorage().getAll(userId);
      Collection<String> labels = getLinkStorage().getAllLabels(userId);

      Map<String, Long> stats = new TreeMap<>(CASE_INSENSITIVE_ORDER);

      switch (key) {
        case "source":
          stats.putAll(
              links.stream()
                  .collect(groupingBy(g -> g.getSource(), counting())));
          break;
        case "create_year":
          stats.putAll(
              links.stream()
                  .collect(groupingBy(g -> g.getCreationDate().getYear() + "", counting())));
          break;
        case "visit_year":
          stats.putAll(
              links.stream()
                  .collect(groupingBy(g -> g.getLastVisit() == null ? "Never" : g.getLastVisit().getYear() + "", counting())));
          break;
        case "labels":
          labels.forEach(l -> {
            stats.put(l, links.stream().filter(f -> f.getLabels().contains(l)).count());
          });
          break;
      }

      req.setAttribute("map", stats);
      req.getRequestDispatcher("/stats.jsp").include(req, res);
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

      if (hasValue(req, "type") && req.getParameter("type").equals("popup")) {
        req.getRequestDispatcher("/close.jsp").include(req, res);
      } else {
        res.sendRedirect("/home?" + req.getParameter("referer"));
      }

    } else if (exists(req, "cancelButton")) {

      if (hasValue(req, "type") && req.getParameter("type").equals("popup")) {
        req.getRequestDispatcher("/close.jsp").include(req, res);
      } else {
        res.sendRedirect("/home?" + req.getParameter("referer"));
      }
    }
  }
}
