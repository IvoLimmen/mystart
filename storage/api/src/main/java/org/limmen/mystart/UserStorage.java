package org.limmen.mystart;

import java.util.Collection;
import org.limmen.mystart.exception.StorageException;

public interface UserStorage {

  void store(User item) throws StorageException;

  void remove(Long id) throws StorageException;

  User getByNameOrEmail(String nameOrEmail) throws StorageException;

  User getByEmail(String email) throws StorageException;

  User get(Long id) throws StorageException;

  Collection<User> getAll() throws StorageException;
}
