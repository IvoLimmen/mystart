package org.limmen.mystart;

import java.util.Collection;
import org.limmen.mystart.exception.StorageException;

public interface UserStorage {

  User get(Long id) throws StorageException;

  Collection<User> getAll() throws StorageException;

  User getByEmail(String email) throws StorageException;

  User getByNameOrEmail(String nameOrEmail) throws StorageException;

  void remove(Long id) throws StorageException;

  void store(User item) throws StorageException;
}
