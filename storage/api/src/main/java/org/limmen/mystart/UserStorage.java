package org.limmen.mystart;

import java.util.Collection;

public interface UserStorage {

  User get(Long id);

  Collection<User> getAll();

  User getByEmail(String email);

  User getByResetCode(String resetCode);

  void remove(Long id);

  void store(User item);
}
