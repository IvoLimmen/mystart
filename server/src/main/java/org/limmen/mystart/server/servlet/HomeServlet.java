package org.limmen.mystart.server.servlet;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.limmen.mystart.Link;
import org.limmen.mystart.Storage;

@Slf4j
public class HomeServlet extends AbstractServlet {

  private static final long serialVersionUID = 1L;

  public HomeServlet(Storage storage,
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
      }
    } else {
      req.getRequestDispatcher("/home.jsp").include(req, res);
    }
  }
}
