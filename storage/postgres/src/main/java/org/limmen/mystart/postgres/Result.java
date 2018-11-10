package org.limmen.mystart.postgres;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import org.limmen.mystart.exception.StorageException;

public class Result {

  private final ResultSet resultSet;

  public Result(ResultSet resultSet) {
    this.resultSet = resultSet;
  }

  public boolean bool(String column) {
    try {
      return this.resultSet.getBoolean(column);
    } catch (SQLException ex) {
      throw new StorageException(ex);
    }
  }

  public int integer(String column) {
    try {
      return this.resultSet.getInt(column);
    } catch (SQLException ex) {
      throw new StorageException(ex);
    }
  }

  public long lng(String column) {
    try {
      return this.resultSet.getLong(column);
    } catch (SQLException ex) {
      throw new StorageException(ex);
    }
  }

  public LocalDateTime localDateTime(String column) {
    try {
      return DbUtil.toLocateDateTime(this.resultSet.getTimestamp(column));
    } catch (SQLException ex) {
      throw new StorageException(ex);
    }
  }

  public String string(String column) {
    try {
      return this.resultSet.getString(column);
    } catch (SQLException ex) {
      throw new StorageException(ex);
    }
  }

  public String[] stringArray(String column) {
    try {
      Array array = this.resultSet.getArray(column);
      if (array == null) {
        return null;
      }
      return (String[]) array.getArray();
    } catch (SQLException ex) {
      throw new StorageException(ex);
    }
  }
}
