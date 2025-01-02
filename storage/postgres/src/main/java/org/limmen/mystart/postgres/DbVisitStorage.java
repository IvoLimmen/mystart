package org.limmen.mystart.postgres;

import java.time.LocalDateTime;
import java.util.List;

import org.limmen.mystart.VisitStorage;

public class DbVisitStorage extends DbAbstractStorage implements VisitStorage {

  public DbVisitStorage(String user, String password, String url) {
    super(user, password, url);
  }

  @Override
  public List<LocalDateTime> getLast20Visists(Long linkId) {
    String sql = "select visit from visits where link_id = ? order by visit desc limit 20";
    return executeSqlTimestampCollection(sql, args -> {
                                       args.setLong(1, linkId);
                                     });
  }

  @Override
  public void visit(Long linkId) {
    String sql = "insert into visits (link_id, visit) "
        + "values (?, ?)";
    executeSql(sql, args -> {
             args.setLong(1, linkId);
             args.setLocalDate(2, LocalDateTime.now());
           });
  }
}
