package org.limmen.mystart;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.limmen.mystart.exception.StorageException;

public class SerializeLinkStorage implements LinkStorage {

   private final Map<Long, SingleUserSerializeLinkStorage> storageMap = new HashMap<>();
   
   private SingleUserSerializeLinkStorage getStorage(Long userId) throws StorageException {
      SingleUserSerializeLinkStorage storage = storageMap.get(userId);
      
      if (storage == null) {
         storage = new SingleUserSerializeLinkStorage(Long.toString(userId));
         storageMap.put(userId, storage);
      }
      
      return storage;
   }
   
   @Override
   public Collection<Link> getAllByLabel(Long userId, String tagName) throws StorageException {
      return this.getStorage(userId).getAllByLabel(tagName);
   }

   @Override
   public Collection<String> getAllLabels(Long userId) throws StorageException {
      return this.getStorage(userId).getAllLabels();
   }

   @Override
   public void storeCollection(Long userId, Collection<Link> link) throws StorageException {
      this.getStorage(userId).storeCollection(link);
   }

   @Override
   public void store(Long userId, Link item) throws StorageException {
      this.getStorage(userId).store(item);
   }

   @Override
   public void remove(Long userId, Long id) throws StorageException {
      this.getStorage(userId).remove(id);
   }

   @Override
   public Link get(Long userId, Long id) throws StorageException {
      return this.getStorage(userId).get(id);
   }

   @Override
   public Collection<Link> getAll(Long userId) throws StorageException {
      return this.getStorage(userId).getAll();
   }
}
