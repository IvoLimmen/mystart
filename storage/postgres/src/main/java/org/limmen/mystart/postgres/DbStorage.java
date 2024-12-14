package org.limmen.mystart.postgres;

import java.util.Properties;

import org.flywaydb.core.Flyway;
import org.limmen.mystart.CategoryStorage;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.StatsStorage;
import org.limmen.mystart.Storage;
import org.limmen.mystart.UserStorage;
import org.limmen.mystart.VisitStorage;

public class DbStorage implements Storage {

  private String url;
  private String user;
  private String password;

  private LinkStorage linkStorage;
  private CategoryStorage categoryStorage;
  private StatsStorage statsStorage;
  private UserStorage userStorage;
  private VisitStorage visitStorage;

  public DbStorage() {
  }

  @Override
  public LinkStorage getLinkStorage() {
    return this.linkStorage;
  }

  public String getName() {
    return "postgres";
  }

  @Override
  public StatsStorage getStatsStorage() {
    return this.statsStorage;
  }

  @Override
  public UserStorage getUserStorage() {
    return this.userStorage;
  }

  @Override
  public VisitStorage getVisitStorage() {
    return this.visitStorage;
  }

  @Override
  public CategoryStorage getCategoryStorage() {
    return this.categoryStorage;
  }

  @Override
  public void initialize(Properties properties) {

    this.url = properties.getProperty("server.db.jdbcUrl");
    this.user = properties.getProperty("server.db.username");
    this.password = properties.getProperty("server.db.password");

    Flyway.configure()
        .dataSource(url, user, password)
        .load()
        .migrate();

    this.linkStorage = new DbLinkStorage(user, password, url);
    this.userStorage = new DbUserStorage(user, password, url);
    this.visitStorage = new DbVisitStorage(user, password, url);
    this.statsStorage = new DbStatsStorage(user, password, url);
    this.categoryStorage = new DbCategoryStorage(user, password, url);
  }
}
