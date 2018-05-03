package org.limmen.mystart;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import org.limmen.mystart.exception.StorageException;

@ApplicationScoped
public class StorageProducer {

  private LinkStorage linkStorage;

  private UserStorage userStorage;

  public StorageProducer() {
    try {
      Config conf = ConfigFactory.load();
      Storage storage = StorageProvider.getStorageByName(conf, conf.getString("server.storage"));
      this.linkStorage = storage.getLinkStorage();
      this.userStorage = storage.getUserStorage();
    } catch (StorageException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Produces
  public LinkStorage linkStorage() {
    return this.linkStorage;
  }

  @Produces
  public UserStorage userStorage() {
    return this.userStorage;
  }
}
