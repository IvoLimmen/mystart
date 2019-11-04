package org.limmen.mystart.server.servlet;

import java.io.IOException;
import java.nio.file.Path;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.limmen.mystart.Storage;
import org.limmen.mystart.User;

public class NavServlet extends AbstractServlet {

  private static final long serialVersionUID = 1L;

  public NavServlet(Storage storage,
                    MultipartConfigElement multipartConfigElement,
                    Path temporaryDirectory) {
    super(storage,
          multipartConfigElement,
          temporaryDirectory);
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

    if (exists(req, "page")) {

      String page = req.getParameter("page");

      if ("search".equals(page)) {
        req.setAttribute("labels", getLinkStorage().getAllLabels(userId));
      }

      req.getRequestDispatcher("/" + page + ".jsp").include(req, res);
    }
  }

}
