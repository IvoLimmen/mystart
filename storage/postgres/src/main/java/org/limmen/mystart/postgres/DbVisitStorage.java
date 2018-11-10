package org.limmen.mystart.postgres;

import java.time.LocalDateTime;
import java.util.Set;
import org.limmen.mystart.VisitStorage;

public class DbVisitStorage extends DbAbstractStorage implements VisitStorage {

  public DbVisitStorage(String user, String password, String url) {
    super(user, password, url);
  }

  @Override
  public Set<LocalDateTime> getLast20Visists(Long linkId) {
    String sql = "select visit from visits where link_id = ? order by visit";
    return executeSqlTimestampCollection(sql, args -> {
                                       args.setLong(1, linkId);
                                     });
  }

}
