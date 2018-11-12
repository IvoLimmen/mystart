package org.limmen.mystart;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;
import org.limmen.mystart.exception.StorageException;

public class StorageProvider {

  private final static StorageProvider CURRENT = new StorageProvider();

  public static Storage getStorageByName(Properties properties, String name) throws StorageException {
    Storage storage = CURRENT.storageImplementations.stream()
        .filter(f -> f.getName().equalsIgnoreCase(name))
        .findFirst()
        .orElseThrow(RuntimeException::new);

    storage.initialize(properties);

    return storage;
  }

  private final List<Storage> storageImplementations = new ArrayList<>();

  private StorageProvider() {

    ServiceLoader.load(Storage.class).forEach((s) -> {
      storageImplementations.add(s);
    });
  }

}
