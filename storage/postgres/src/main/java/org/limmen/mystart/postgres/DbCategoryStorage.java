package org.limmen.mystart.postgres;

import java.util.Collection;
import java.util.function.Function;

import org.limmen.mystart.Category;
import org.limmen.mystart.CategoryStorage;

public class DbCategoryStorage extends DbAbstractStorage implements CategoryStorage {

  private static final Function<Result, Category> CATEGORY_MAPPER = (var res) -> {
    var category = new Category();
    category.setId(res.lng("id"));
    category.setName(res.string("name"));
    category.setColor(res.string("color"));
    return category;
  };

  public DbCategoryStorage(String user, String password, String url) {
    super(user, password, url);
  }

  @Override
  public void delete(Long userId, Long id) {
    executeSql("delete from category where id = ? and user_id = ?", stmt -> {
      stmt.setLong(1, id);
      stmt.setLong(2, userId);
    });    
  }

  @Override
  public void create(Long userId, Category category) {
    String sql = "insert into category (user_id, name, color) "
        + "values (?, ?, ?)";
    executeSql(sql, stmt -> {
      stmt.setLong(1, userId);
      stmt.setString(2, category.getName());
      stmt.setString(3, category.getColor());
    });
  }

  @Override
  public Category get(Long userId, Long id) {
    return executeSqlSingle("select * from category where user_id = ? and id = ?", args -> {
      args.setLong(1, userId);
      args.setLong(2, id);
    }, CATEGORY_MAPPER);
  }

  @Override
  public Collection<Category> getAll(Long userId) {
    return executeSql("select * from category where user_id = ? order by name asc", args -> {
      args.setLong(1, userId);
    }, CATEGORY_MAPPER);
  }

  @Override
  public void update(Long userId, Category category) {
    String sql = "update category "
        + "set name = ?, "
        + "color = ? "
        + "where id = ? "
        + "and user_id = ?";
    executeSql(sql, stmt -> {
      stmt.setString(1, category.getName());
      stmt.setString(2, category.getColor());

      stmt.setLong(3, category.getId());
      stmt.setLong(4, userId);
    });
  }
}
