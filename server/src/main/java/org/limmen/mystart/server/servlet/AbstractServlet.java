package org.limmen.mystart.server.servlet;

import java.io.IOException;
import java.nio.file.Path;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.Parser;
import org.limmen.mystart.User;
import org.limmen.mystart.UserStorage;

public class AbstractServlet extends HttpServlet {

  public static final String USER = "user";
  public static final String USER_ID = "userId";

  private static final long serialVersionUID = 1L;

  private final LinkStorage linkStorage;

  private final MultipartConfigElement multipartConfigElement;

  private final Parser parser;

  private final Path temporaryDirectory;

  private final UserStorage userStorage;

  public AbstractServlet(
      Parser parser,
      LinkStorage linkStorage,
      UserStorage userStorage,
      MultipartConfigElement multipartConfigElement,
      Path temporaryDirectory) {
    this.parser = parser;
    this.linkStorage = linkStorage;
    this.userStorage = userStorage;
    this.multipartConfigElement = multipartConfigElement;
    this.temporaryDirectory = temporaryDirectory;
  }

  public LinkStorage getLinkStorage() {
    return linkStorage;
  }

  public Parser getParser() {
    return parser;
  }

  public Path getTemporaryDirectory() {
    return temporaryDirectory;
  }

  public UserStorage getUserStorage() {
    return userStorage;
  }

  protected void clearCookies(HttpServletRequest req, HttpServletResponse res) {
    Cookie[] cookies = req.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().startsWith("mystart")) {
          cookie.setValue("");
          cookie.setPath("/");
          cookie.setMaxAge(0);
          res.addCookie(cookie);
        }
      }
    }
    req.getSession().invalidate();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    if (req.getSession().getAttribute(USER_ID) == null) {
      // check cookies
      Cookie[] cookies = req.getCookies();
      if (cookies != null) {
        String name = null;
        String password = null;
        for (Cookie cookie : cookies) {
          switch (cookie.getName()) {
            case "mystartUser":
              name = cookie.getValue();
              break;
            case "mystartUserPassword":
              password = cookie.getValue();
              break;
            default:
              break;
          }
        }
        // check cookie data
        if (password != null & name != null) {
          User user = getUserStorage().getByNameOrEmail(name);
          if (user != null && user.getPassword().equals(password)) {
            req.getSession().setAttribute(USER_ID, user.getId());
          } else {
            clearCookies(req, res);
          }
        }
      }

      if (req.getSession().getAttribute(USER_ID) == null) {
        req.getRequestDispatcher("/login.jsp").include(req, res);
      }
    }

    if (req.getSession().getAttribute(USER_ID) != null) {
      Long userId = (Long) req.getSession().getAttribute(USER_ID);
      User user = getUserStorage().get(userId);
      req.setAttribute(USER_ID, userId);
      req.setAttribute(USER, user);
      req.setAttribute("labels", getLinkStorage().getAllLabels(userId));
      req.setAttribute("links", getLinkStorage().getAllByLabel(userId, "MyStart"));
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    if (req.getSession().getAttribute(USER_ID) == null) {
      res.sendRedirect("/home");
      return;
    }
    if (req.getContentType() != null && req.getContentType().startsWith("multipart/form-data")) {
      req.setAttribute(Request.__MULTIPART_CONFIG_ELEMENT, multipartConfigElement);
    }
  }

  protected boolean exists(HttpServletRequest req, String parameter) {
    return req.getParameter(parameter) != null;
  }

  protected boolean getBool(HttpServletRequest req, String parameter) {
    return req.getParameter(parameter) != null;
  }

  protected int getInt(HttpServletRequest req, String parameter) {
    return Integer.parseInt(req.getParameter(parameter));
  }

  protected long getLong(HttpServletRequest req, String parameter) {
    return Long.parseLong(req.getParameter(parameter));
  }

  protected String getOrignalParameters(HttpServletRequest req) {
    String referer = req.getHeader("Referer");

    if (referer.contains("?")) {
      int index = referer.indexOf("?");
      return referer.substring(1 + index);
    }

    return referer;
  }

  protected boolean hasValue(HttpServletRequest req, String parameter) {
    return req.getParameter(parameter) != null && req.getParameter(parameter).length() > 0;
  }
}
