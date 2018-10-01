package org.limmen.mystart.server.servlet;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.limmen.mystart.Link;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.Parser;
import org.limmen.mystart.UserStorage;

@Slf4j
public class HomeServlet extends AbstractServlet {

  private static final long serialVersionUID = 1L;

  public HomeServlet(
      Parser parser,
      LinkStorage linkStorage,
      UserStorage userStorage,
      MultipartConfigElement multipartConfigElement,
      Path temporaryDirectory) {
    super(parser, linkStorage, userStorage, multipartConfigElement, temporaryDirectory);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    super.doGet(req, res);
    Long userId = (Long) req.getSession().getAttribute(USER);

    if (userId == null) {
      return;
    }

    if (req.getParameter("reg") != null) {
      Long id = Long.parseLong(req.getParameter("reg"));
      Link link = getLinkStorage().get(userId, id);
      link.visited();
      getLinkStorage().create(userId, link);
      res.sendRedirect(link.getUrl());
    } else {
      if (req.getParameter("search") != null) {
        String search = req.getParameter("search");
        Collection<Link> links = getLinkStorage().getAll(userId).stream()
            .filter(link -> link.hasKeyword(search))
            .collect(Collectors.toList());
        req.setAttribute("links", links);
      } else if (req.getParameter("label") != null) {
        Collection<Link> links = getLinkStorage().getAllByLabel(userId, req.getParameter("label"));
        req.setAttribute("links", links);
      }

      req.getRequestDispatcher("/home.jsp").include(req, res);
    }
  }
}
