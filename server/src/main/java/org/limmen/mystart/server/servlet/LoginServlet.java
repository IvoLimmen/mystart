package org.limmen.mystart.server.servlet;

import java.io.IOException;
import java.nio.file.Path;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.limmen.mystart.Storage;
import org.limmen.mystart.User;

public class LoginServlet extends AbstractServlet {

  private static final long serialVersionUID = 1L;

  public LoginServlet(Storage storage,
                      MultipartConfigElement multipartConfigElement,
                      Path temporaryDirectory) {
    super(storage,
          multipartConfigElement,
          temporaryDirectory);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    super.doGet(req, res);

    if (exists(req, "logout")) {
      clearCookies(req, res);

      res.sendRedirect("/home");
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    String email = req.getParameter("email");
    String password = req.getParameter("password");

    if (exists(req, "registerButton")) {
      User user = new User();
      user.setEmail(email);
      user.updatePassword(password);
      getUserStorage().store(user);

      user = getUserStorage().getByEmail(email);

      res.sendRedirect("/home");

    } else if (exists(req, "loginButton")) {

      User user = getUserStorage().getByEmail(email);

      if (user == null || !user.check(password)) {
        res.sendRedirect("/login.jsp?error=1");
      } else {
        req.getSession().setAttribute(USER_ID, user.getId());
        addCookie(res, "mystart", email + "|" + user.getPassword());
        res.sendRedirect("/home");
      }

    } else if (exists(req, "cancelButton")) {

      res.sendRedirect("/home");
    }
  }
}
