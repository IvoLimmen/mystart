package org.limmen.mystart.postgres;

import com.typesafe.config.Config;
import org.flywaydb.core.Flyway;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.Storage;
import org.limmen.mystart.UserStorage;

public class DbStorage implements Storage {

  private String password;
  private String url;
  private String user;

  public DbStorage() {
  }

  @Override
  public LinkStorage getLinkStorage() {
    return new DbLinkStorage(user, password, url);
  }

  @Override
  public String getName() {
    return "postgres";
  }

  @Override
  public UserStorage getUserStorage() {
    return new DbUserStorage(user, password, url);
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
  }
}
