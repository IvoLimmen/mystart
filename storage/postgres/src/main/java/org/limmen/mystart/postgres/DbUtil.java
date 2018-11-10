package org.limmen.mystart.postgres;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DbUtil {

  public static LocalDateTime toLocateDateTime(Timestamp ts) {
    if (ts == null) {
      return null;
    } else {
      return ts.toLocalDateTime();
    }
  }

  public static Timestamp toTimestamp(LocalDateTime date) {
    if (date == null) {
      return null;
    } else {
      return Timestamp.valueOf(date);
    }
  }
}
