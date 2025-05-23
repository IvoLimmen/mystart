package org.limmen.mystart.postgres;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.limmen.mystart.Category;
import org.limmen.mystart.Link;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.criteria.Criteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbLinkStorage extends DbAbstractStorage implements LinkStorage {

  private final static Logger log = LoggerFactory.getLogger(DbLinkStorage.class);

  private static final Function<Result, Link> LINK_MAPPER = (var res) -> {
    var link = new Link();
    link.setId(res.lng("id"));
    link.setUrl(res.string("url"));
    link.setTitle(res.string("title"));
    link.setDescription(res.string("description"));
    link.setSource(res.string("source"));
    link.setCreationDate(res.localDateTime("creation_date"));
    link.setLastVisit(res.localDateTime("last_visit"));
    link.setLabels(Arrays.asList(res.stringArray("labels")));

    if (res.string("cat_name") != null) {
      var category = new Category();
      category.setName(res.string("cat_name"));
      category.setColor(res.string("cat_color"));
      category.setId(res.lng("cat_id"));
      link.setCategory(category);
    }

    return link;
  };

  public DbLinkStorage(String user, String password, String url) {
    super(user, password, url);
  }

  @Override
  public void create(Long userId, Link link) {
    String sql = "insert into links (user_id, description, source, title, url, labels, creation_date, category_id) "
        + "values (?, ?, ?, ?, ?, ?, ?, ?)";
    executeSql(sql, stmt -> {
      stmt.setLong(1, userId);
      stmt.setString(2, link.getDescription());
      stmt.setString(3, link.getSource());
      stmt.setString(4, link.getTitle());
      stmt.setString(5, link.getUrl());
      stmt.setStringArray(6, link.getLabels().toArray(new String[link.getLabels().size()]));
      stmt.setLocalDate(7, link.getCreationDate());
      if (link.getCategory() != null) {
        stmt.setLong(8, link.getCategory().getId());
      } else {
        stmt.setLongNull(8);
      }
      
    });
  }

  @Override
  public Link get(Long userId, Long id) {
    return executeSqlSingle("select l.*, c.name as cat_name, c.color as cat_color, c.id as cat_id from links l left outer join category c on c.id = l.category_id where l.user_id = ? and l.id = ?", args -> {
      args.setLong(1, userId);
      args.setLong(2, id);
    }, LINK_MAPPER);
  }

  @Override
  public Collection<Link> getAll(Long userId) {
    return executeSql("select l.*, c.name as cat_name, c.color as cat_color, c.id as cat_id from links l left outer join category c on c.id = l.category_id where l.user_id = ? order by l.title asc", args -> {
      args.setLong(1, userId);
    }, LINK_MAPPER);
  }

  @Override
  public Collection<Link> getAllByLabel(Long userId, String label) {
    if (label == null || label.equals("")) {
      return executeSql("select l.*, c.name as cat_name, c.color as cat_color, c.id as cat_id from links l left outer join category c on c.id = l.category_id where l.user_id = ? and l.labels = '{}' order by l.title asc", args -> {
        args.setLong(1, userId);
      }, LINK_MAPPER);
    }
    return executeSql("select l.*, c.name as cat_name, c.color as cat_color, c.id as cat_id from links l left outer join category c on c.id = l.category_id where l.user_id = ? and l.labels && array[?] order by l.title asc", args -> {
      args.setLong(1, userId);
      args.setStringArray(2, new String[] { label });
    }, LINK_MAPPER);
  }

  @Override
  public Collection<String> getAllLabels(Long userId) {
    return executeSqlStringCollection("select distinct unnest(labels) from links where user_id = ? order by 1",
        args -> {
          args.setLong(1, userId);
        });
  }

  @Override
  public Link getByUrl(Long userId, Link url) {

    String strippedUrl = url.getUrl();
    log.debug("Searching for url: {}", strippedUrl);

    return executeSqlSingle("select l.*, c.name as cat_name, c.color as cat_color, c.id as cat_id from links l left outer join category c on c.id = l.category_id where l.user_id = ? and lower(l.url) = ?", args -> {
      args.setLong(1, userId);
      args.setString(2, strippedUrl);
    }, LINK_MAPPER);
  }

  @Override
  public Collection<Link> getLastCreated(Long userId, int limit) {
    return executeSql("select l.*, c.name as cat_name, c.color as cat_color, c.id as cat_id from links l left outer join category c on c.id = l.category_id where l.user_id = ? order by l.creation_date desc", args -> {
      args.setLong(1, userId);
      args.setMaxRows(limit);
    }, LINK_MAPPER);
  }

  @Override
  public Collection<Link> getLastVisited(Long userId, int limit) {
    return executeSql(
        "select l.*, c.name as cat_name, c.color as cat_color, c.id as cat_id from links l join visits v on l.id = v.link_id left outer join category c on c.id = l.category_id where l.user_id = ? order by v.visit desc", args -> {
          args.setLong(1, userId);
          args.setMaxRows(limit);
        }, LINK_MAPPER);
  }

  @Override
  public Collection<Link> getSimilarByLink(Long userId, Link link) {
    String url = link.getUrl();

    if (url.startsWith("http://")) {
      url = url.substring(7, url.length());
    }
    if (url.startsWith("https://")) {
      url = url.substring(8, url.length());
    }
    if (url.endsWith("/")) {
      url = url.substring(0, url.length() - 1);
    }
    String strippedUrl = "%" + url + "%";
    log.debug("Searching for url: {}", strippedUrl);

    return executeSql("select l.*, c.name as cat_name, c.color as cat_color, c.id as cat_id from links l left outer join category c on c.id = l.category_id where l.user_id = ? and l.url ilike ? and l.id != ?", args -> {
      args.setLong(1, userId);
      args.setString(2, strippedUrl);
      args.setLong(3, link.getId());
    }, LINK_MAPPER);
  }

  @Override
  public Collection<Link> getSimilarByUrl(Long userId, String url) {
    if (url.startsWith("http://")) {
      url = url.substring(7, url.length());
    }
    if (url.startsWith("https://")) {
      url = url.substring(8, url.length());
    }
    if (url.endsWith("/")) {
      url = url.substring(0, url.length() - 1);
    }
    String strippedUrl = "%" + url + "%";
    log.debug("Searching for url: {}", strippedUrl);

    return executeSql("select l.*, c.name as cat_name, c.color as cat_color, c.id as cat_id from links l left outer join category c on c.id = l.category_id where l.user_id = ? and l.url ilike ?", args -> {
      args.setLong(1, userId);
      args.setString(2, strippedUrl);
    }, LINK_MAPPER);
  }

  @Override
  public void importCollection(Long userId, Collection<Link> links, boolean skipDuplicates) {
    AtomicInteger updated = new AtomicInteger(0);
    AtomicInteger created = new AtomicInteger(0);
    links.forEach(l -> {
      log.debug("Searching for URL: {}", l.getUrl());
      Link link = getByUrl(userId, l);
      if (link == null) {
        log.info("Creating link {}", l.getUrl());
        create(userId, l);
        created.incrementAndGet();
      } else {
        if (!skipDuplicates) {
          log.info("Updating labels of link {}", l.getUrl());
          link.addLabels(l.getLabels());
          update(userId, link);
          updated.incrementAndGet();
        } else {
          log.info("Skipping link {}", l.getUrl());
        }
      }
    });
    log.info("Updated {} links, created {} links", updated.get(), created.get());
  }

  @Override
  public Collection<Link> last20Visits(Long userId) {
    String sql = "select l.*, c.name as cat_name, c.color as cat_color, c.id as cat_id "
        + "from links l "
        + "join visits v ON v.link_id = l.id "
        + "left outer join category c on c.id = l.category_id "
        + "where l.user_id = ? "
        + "order by v.visit desc limit 20";

    return executeSql(sql, args -> {
      args.setLong(1, userId);
    }, LINK_MAPPER);
  }

  @Override
  public void remove(Long userId, Long id) {
    executeSql("delete from links where id = ? and user_id = ?", stmt -> {
      stmt.setLong(1, id);
      stmt.setLong(2, userId);
    });
  }

  @Override
  public Collection<Link> search(Long userId, Criteria criteria) {
    if (criteria == null) {
      throw new IllegalArgumentException();
    }

    StringBuilder sql = new StringBuilder();
    sql.append("select l.*, c.name as cat_name, c.color as cat_color, c.id as cat_id from links l left outer join category c on c.id = l.category_id where l.user_id = ? and (");
    sql.append(criteria.toSQL());
    sql.append(") order by l.title asc");

    AtomicInteger index = new AtomicInteger(0);
    return executeSql(sql.toString(), args -> {
      args.setLong(index.incrementAndGet(), userId);
      criteria.toArguments().forEach(c -> {
        if (c.getValueType().equals(String.class)) {
          args.setString(index.incrementAndGet(), "%" + c.getValue() + "%");
        }
        if (c.getValueType().equals(String[].class)) {
          args.setString(index.incrementAndGet(), (String) c.getValue());
        }
      });
    }, LINK_MAPPER);
  }

  @Override
  public void update(Long userId, Link link) {
    String sql = "update links "
        + "set description = ?, "
        + "title = ?, "
        + "source = ?, "
        + "url = ?, "
        + "labels = ?, "
        + "last_visit = ?, "
        + "category_id = ? "
        + "where id = ? "
        + "and user_id = ?";
    executeSql(sql, stmt -> {
      stmt.setString(1, link.getDescription());
      stmt.setString(2, link.getTitle());
      stmt.setString(3, link.getSource());
      stmt.setString(4, link.getUrl());
      stmt.setStringArray(5, link.getLabels().toArray(new String[link.getLabels().size()]));
      stmt.setLocalDate(6, link.getLastVisit());
      if (link.getCategory() != null) {
        stmt.setLong(7, link.getCategory().getId());
      } else {
        stmt.setLongNull(7);
      }      

      stmt.setLong(8, link.getId());
      stmt.setLong(9, userId);
    });
  }
}
