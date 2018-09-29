package org.limmen.mystart.server.servlet;

import java.io.IOException;
import java.nio.file.Path;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
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

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    if (req.getSession().getAttribute(USER) == null) {
      req.getRequestDispatcher("/signup.jsp").include(req, res);
    } else {
      Long userId = (Long) req.getSession().getAttribute(USER);
      User user = getUserStorage().get(userId);
      req.setAttribute("userId", userId);
      req.setAttribute("email", user.getEmail());
      req.setAttribute("labels", getLinkStorage().getAllLabels(userId));
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    if (req.getContentType() != null && req.getContentType().startsWith("multipart/form-data")) {
      req.setAttribute(Request.__MULTIPART_CONFIG_ELEMENT, multipartConfigElement);
    }
  }

  protected boolean exists(HttpServletRequest req, String parameter) {
    return req.getParameter(parameter) != null;
  }

  protected boolean hasValue(HttpServletRequest req, String parameter) {
    return req.getParameter(parameter) != null && req.getParameter(parameter).length() > 0;
  }
}
