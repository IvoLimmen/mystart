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
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    super.doPost(req, res);

    String email = req.getParameter("email");
    String password = req.getParameter("password");

    if (req.getParameter("register") != null) {
      User user = new User(email, password);
      getUserStorage().store(user);

      user = getUserStorage().getByEmail(email);
      req.getSession().setAttribute(USER, user.getId());
    }

    if (req.getParameter("login") != null) {
      User user = getUserStorage().getByEmail(email);
      if (!user.check(password)) {
        res.sendRedirect("/signup.jsp?error=1");
      } else {
        req.getSession().setAttribute(USER, user.getId());
      }
    }

    res.sendRedirect("/home");
  }
}
