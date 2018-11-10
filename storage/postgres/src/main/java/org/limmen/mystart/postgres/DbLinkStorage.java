package org.limmen.mystart.postgres;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.limmen.mystart.Link;
import org.limmen.mystart.LinkStorage;

@Slf4j
public class DbLinkStorage extends DbAbstractStorage implements LinkStorage {

  private static final Function<Result, Link> LINK_MAPPER = res -> {
    Link l = new Link();
    l.setId(res.lng("id"));
    l.setUrl(res.string("url"));
    l.setTitle(res.string("title"));
    l.setDescription(res.string("description"));
    l.setSource(res.string("source"));
    l.setCheckResult(res.string("check_result"));
    l.setCreationDate(res.localDateTime("creation_date"));
    l.setLastVisit(res.localDateTime("last_visit"));
    l.setLastCheck(res.localDateTime("last_check"));
    l.setLabels(Arrays.asList(res.stringArray("labels")));
    l.setPrivateNetwork(res.bool("private_network"));
    return l;
  };

  public DbLinkStorage(String user, String password, String url) {
    super(user, password, url);
  }

  @Override
  public void create(Long userId, Link link) {
    String sql = "insert into links (user_id, description, source, title, url, labels, creation_date) "
        + "values (?, ?, ?, ?, ?, ?, ?)";
    executeSql(sql, stmt -> {
             stmt.setLong(1, userId);
      stmt.setString(2, link.getDescription());
      stmt.setString(3, link.getSource());
      stmt.setString(4, link.getTitle());
      stmt.setString(5, link.getUrl());
      stmt.setStringArray(6, link.getLabels().toArray(new String[link.getLabels().size()]));
      stmt.setLocalDate(7, link.getCreationDate());
           });
  }

  @Override
  public Link get(Long userId, Long id) {
    return executeSqlSingle("select * from links where user_id = ? and id = ?", args -> {
                          args.setLong(1, userId);
                          args.setLong(2, id);
                        }, LINK_MAPPER);
  }

  @Override
  public Collection<Link> getAll(Long userId) {
    return executeSql("select * from links where user_id = ?", args -> {
                    args.setLong(1, userId);
                  }, LINK_MAPPER);
  }

  @Override
  public Collection<Link> getAllByLabel(Long userId, String label) {
    return executeSql("select * from links where user_id = ? and labels && array[?]", args -> {
                    args.setLong(1, userId);
      args.setStringArray(2, new String[]{label});
                  }, LINK_MAPPER);
  }

  @Override
  public Collection<String> getAllLabels(Long userId) {
    return executeSqlStringCollection("select distinct unnest(labels) from links where user_id = ? order by 1", args -> {
                                    args.setLong(1, userId);
                                  });
  }

  @Override
  public Link getByUrl(Long userId, String url) {
    return executeSqlSingle("select * from links where user_id = ? and url = ?", args -> {
                          args.setLong(1, userId);
                          args.setString(2, url);
                        }, LINK_MAPPER);
  }

  @Override
  public void importCollection(Long userId, Collection<Link> links, boolean skipDuplicates) {
    links.forEach(l -> {
      Link link = getByUrl(userId, l.getUrl());
      if (link == null) {
        log.info("Creating link {}", l.getUrl());
        create(userId, l);
      } else {
        if (!skipDuplicates) {
          log.info("Updating link {}", l.getUrl());
          link.addLabels(l.getLabels());
          update(userId, link);
        } else {
          log.info("Scipping link {}", l.getUrl());
        }
      }
    });
  }

  @Override
  public void remove(Long userId, Long id) {
    executeSql("delete from links where id = ? and user_id = ?", stmt -> {
             stmt.setLong(1, id);
             stmt.setLong(2, userId);
           });
  }

  @Override
  public void update(Long userId, Link link) {
    String sql = "update links "
        + "set description = ?,"
        + "title = ?,"
        + "source = ?,"
        + "url = ?,"
        + "labels = ?,"
        + "private_network = ?,"
        + "check_result = ?,"
        + "last_check = ?,"
        + "last_visit = ?"
        + "where id = ?"
        + "and user_id = ?";
    executeSql(sql, stmt -> {
             stmt.setString(1, link.getDescription());
      stmt.setString(2, link.getTitle());
      stmt.setString(3, link.getSource());
      stmt.setString(4, link.getUrl());
      stmt.setStringArray(5, link.getLabels().toArray(new String[link.getLabels().size()]));
      stmt.setBool(6, link.isPrivateNetwork());
      stmt.setString(7, link.getCheckResult());
      stmt.setLocalDate(8, link.getLastCheck());
      stmt.setLocalDate(9, link.getLastVisit());

      stmt.setLong(10, link.getId());
      stmt.setLong(11, userId);
           });
  }
}
