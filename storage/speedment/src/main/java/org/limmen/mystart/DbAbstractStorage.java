package org.limmen.mystart;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

public abstract class DbAbstractStorage {

  protected Timestamp ts(LocalDateTime date) {
    if (date == null) {
      return null;
    } else {
      return Timestamp.valueOf(date);
    }
  }

  protected LocalDateTime date(Optional<Timestamp> ts) {
    if (!ts.isPresent()) {
      return null;
    } else {
      return ts.get().toLocalDateTime();
    }
  }

}
