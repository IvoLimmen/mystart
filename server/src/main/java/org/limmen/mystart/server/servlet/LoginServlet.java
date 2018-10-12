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

public class LoginServlet extends AbstractServlet {

  private static final long serialVersionUID = 1L;

  public LoginServlet(
      Parser parser,
      LinkStorage linkStorage,
      UserStorage userStorage,
      MultipartConfigElement multipartConfigElement,
      Path temporaryDirectory) {
    super(parser, linkStorage, userStorage, multipartConfigElement, temporaryDirectory);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    String email = req.getParameter("email");
    String name = req.getParameter("name");
    String password = req.getParameter("password");

    if (exists(req, "registerButton")) {
      User user = new User(name, email, password);
      getUserStorage().store(user);

      user = getUserStorage().getByNameOrEmail(email);
      req.getSession().setAttribute(USER_ID, user.getId());

      res.sendRedirect("/home");

    } else if (exists(req, "loginButton")) {

      User user = getUserStorage().getByNameOrEmail(name);

      if (user == null || !user.check(password)) {
        res.sendRedirect("/login.jsp?error=1");
      } else {
        req.getSession().setAttribute(USER_ID, user.getId());
        res.sendRedirect("/home");
      }

    } else if (exists(req, "cancelButton")) {

      res.sendRedirect("/home");
    }
  }
}
