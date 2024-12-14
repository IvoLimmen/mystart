package org.limmen.mystart.server.servlet;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

import org.limmen.mystart.Category;
import org.limmen.mystart.Storage;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CategoryServlet extends AbstractServlet {

  private static final long serialVersionUID = 1L;

  public CategoryServlet(
      Storage storage,
      Path temporaryDirectory,
      Properties properties) {
    super(storage, temporaryDirectory, properties);
  }

    @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    super.doGet(req, res);
    Long userId = (Long) req.getSession().getAttribute(USER_ID);

    req.setAttribute("categories", getCategoryStorage().getAll(userId));
    req.getRequestDispatcher("/categories.jsp").include(req, res);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    super.doPost(req, res);
    Long userId = (Long) req.getSession().getAttribute(USER_ID);

    if (userId == null) {
      return;
    }  
  
    Long id = null;
    if (req.getParameter("id") != null) {
      id = Long.parseLong(req.getParameter("id"));
    }    
    String name = req.getParameter("name");
    String color = req.getParameter("color");
    
    if (exists(req, "delete")) {      
      getCategoryStorage().delete(userId, id);
    } else if (exists(req, "update")) {
      Category category = getCategoryStorage().get(userId, id);
      category.setColor(color);
      category.setName(name);
      getCategoryStorage().update(userId, category);
    } else if (exists(req, "new")) {
      Category category = new Category();
      category.setColor(color);
      category.setName(name);
      getCategoryStorage().create(userId, category);
    }

    req.setAttribute("categories", getCategoryStorage().getAll(userId));
    req.getRequestDispatcher("/categories.jsp").include(req, res);
  }
}
