package org.limmen.mystart;

import com.typesafe.config.Config;
import org.limmen.mystart.exception.StorageException;

public class SerializeStorage implements Storage {

    private LinkStorage linkStorage;

    private UserStorage userStorage;

    @Override
    public LinkStorage getLinkStorage() {
        return linkStorage;
    }

    @Override
    public String getName() {
        return "serialize";
    }

    @Override
    public UserStorage getUserStorage() {
        return userStorage;
    }

    @Override
    public void initialize(Config conf) throws StorageException {
        linkStorage = new SerializeLinkStorage();
        userStorage = new SerializeUserStorage();
    }
}
