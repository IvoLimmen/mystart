package org.limmen.mystart.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Function;

import org.limmen.mystart.exception.StorageException;

public abstract class DbAbstractStorage {

  
  private final String password;
  private final String url;
  private final String user;

  public DbAbstractStorage(String user, String password, String url) {
    this.user = user;
    this.password = password;
    this.url = url;
  }

  private Connection connection() throws SQLException {
    return DriverManager.getConnection(url, user, password);
  }

  protected void executeSql(String sql, Consumer<StatementBuilder> arguments) {
    try (Connection connection = connection()) {
      try (PreparedStatement statement = connection.prepareStatement(sql)) {
        arguments.accept(new StatementBuilder(connection, statement));
        statement.executeUpdate();
      }
    } catch (SQLException e) {
      throw new StorageException(e);
    }
  }

  protected <T> List<T> executeSql(String sql, Consumer<StatementBuilder> arguments, Function<Result, T> mapper) {
    List<T> results = new ArrayList<>();
    try (Connection connection = connection()) {
      try (PreparedStatement statement = connection.prepareStatement(sql)) {
        arguments.accept(new StatementBuilder(connection, statement));
        try (ResultSet resultSet = statement.executeQuery()) {
          while (resultSet.next()) {
            results.add(mapper.apply(new Result(resultSet)));
          }
        }
      }
    } catch (SQLException e) {
      throw new StorageException(e);
    }
    return results;
  }

  protected <T> T executeSqlSingle(String sql, Consumer<StatementBuilder> arguments, Function<Result, T> mapper) {
    try (Connection connection = connection()) {
      try (PreparedStatement statement = connection.prepareStatement(sql)) {
        arguments.accept(new StatementBuilder(connection, statement));
        try (ResultSet resultSet = statement.executeQuery()) {
          while (resultSet.next()) {
            return mapper.apply(new Result(resultSet));
          }
        }
      }
    } catch (SQLException e) {
      throw new StorageException(e);
    }
    return null;
  }

  protected List<String> executeSqlStringCollection(String sql, Consumer<StatementBuilder> arguments) {
    List<String> results = new ArrayList<>();
    try (Connection connection = connection()) {
      try (PreparedStatement statement = connection.prepareStatement(sql)) {
        arguments.accept(new StatementBuilder(connection, statement));
        try (ResultSet resultSet = statement.executeQuery()) {
          while (resultSet.next()) {
            results.add(resultSet.getString(1));
          }
        }
      }
    } catch (SQLException e) {
      throw new StorageException(e);
    }
    return results;
  }

  protected List<LocalDateTime> executeSqlTimestampCollection(String sql, Consumer<StatementBuilder> arguments) {
    List<LocalDateTime> results = new ArrayList<>();
    try (Connection connection = connection()) {
      try (PreparedStatement statement = connection.prepareStatement(sql)) {
        arguments.accept(new StatementBuilder(connection, statement));
        try (ResultSet resultSet = statement.executeQuery()) {
          while (resultSet.next()) {
            results.add(DbUtil.toLocateDateTime(resultSet.getTimestamp(1)));
          }
        }
      }
    } catch (SQLException e) {
      throw new StorageException(e);
    }
    return results;
  }

  protected Map<String, Long> executeStatisticsSql(String sql, Consumer<StatementBuilder> arguments, Function<Result, Map.Entry<String, Long>> mapper) {
    Map<String, Long> results = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    try (Connection connection = connection()) {
      try (PreparedStatement statement = connection.prepareStatement(sql)) {
        arguments.accept(new StatementBuilder(connection, statement));
        try (ResultSet resultSet = statement.executeQuery()) {
          while (resultSet.next()) {
            Map.Entry<String, Long> entry = mapper.apply(new Result(resultSet));
            results.put(entry.getKey(), entry.getValue());
          }
        }
      }
    } catch (SQLException e) {
      throw new StorageException(e);
    }
    return results;
  }
}
