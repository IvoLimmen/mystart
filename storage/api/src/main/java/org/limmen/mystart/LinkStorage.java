package org.limmen.mystart;

import java.util.Collection;
import org.limmen.mystart.exception.StorageException;

public interface LinkStorage {

  void create(Long userId, Link item);

  Link get(Long userId, Long id) throws StorageException;

  Collection<Link> getAll(Long userId) throws StorageException;

  Collection<Link> getAllByLabel(Long userId, String tagName) throws StorageException;

  Collection<String> getAllLabels(Long userId) throws StorageException;

  void importCollection(Long userId, Collection<Link> link, boolean skipDuplicates) throws StorageException;

  void remove(Long userId, Long id) throws StorageException;

  void update(Long userId, Link item) throws StorageException;
}
