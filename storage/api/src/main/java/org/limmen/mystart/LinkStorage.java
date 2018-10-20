package org.limmen.mystart;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

public interface LinkStorage {

  void create(Long userId, Link item);

  Link get(Long userId, Long id);

  Collection<Link> getAll(Long userId);

  Collection<Link> getAllByLabel(Long userId, String tagName);

  Collection<String> getAllLabels(Long userId);

  Set<LocalDateTime> getAllVisists(Long id);

  Link getByUrl(Long userId, String url);

  void importCollection(Long userId, Collection<Link> link, boolean skipDuplicates);

  void remove(Long userId, Long id);

  void update(Long userId, Link item);
}
