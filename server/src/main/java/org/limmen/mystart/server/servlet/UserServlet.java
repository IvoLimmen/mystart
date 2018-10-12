package org.limmen.mystart.server.servlet;

import java.io.IOException;
import java.nio.file.Path;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.Parser;
import org.limmen.mystart.User;
import org.limmen.mystart.UserStorage;

public class UserServlet extends AbstractServlet {

  private static final long serialVersionUID = 1L;

  public UserServlet(
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

    Long userId = (Long) req.getSession().getAttribute(USER_ID);

    if (userId == null) {
      return;
    }

    User user = getUserStorage().get(userId);
    req.setAttribute("user", user);
    req.getRequestDispatcher("/account.jsp").include(req, res);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    super.doPost(req, res);

    String email = req.getParameter("email");
    String name = req.getParameter("name");
    String password = req.getParameter("password");

    if (exists(req, "saveButton")) {

      Long id = getLong(req, "id");
      User user = getUserStorage().get(id);
      String password2 = req.getParameter("password2");

      user.setOpenInNewTab(exists(req, "openinnewtab"));
      user.setEmail(email);
      user.setName(name);
      if (password != null && password2 != null && password.equals(password2)) {
        user.updatePassword(password);
      }
      getUserStorage().store(user);

      res.sendRedirect("/home");

    } else if (exists(req, "cancelButton")) {

      res.sendRedirect("/home");
    }
  }
}
