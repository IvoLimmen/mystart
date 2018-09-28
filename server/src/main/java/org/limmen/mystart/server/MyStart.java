package org.limmen.mystart.server;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.limmen.mystart.AutoDetectParser;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.Parser;
import org.limmen.mystart.Storage;
import org.limmen.mystart.StorageProvider;
import org.limmen.mystart.UserStorage;
import org.limmen.mystart.exception.StorageException;

public class MyStart {

  public void initialize() throws StorageException {
    Config conf = ConfigFactory.load();

    Storage storage = StorageProvider.getStorageByName(conf, conf.getString("server.storage"));
    UserStorage userStorage = storage.getUserStorage();
    LinkStorage linkStorage = storage.getLinkStorage();

    Parser parser = new AutoDetectParser();

  }
}
