package org.limmen.mystart;

import java.util.Collection;

public interface CategoryStorage {
  
  void delete(Long userId, Long id);

  void create(Long userId, Category category);

  Category get(Long userId, Long id);

  Collection<Category> getAll(Long userId);  

  void update(Long userId, Category link);
}
