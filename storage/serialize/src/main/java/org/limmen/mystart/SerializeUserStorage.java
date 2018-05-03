package org.limmen.mystart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.limmen.mystart.exception.StorageException;

public final class SerializeUserStorage extends BaseSerializeStorage implements UserStorage {

   private String fileForUsers;

   private List<User> users = new ArrayList<>();

   public SerializeUserStorage() throws StorageException {
      fileForUsers = getFileName("users.ser");

      try {
         load();
      } catch (StorageException ex) {
         save();
      }
   }

   private void save() throws StorageException {
      save(users, fileForUsers);
   }

   @SuppressWarnings("unchecked")
   private void load() throws StorageException {
      this.users = load(fileForUsers);
   }

   @Override
   public void store(User item) throws StorageException {
      Optional<User> current = this.users.stream().filter(l -> {
         return l.equals(item);
      }).findFirst();

      if (!current.isPresent()) {
         this.users.add(item);
      } else {
         LOGGER.info("Updating user {} as we allready have it.", item.getEmail());

         current.get().setPassword(item.getPassword());
      }

      save();
   }

   @Override
   public void remove(Long id) throws StorageException {
      Optional<User> user = this.users.stream().filter(p -> {
         return id != null && id.equals(p.getId());
      }).findFirst();

      if (user.isPresent()) {
         this.users.remove(user.get());
         save();
      }
   }

   @Override
   public User get(Long id) throws StorageException {
      Optional<User> user = this.users.stream().filter(p -> {
         return id.equals(p.getId());
      }).findFirst();

      if (user.isPresent()) {
         return user.get();
      } else {
         return null;
      }
   }

   @Override
   public User getByEmail(String email) throws StorageException {
      Optional<User> user = this.users.stream().filter(p -> {
         return email.equals(p.getEmail());
      }).findFirst();

      if (user.isPresent()) {
         return user.get();
      } else {
         return null;
      }
   }

   @Override
   public Collection<User> getAll() {
      return this.users;
   }
}
