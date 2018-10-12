package org.limmen.mystart;

import com.typesafe.config.Config;
import org.flywaydb.core.Flyway;
import org.limmen.mystart.mystart.public_.ms_link.MsLinkManager;
import org.limmen.mystart.mystart.public_.ms_user.MsUserManager;

public class DbStorage implements Storage {

  private MyStartApplication app;
  private MsLinkManager links;
  private MsUserManager users;

  public DbStorage() {
  }

  @Override
  public LinkStorage getLinkStorage() {
    return new DbLinkStorage(links);
  }

  @Override
  public String getName() {
    return "speedment";
  }

  @Override
  public UserStorage getUserStorage() {
    return new DbUserStorage(users);
  }

  @Override
  public void initialize(Config conf) {

    String url = conf.getString("server.db.jdbcUrl");
    String user = conf.getString("server.db.username");
    String password = conf.getString("server.db.password");

    Flyway flyway = Flyway.configure().dataSource(url, user, password).load();
    flyway.migrate();

    this.app = new MyStartApplicationBuilder()
        .withConnectionUrl(url)
        .withUsername(user)
        .withPassword(password)
        .build();

    links = app.getOrThrow(MsLinkManager.class);
    users = app.getOrThrow(MsUserManager.class);
  }
}
