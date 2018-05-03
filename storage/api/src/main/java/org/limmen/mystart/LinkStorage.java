package org.limmen.mystart;

import java.util.Collection;
import org.limmen.mystart.exception.StorageException;

public interface LinkStorage {

	Collection<Link> getAllByLabel(Long userId, String tagName) throws StorageException;

	Collection<String> getAllLabels(Long userId) throws StorageException;
   
   void storeCollection(Long userId, Collection<Link> link) throws StorageException;

   void store(Long userId, Link item) throws StorageException;

   void remove(Long userId, Long id) throws StorageException;

   Link get(Long userId, Long id) throws StorageException;
   
   Collection<Link> getAll(Long userId) throws StorageException;   
}
