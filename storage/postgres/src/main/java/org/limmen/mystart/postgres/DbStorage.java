package org.limmen.mystart.postgres;

import com.typesafe.config.Config;
import org.flywaydb.core.Flyway;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.Storage;
import org.limmen.mystart.UserStorage;
import org.limmen.mystart.VisitStorage;

public class DbStorage implements Storage {

  private LinkStorage linkStorage;
  private String password;
  private String url;
  private String user;
  private UserStorage userStorage;
  private VisitStorage visitStorage;

  public DbStorage() {
  }

  @Override
  public LinkStorage getLinkStorage() {
    return this.linkStorage;
  }

  @Override
  public String getName() {
    return "postgres";
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
  public void initialize(Config conf) {

    this.url = conf.getString("server.db.jdbcUrl");
    this.user = conf.getString("server.db.username");
    this.password = conf.getString("server.db.password");

    Flyway.configure()
        .dataSource(url, user, password)
        .load()
        .migrate();

    this.linkStorage = new DbLinkStorage(user, password, url);
    this.userStorage = new DbUserStorage(user, password, url);
    this.visitStorage = new DbVisitStorage(user, password, url);
  }
}
