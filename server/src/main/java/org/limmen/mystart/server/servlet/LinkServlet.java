package org.limmen.mystart.server.servlet;

import java.io.IOException;
import static java.lang.String.CASE_INSENSITIVE_ORDER;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.limmen.mystart.DomainUtil;
import org.limmen.mystart.Link;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.Parser;
import org.limmen.mystart.UserStorage;
import org.limmen.mystart.server.cleanup.CleanupContext;
import org.limmen.mystart.server.cleanup.CleanupTaskManager;

@Slf4j
public class LinkServlet extends AbstractServlet {

  private static final long serialVersionUID = 1L;

  public LinkServlet(
      Parser parser,
      LinkStorage linkStorage,
      UserStorage userStorage,
      MultipartConfigElement multipartConfigElement,
      Path temporaryDirectory) {
    super(parser, linkStorage, userStorage, multipartConfigElement, temporaryDirectory);
  }

  private void scheduleCleanup(HttpServletRequest req, Long userId) {

    new Thread(new CleanupTaskManager(
        getLinkStorage(),
        userId,
        CleanupContext.builder()
            .assumeHttps(getBool(req, "assumeHttps"))
            .markAsPrivateNetworkOnDomainError(getBool(req, "markAsPrivateNetworkOnDomainError"))
            .maximumTimeoutInSeconds(getInt(req, "maximumTimeoutInSeconds"))
            .userId(userId)
            .build())
    ).start();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    super.doGet(req, res);
    Long userId = (Long) req.getSession().getAttribute(USER);

    if (userId == null) {
      return;
    }

    if (exists(req, "reg")) {

      Long id = Long.parseLong(req.getParameter("reg"));
      Link link = getLinkStorage().get(userId, id);
      link.visited();
      getLinkStorage().update(userId, link);
      res.sendRedirect(link.getRedirectUrl());

    } else if (exists(req, "edit")) {

      Link link = null;
      if (req.getParameter("edit") != null && req.getParameter("edit").length() > 0) {

        Long id = Long.parseLong(req.getParameter("edit"));
        link = getLinkStorage().get(userId, id);
      } else {
        link = new Link();
      }

      req.setAttribute("link", link);
      req.setAttribute("labels", DomainUtil.formatLabels(link));
      req.getRequestDispatcher("/edit.jsp").include(req, res);

    } else if (exists(req, "delete")) {

      Long id = Long.parseLong(req.getParameter("delete"));
      getLinkStorage().remove(userId, id);
      res.sendRedirect("/home?" + getOrignalParameters(req));

    } else if (exists(req, "stats")) {

      String key = req.getParameter("stats");
      Collection<Link> links = getLinkStorage().getAll(userId);
      Collection<String> labels = getLinkStorage().getAllLabels(userId);

      switch (key) {
        case "source":
          req.setAttribute("map",
              links.stream()
                  .collect(groupingBy(g -> g.getSource(), counting())));
          break;
        case "create_year":
          req.setAttribute("map",
              links.stream()
                  .collect(groupingBy(g -> g.getCreationDate().getYear(), counting())));
          break;
        case "visit_year":
          req.setAttribute("map",
              links.stream()
                  .collect(groupingBy(g -> g.getLastVisit() == null ? "Never" : g.getLastVisit().getYear(), counting())));
          break;
        case "labels":
          Map<String, Long> stats = new TreeMap<>(CASE_INSENSITIVE_ORDER);
          labels.forEach(l -> {
            stats.put(l, links.stream().filter(f -> f.getLabels().contains(l)).count());
          });
          req.setAttribute("map", stats);
          break;
      }

      req.getRequestDispatcher("/stats.jsp").include(req, res);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    super.doPost(req, res);
    Long userId = (Long) req.getSession().getAttribute(USER);

    if (userId == null) {
      return;
    }

    if (exists(req, "check")) {

      scheduleCleanup(req, userId);

      res.sendRedirect("/home");

    } else if (exists(req, "save")) {

      Link link = new Link();
      if (hasValue(req, "id")) {
        Long id = Long.parseLong(req.getParameter("id"));
        link = getLinkStorage().get(userId, id);
      }

      link.setDescription(req.getParameter("description"));
      link.setTitle(req.getParameter("title"));
      link.setUrl(req.getParameter("url"));

      if (hasValue(req, "labels")) {
        String label = req.getParameter("labels");
        link.setLabels(DomainUtil.parseLabels(label));
      }

      if (link.getId() == null) {
        link.setSource("MyStart");
        getLinkStorage().create(userId, link);
      } else {
        getLinkStorage().update(userId, link);
      }
      res.sendRedirect("/home");

    } else if (exists(req, "cancel")) {

      res.sendRedirect("/home");
    }
  }
}
