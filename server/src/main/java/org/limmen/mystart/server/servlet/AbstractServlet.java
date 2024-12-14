package org.limmen.mystart.server.servlet;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.text.StringEscapeUtils;
import org.limmen.mystart.CategoryStorage;
import org.limmen.mystart.Link;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.StatsStorage;
import org.limmen.mystart.Storage;
import org.limmen.mystart.User;
import org.limmen.mystart.UserStorage;
import org.limmen.mystart.VisitStorage;
import org.limmen.mystart.server.support.PropertyHelper;

import emoji4j.EmojiManager;
import emoji4j.EmojiUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AbstractServlet extends HttpServlet {

  public static final String USER = "user";
  public static final String USER_ID = "userId";

  private static final Map<String, String> FLAIR = new HashMap<>();
  private static final long serialVersionUID = 1L;

  static {
    // common domains
    FLAIR.put("github.com", "fa-github");
    FLAIR.put("gitlab.com", "fa-gitlab");
    FLAIR.put("bitbucket.org", "fa-bitbucket");
    FLAIR.put("stackoverflow.com", "fa-stack-overflow");
    FLAIR.put("bandcamp.com", "fa-bandcamp");
    FLAIR.put("blogger.com", "fa-blogger");
    FLAIR.put("last.fm", "fa-lastfm");
    FLAIR.put("soundcloud.com", "fa-soundcloud");
    FLAIR.put("youtube.com", "fa-youtube");
    FLAIR.put("reddit.com", "fa-reddit");
    FLAIR.put("yelp.com", "fa-yelp");
    FLAIR.put("slack.com", "fa-slack");
    FLAIR.put("amazon.com", "fa-amazon");
    FLAIR.put("google.com", "fa-google");
    FLAIR.put("medium.com", "fa-medium");
    FLAIR.put("news.ycombinator.com", "fa-hacker-news");

    EmojiManager.addStopWords("http://", "https://");
  }

  private final Storage storage;

  private final Path temporaryDirectory;

  private final Properties properties;

  public AbstractServlet(Storage storage,
      Path temporaryDirectory,
      Properties properties) {
    this.storage = storage;
    this.temporaryDirectory = temporaryDirectory;
    this.properties = properties;
  }

  public String getBookmarkletUrl() {
    String serverName = PropertyHelper.getServerName(properties);
    return String.format(
        "javascript:(function(){var a=window,b=document,c=encodeURIComponent,d=a.open(\"https://%s/link?edit&url=\"+c(b.location)+\"&title=\"+c(b.title),\"popup\",\"left=\"+((a.screenX||a.screenLeft)+10)+\",top=\"+((a.screenY||a.screenTop)+10)+\",height=750px,width=600px,resizable=1,alwaysRaised=1\");a.setTimeout(function(){d.focus()},300)})();",
        serverName);
  }

  public String getTitle(Link link) {
    if (link == null || link.getTitle() == null) {
      return null;
    }

    return EmojiUtils.htmlify(StringEscapeUtils.unescapeHtml4(link.getTitle()));
  }

  public String getDescription(Link link) {
    if (link == null || link.getDescription() == null) {
      return null;
    }
    return EmojiUtils.htmlify(StringEscapeUtils.unescapeHtml4(link.getDescription()));
  }

  public String getBoxStyle(Link link) {
    if (link.getCategory() == null) {
      return "border-width: 1px; border-style: solid; border-color: #d2d6de;";
    } else {
      return "border-width: 1px; border-style: solid; border-color: " + link.getCategory().getColor() + ";";
    }
  }

  public String getBoxHeaderStyle(Link link) {
    if (link.getCategory() == null) {
      return "background-color: #d2d6de;";
    } else {
      return "background-color: " + link.getCategory().getColor() + ";";
    }
  }
  
  public String getFlair(Link link) {
    if (link.getHost() == null) {
      return null;
    }

    return FLAIR.keySet().stream()
        .filter(filter -> link.getHost().toLowerCase().contains(filter))
        .findFirst()
        .orElse(null);
  }

  public LinkStorage getLinkStorage() {
    return this.storage.getLinkStorage();
  }

  public CategoryStorage getCategoryStorage() {
    return this.storage.getCategoryStorage();
  }

  public StatsStorage getStatsStorage() {
    return this.storage.getStatsStorage();
  }

  public Storage getStorage() {
    return storage;
  }

  public Path getTemporaryDirectory() {
    return temporaryDirectory;
  }

  public UserStorage getUserStorage() {
    return this.storage.getUserStorage();
  }

  public VisitStorage getVisitStorage() {
    return this.storage.getVisitStorage();
  }

  protected void addCookie(HttpServletResponse res, String key, String value) {
    Cookie cookie = new Cookie(key, value);
    cookie.setMaxAge(60 * 60 * 24 * 7); // week
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/");
    res.addCookie(cookie);
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
        String email = null;
        String password = null;
        for (Cookie cookie : cookies) {
          if (cookie.getName().equals("mystart")) {
            String[] value = cookie.getValue().split("\\|");
            email = value[0];
            password = value[1];
          }
        }
        // check cookie data
        if (password != null & email != null) {
          User user = getUserStorage().getByEmail(email);
          if (user != null && user.getPassword().equals(password)) {
            req.getSession().setAttribute(USER_ID, user.getId());
          } else {
            clearCookies(req, res);
          }
        }
      }

      if (req.getSession().getAttribute(USER_ID) == null && !"/login".equals(req.getServletPath())) {
        req.getRequestDispatcher("/login.jsp").include(req, res);
      }
    }

    if (req.getSession().getAttribute(USER_ID) != null) {
      Long userId = (Long) req.getSession().getAttribute(USER_ID);
      User user = getUserStorage().get(userId);
      req.setAttribute(USER_ID, userId);
      req.setAttribute(USER, user);
      req.setAttribute("menulabels", user.getMenuLabels());
      req.setAttribute("links", getLinkStorage().getAllByLabel(userId, user.getAutoStartLabel()));
      req.setAttribute("flair", FLAIR);
      req.setAttribute("util", this);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    if (req.getSession().getAttribute(USER_ID) == null) {
      res.sendRedirect("/home");
      return;
    } else {
      Long userId = (Long) req.getSession().getAttribute(USER_ID);
      User user = getUserStorage().get(userId);
      req.setAttribute(USER_ID, userId);
      req.setAttribute(USER, user);
      req.setAttribute("menulabels", user.getMenuLabels());
      req.setAttribute("links", getLinkStorage().getAllByLabel(userId, user.getAutoStartLabel()));
      req.setAttribute("flair", FLAIR);
      req.setAttribute("util", this);      
    }
  }

  protected boolean exists(HttpServletRequest req, String parameter) {
    if (req.getMethod().equalsIgnoreCase("post") && req.getContentType().contains("multipart/form-data")) {
      try {
        return req.getPart(parameter) != null;
      } catch (IOException | ServletException e) {
        e.printStackTrace();
      }  
    }
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

    if (referer != null && referer.contains("?")) {
      int index = referer.indexOf("?");
      return referer.substring(1 + index);
    }

    return referer;
  }

  protected boolean hasValue(HttpServletRequest req, String parameter) {
    return req.getParameter(parameter) != null && req.getParameter(parameter).length() > 0;
  }

  protected boolean hasValue(HttpServletRequest req, String parameter, String value) {
    return value.equals(req.getParameter(parameter));
  }
}
