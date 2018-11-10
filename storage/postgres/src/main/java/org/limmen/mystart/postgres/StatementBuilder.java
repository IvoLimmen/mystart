package org.limmen.mystart.postgres;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import org.limmen.mystart.exception.StorageException;

public class StatementBuilder {

  private final Connection connection;
  private final PreparedStatement preparedStatement;

  public StatementBuilder(Connection connection, PreparedStatement preparedStatement) {
    this.connection = connection;
    this.preparedStatement = preparedStatement;
  }

  public void setBool(int index, Boolean arg) {
    try {
      this.preparedStatement.setBoolean(index, arg);
    } catch (SQLException ex) {
      throw new StorageException(ex);
    }
  }

  public void setLocalDate(int index, LocalDateTime date) {
    try {
      this.preparedStatement.setTimestamp(index, DbUtil.toTimestamp(date));
    } catch (SQLException ex) {
      throw new StorageException(ex);
    }
  }

  public void setLong(int index, Long arg) {
    try {
      this.preparedStatement.setLong(index, arg);
    } catch (SQLException ex) {
      throw new StorageException(ex);
    }
  }

  public void setString(int index, String arg) {
    try {
      this.preparedStatement.setString(index, arg);
    } catch (SQLException ex) {
      throw new StorageException(ex);
    }
  }

  public void setStringArray(int index, String[] array) {
    try {
      Array obj = connection.createArrayOf("text", array);
      this.preparedStatement.setArray(index, obj);
    } catch (SQLException ex) {
      throw new StorageException(ex);
    }
  }
}
