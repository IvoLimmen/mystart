package org.limmen.mystart.server.servlet;

import java.io.IOException;
import java.nio.file.Path;
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

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    super.doPost(req, res);
    Long userId = (Long) req.getSession().getAttribute(USER);

    if (userId == null) {
      return;
    }

    if (req.getParameter("save") != null) {

    } else if (req.getParameter("cancel") != null) {
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

    if (req.getParameter("edit") != null) {
      Long id = Long.parseLong(req.getParameter("edit"));
      Link link = getLinkStorage().get(userId, id);

      req.setAttribute("link", link);
      req.setAttribute("labels", link.getLabels().stream().collect(Collectors.joining(", ")));
      req.getRequestDispatcher("/edit.jsp").include(req, res);
    } else if (req.getParameter("delete") != null) {
      Long id = Long.parseLong(req.getParameter("delete"));
      getLinkStorage().remove(userId, id);
      res.sendRedirect("/home");
    }
  }
}
