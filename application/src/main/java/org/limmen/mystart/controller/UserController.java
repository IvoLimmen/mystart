package org.limmen.mystart.controller;

import org.limmen.mystart.User;
import org.limmen.mystart.UserStorage;

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

    @Get("/me")
    public User getMyself() {
        return userStorage.get(getUserId()).orElse(null);
    }

    @Get("/{id}")
    public User getUser(Long id) {
        if (getUserId().equals(id)) {
            return userStorage.get(id).orElse(null);
        }
        return null;
    }
}
