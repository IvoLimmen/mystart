package org.limmen.mystart.controller;

import org.limmen.mystart.User;
import org.limmen.mystart.UserStorage;
import org.limmen.mystart.dto.UserDto;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.utils.DefaultSecurityService;
import jakarta.inject.Inject;

@Controller("/api/users")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class UserController extends AbstractController {

  private UserStorage userStorage;

  @Inject
  public UserController(DefaultSecurityService defaultSecurityService, UserStorage userStorage) {
    super(defaultSecurityService);
    this.userStorage = userStorage;
  }

  private UserDto toUserDto(User user) {
    var u = new UserDto();
    u.setAutoStartLabel(user.getAutoStartLabel());
    u.setOpenInNewTab(user.isOpenInNewTab());
    u.setFullName(user.getFullName());
    u.setEmail(user.getEmail());
    return u;
  }

  @Get("/me")
  public UserDto getMyself() {
    return userStorage.get(getUserId())
        .map(this::toUserDto)
        .orElseThrow();
  }

  @Get("/{id}")
  public UserDto getUser(Long id) {
    if (getUserId().equals(id)) {
      return userStorage.get(id)
          .map(this::toUserDto)
          .orElseThrow();
    }
    return null;
  }
}
