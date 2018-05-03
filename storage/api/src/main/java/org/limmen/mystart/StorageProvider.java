package org.limmen.mystart;

import com.typesafe.config.Config;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import org.limmen.mystart.exception.StorageException;

public class StorageProvider {

    private final static StorageProvider CURRENT = new StorageProvider();

    private final List<Storage> storageImplementations = new ArrayList<>();

    private StorageProvider() {

        ServiceLoader.load(Storage.class).forEach((s) -> {
            storageImplementations.add(s);
        });
    }

    public static Storage getStorageByName(Config conf, String name) throws StorageException {
        Storage storage = CURRENT.storageImplementations.stream()
            .filter(f -> f.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(RuntimeException::new);
		  
		  storage.initialize(conf);
		  
		  return storage;
    }
}
