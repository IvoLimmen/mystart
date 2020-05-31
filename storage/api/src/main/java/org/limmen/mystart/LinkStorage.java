package org.limmen.mystart;

import java.util.Collection;
import org.limmen.mystart.criteria.AbstractCriteria;

public interface LinkStorage {

  void create(Long userId, Link item);

  Link get(Long userId, Long id);

  Collection<Link> getAll(Long userId);

  Collection<Link> getAllByLabel(Long userId, String tagName);

  Collection<String> getAllLabels(Long userId);

  Link getByUrl(Long userId, Link url);

  Collection<Link> getLastCreated(Long userId, int limit);

  Collection<Link> getLastVisited(Long userId, int limit);

  Collection<Link> getSimilarByLink(Long userId, Link link);

  Collection<Link> getSimilarByUrl(Long userId, String url);

  void importCollection(Long userId, Collection<Link> link, boolean skipDuplicates);

  Collection<Link> last20Visits(Long userId);

  void remove(Long userId, Long id);

  Collection<Link> search(Long userId, Collection<AbstractCriteria> criteria);

  void update(Long userId, Link item);
}
