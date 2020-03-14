package org.limmen.mystart;

public interface UserStorage {

  User get(Long id);

  User getByEmail(String email);

  User getByResetCode(String resetCode);

  void remove(Long id);

  void store(User item);
}
