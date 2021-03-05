package org.limmen.mystart.server.servlet;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.limmen.mystart.Link;
import org.limmen.mystart.Storage;
import org.limmen.mystart.criteria.And;
import org.limmen.mystart.criteria.Criteria;
import org.limmen.mystart.criteria.Like;

public class HomeServlet extends AbstractServlet {

  private static final long serialVersionUID = 1L;

  public HomeServlet(Storage storage,
                     MultipartConfigElement multipartConfigElement,
                     Path temporaryDirectory, 
                     Properties properties) {
    super(storage,
          multipartConfigElement,
          temporaryDirectory,
          properties
    );
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    super.doGet(req, res);
    Long userId = (Long) req.getSession().getAttribute(USER_ID);

    if (userId == null) {
      return;
    }

    if (exists(req, "searchButton")) {
      String search = req.getParameter("search");
      Collection<Link> links = getLinkStorage().getAll(userId).stream()
          .filter(link -> link.hasKeyword(search))
          .sorted((link1, link2) -> {
            if (link1.getTitle() != null && link2.getTitle() != null) {
              return link1.getTitle().compareTo(link2.getTitle());
            }
            return 0;
          })
          .collect(Collectors.toList());

      req.setAttribute("links", links);
      req.getRequestDispatcher("/home.jsp").include(req, res);

    } else if (exists(req, "label")) {

      Collection<Link> links = getLinkStorage().getAllByLabel(userId, req.getParameter("label"));
      req.setAttribute("links", links);
      req.getRequestDispatcher("/home.jsp").include(req, res);

    } else if (exists(req, "show")) {

      if ("labels".equals(req.getParameter("show"))) {

        Map<String, Long> stats = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        Collection<String> labels = getLinkStorage().getAllLabels(userId);
        Collection<Link> links = getLinkStorage().getAll(userId);
        labels.forEach(l -> {
          stats.put(l, links.stream().filter(f -> f.getLabels().contains(l)).count());
        });

        req.setAttribute("labels", labels);
        req.setAttribute("stats", stats);
        req.getRequestDispatcher("/labels.jsp").include(req, res);

      } else if ("lastvisit".equals(req.getParameter("show"))) {

        Collection<Link> links = getLinkStorage().getLastVisited(userId, 20);
        req.setAttribute("links", links);
        req.getRequestDispatcher("/home.jsp").include(req, res);

      } else if ("lastcreated".equals(req.getParameter("show"))) {

        Collection<Link> links = getLinkStorage().getLastCreated(userId, 20);
        req.setAttribute("links", links);
        req.getRequestDispatcher("/home.jsp").include(req, res);
      }
    } else {
      req.getRequestDispatcher("/home.jsp").include(req, res);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    super.doPost(req, res);

    Long userId = (Long) req.getSession().getAttribute(USER_ID);

    if (userId == null) {
      return;
    }

    if (exists(req, "advSearchButton")) {

      Criteria criteria = null;

      if (hasValue(req, "title")) {
        criteria = new Like("title", req.getParameter("title"), String.class);
      }
      if (hasValue(req, "url")) {
        Criteria newCriteria = new Like("url", req.getParameter("url"), String.class);
        if (criteria == null) {
          criteria = newCriteria;
        } else {
          criteria = new And(criteria, newCriteria);
        }        
      }
      if (hasValue(req, "description")) {
        Criteria newCriteria = new Like("description", req.getParameter("description"), String.class);
        if (criteria == null) {
          criteria = newCriteria;
        } else {
          criteria = new And(criteria, newCriteria);
        }        
      }
      if (hasValue(req, "label")) {
        Criteria newCriteria = new Like("labels", req.getParameter("label"), String[].class);
        if (criteria == null) {
          criteria = newCriteria;
        } else {
          criteria = new And(criteria, newCriteria);
        }        
      }

      req.setAttribute("links", getLinkStorage().search(userId, criteria));
      req.getRequestDispatcher("/home.jsp").include(req, res);
    } else if (exists(req, "cancelButton")) {

      res.sendRedirect("/home");
    }
  }
}
