package org.limmen.mystart.server.servlet;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

import org.limmen.mystart.Storage;
import org.limmen.mystart.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class NavServlet extends AbstractServlet {

  private static final long serialVersionUID = 1L;

  public NavServlet(Storage storage,
                    Path temporaryDirectory, 
                    Properties properties) {
    super(storage,
          temporaryDirectory, 
          properties);
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
