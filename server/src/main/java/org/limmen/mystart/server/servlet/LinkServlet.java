package org.limmen.mystart.server.servlet;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.limmen.mystart.DomainUtil;
import org.limmen.mystart.Link;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.Parser;
import org.limmen.mystart.UserStorage;
import org.limmen.mystart.server.cleanup.CleanupContext;
import org.limmen.mystart.server.cleanup.CleanupTask;

@Slf4j
public class LinkServlet extends AbstractServlet {

  private static final long serialVersionUID = 1L;

  private final ExecutorService executorService = Executors.newFixedThreadPool(5);

  public LinkServlet(
      Parser parser,
      LinkStorage linkStorage,
      UserStorage userStorage,
      MultipartConfigElement multipartConfigElement,
      Path temporaryDirectory) {
    super(parser, linkStorage, userStorage, multipartConfigElement, temporaryDirectory);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    super.doPost(req, res);
    Long userId = (Long) req.getSession().getAttribute(USER);

    if (userId == null) {
      return;
    }

    if (exists(req, "check")) {

      executorService.submit(
          new CleanupTask(getLinkStorage(), CleanupContext.builder()
              .assumeHttps(getBool(req, "assumeHttps"))
              .markAsPrivateNetworkOnDomainError(getBool(req, "markAsPrivateNetworkOnDomainError"))
              .userId(userId)
              .build()));

      res.sendRedirect("/home");

    } else if (exists(req, "save")) {

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
      res.sendRedirect("/home");

    } else if (exists(req, "cancel")) {

      res.sendRedirect("/home");
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    super.doGet(req, res);
    Long userId = (Long) req.getSession().getAttribute(USER);

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

      Link link = null;
      if (req.getParameter("edit") != null && req.getParameter("edit").length() > 0) {

        Long id = Long.parseLong(req.getParameter("edit"));
        link = getLinkStorage().get(userId, id);
      } else {
        link = new Link();
      }

      req.setAttribute("link", link);
      req.setAttribute("labels", DomainUtil.formatLabels(link));
      req.getRequestDispatcher("/edit.jsp").include(req, res);

    } else if (exists(req, "delete")) {

      Long id = Long.parseLong(req.getParameter("delete"));
      getLinkStorage().remove(userId, id);
      res.sendRedirect("/home?" + getOrignalParameters(req));
    }
  }
}
