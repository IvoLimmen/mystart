package org.limmen.mystart.server.servlet;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.UUID;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.limmen.mystart.Storage;
import org.limmen.mystart.User;
import org.limmen.mystart.server.support.MailService;
import org.limmen.mystart.server.support.PropertyHelper;

public class LoginServlet extends AbstractServlet {

  private static final long serialVersionUID = 1L;

  private final String localUrl;

  private final String salt; 
  
  private final MailService mailService;

  public LoginServlet(Storage storage,
                      MultipartConfigElement multipartConfigElement,
                      Path temporaryDirectory,
                      MailService mailService,
                      String localUrl,
                      Properties properties) {
    super(storage,
          multipartConfigElement,
          temporaryDirectory, 
          properties);
    this.mailService = mailService;
    this.localUrl = localUrl;
    this.salt = PropertyHelper.getSalt(properties);
  }

  private String createUrl(String localUrl, User user) {
    String url = localUrl;
    if (!url.endsWith("/")) {
      url = url + "/";
    }
    return url + "login?resetcode=" + user.getResetCode();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    super.doGet(req, res);

    if (exists(req, "logout")) {
      clearCookies(req, res);

      res.sendRedirect("/home");
    } else if (exists(req, "resetcode")) {
      
      User user = getUserStorage().getByResetCode(req.getParameter("resetcode"));
      
      if (user != null && user.getResetCodeValid().isAfter(LocalDateTime.now())) {
        req.setAttribute("resetcode", req.getParameter("resetcode"));
        req.getRequestDispatcher("/reset.jsp").include(req, res);
      }
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    String email = req.getParameter("email");
    String password = req.getParameter("password");

    if (exists(req, "registerButton")) {
      User user = new User();
      user.setEmail(email);
      user.updatePassword(salt, password);
      getUserStorage().store(user);

      res.sendRedirect("/home");

    } else if (exists(req, "resetButton")) {
      User user = getUserStorage().getByEmail(email);
      if (user != null) {
        user.setResetCode(UUID.randomUUID().toString());
        user.setResetCodeValid(LocalDateTime.now().plusDays(2));
        user.setPassword(null);
        getUserStorage().store(user);

        mailService.sendPasswordReset(user.getEmail(), user.getFullName(), createUrl(localUrl, user));
      }

      res.sendRedirect("/home");

    } else if (exists(req, "resetConfirmButton")) {
      String resetcode = req.getParameter("resetcode");
      User user = getUserStorage().getByResetCode(resetcode);
      if (user != null) {
        user.setResetCode(null);
        user.setResetCodeValid(null);
        user.updatePassword(salt, password);
        getUserStorage().store(user);
      }

      res.sendRedirect("/home");

    } else if (exists(req, "loginButton")) {

      User user = getUserStorage().getByEmail(email);

      if (user == null || !user.check(salt, password)) {
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
