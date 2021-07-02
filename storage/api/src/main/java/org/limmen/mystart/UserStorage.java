package org.limmen.mystart;

import java.util.Optional;

public interface UserStorage {

  Optional<User> get(Long id);

  Optional<User> getByEmail(String email);

  Optional<User> getByResetCode(String resetCode);

  void remove(Long id);

  void store(User item);
}
