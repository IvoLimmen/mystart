package org.limmen.mystart.postgres;

import java.util.Collection;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.limmen.mystart.User;
import org.limmen.mystart.UserStorage;

@Slf4j
public class DbUserStorage extends DbAbstractStorage implements UserStorage {

  private static final Function<Result, User> USER_MAPPER = res -> {
    User u = new User();
    u.setId(res.lng("id"));
    u.setFullName(res.string("full_name"));
    u.setEmail(res.string("email"));
    u.setName(res.string("name"));    
    u.setAvatarFileName(res.string("avatar_filename"));
    u.setOpenInNewTab(res.bool("open_in_new_tab"));
    u.setPassword(res.string("password"));
    return u;
  };

  public DbUserStorage(String user, String password, String url) {
    super(user, password, url);
  }

  @Override
  public User get(Long id) {
    return executeSqlSingle("select * from users where id = ?", stmt -> {
                          stmt.setLong(1, id);
                        }, USER_MAPPER);
  }

  @Override
  public Collection<User> getAll() {
    return null;
  }

  @Override
  public User getByEmail(String email) {
    return executeSqlSingle("select * from users where email = ?", stmt -> {
                          stmt.setString(1, email);
                        }, USER_MAPPER);
  }

  @Override
  public User getByNameOrEmail(String nameOrEmail) {
    return executeSqlSingle("select * from users where email = ? or name = ?", stmt -> {
                          stmt.setString(1, nameOrEmail);
                          stmt.setString(2, nameOrEmail);
                        }, USER_MAPPER);
  }

  @Override
  public void remove(Long id) {
    executeSql("delete from users where id = ?", stmt -> {
             stmt.setLong(1, id);
           });
  }

  @Override
  public void store(User item) {
    if (item.getId() == null) {
      executeSql("insert into users (name, email, password, full_name, avatar_file_name, open_in_new_tab) values (?, ?, ?, ?, ?, ?)", stmt -> {
               stmt.setString(1, item.getName());
               stmt.setString(2, item.getEmail());
               stmt.setString(3, item.getPassword());
               stmt.setString(4, item.getFullName());
               stmt.setString(5, item.getAvatarFileName());
               stmt.setBool(6, item.isOpenInNewTab());
             });

    } else {
      executeSql("update users set name = ?, email = ?, password = ?, full_name = ?, avatar_file_name = ?, open_in_new_tab = ? where id = ?", stmt -> {
               stmt.setString(1, item.getName());
               stmt.setString(2, item.getEmail());
               stmt.setString(3, item.getPassword());
               stmt.setString(4, item.getFullName());
               stmt.setString(5, item.getAvatarFileName());
               stmt.setBool(6, item.isOpenInNewTab());
               stmt.setLong(7, item.getId());
             });
    }
  }
}
