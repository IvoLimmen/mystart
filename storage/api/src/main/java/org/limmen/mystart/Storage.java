package org.limmen.mystart;

import com.typesafe.config.Config;
import org.limmen.mystart.exception.StorageException;

public interface Storage {

    LinkStorage getLinkStorage();

    String getName();

    UserStorage getUserStorage();

    void initialize(Config conf) throws StorageException;
}
