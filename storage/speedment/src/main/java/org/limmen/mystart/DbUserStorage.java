package org.limmen.mystart;

import java.util.Collection;
import java.util.stream.Collectors;
import org.limmen.mystart.exception.StorageException;
import org.limmen.mystart.mystart.public_.ms_user.MsUser;
import org.limmen.mystart.mystart.public_.ms_user.MsUserImpl;
import org.limmen.mystart.mystart.public_.ms_user.MsUserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbUserStorage implements UserStorage {

   protected static final Logger LOGGER = LoggerFactory.getLogger(DbUserStorage.class);

   private final MsUserManager users;

   public DbUserStorage(MsUserManager users) {
      this.users = users;
   }

   @Override
   public void store(User item) throws StorageException {
      User u = getByEmail(item.getEmail());
      if (u == null) {
         MsUser user = new MsUserImpl();
         user.setEmail(item.getEmail());
         user.setPassword(item.getPassword());

         users.persist(user);
      }
   }

   @Override
   public void remove(Long id) throws StorageException {
      users.stream()
          .filter(MsUser.ID.equal(id.intValue()))
          .forEach(users.remover());
   }

   @Override
   public User getByEmail(String email) throws StorageException {
      return fromDb(users.stream()
          .filter(MsUser.EMAIL.equal(email))
          .findFirst().orElse(null));
   }

   private User fromDb(MsUser user) {
      if (user == null) {
         return null;
      }
      User u = new User(user.getEmail().get(), user.getPassword().get());
      u.setId((long) user.getId());
      u.setPassword(user.getPassword().get());
      return u;
   }

   @Override
   public User get(Long id) throws StorageException {
      return fromDb(users.stream()
          .filter(MsUser.ID.equal(id.intValue()))
          .findFirst().get());
   }

   @Override
   public Collection<User> getAll() throws StorageException {
      return users.stream().map(this::fromDb).collect(Collectors.toList());
   }
}
