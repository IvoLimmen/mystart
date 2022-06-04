package org.limmen.mystart.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.limmen.mystart.User;
import org.limmen.mystart.UserStorage;
import org.limmen.mystart.config.SettingsProvider;
import org.limmen.mystart.dto.UserDto;
import org.limmen.mystart.support.MailService;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.utils.DefaultSecurityService;

@Controller("/api/registration")
public class RegistrationController extends AbstractController {

  private String salt;
  private UserStorage userStorage;
  private MailService mailService;

  public RegistrationController(DefaultSecurityService defaultSecurityService, UserStorage userStorage,
      SettingsProvider settingsProvider, MailService mailService) {
    super(defaultSecurityService);
    this.userStorage = userStorage;
    this.mailService = mailService;
    this.salt = settingsProvider.string("server.salt");
  }

  private URI createUri(HttpRequest<?> request, String code) {
    UriBuilder builder;
    if (request.getServerName().equals("localhost")) {
      builder = UriBuilder.of(URI.create("http://localhost:8080"));
    } else {
      builder = UriBuilder.of(URI.create("https://" + request.getServerName()));
    }
    return builder
        .path("/api/registration/" + code)
        .build();
  }

  @Post("/{code}")
  public HttpResponse<?> verify(@PathVariable String code, @Body UserDto dto) {
    Optional<User> user = this.userStorage.getByResetCode(code);

    if (user.isEmpty()) {
      return HttpResponse.notAllowed();
    }

    var u = user.get();
    if (u.getResetCodeValid().isBefore(LocalDateTime.now())) {
      return HttpResponse.notAllowed();
    }

    u.updatePassword(this.salt, dto.getPassword());
    u.setResetCode(null);
    u.setResetCodeValid(null);
    this.userStorage.store(u);

    return HttpResponse.accepted();
  }

  @Get("/{email}")
  public HttpResponse<?> forgot(@PathVariable String email, HttpRequest<?> request) {

    Optional<User> user = this.userStorage.getByEmail(email);

    if (user.isEmpty()) {
      return HttpResponse.noContent();
    }

    var u = user.get();
    u.setResetCode("r" + UUID.randomUUID().toString());
    u.setResetCodeValid(LocalDateTime.now().plusHours(4));
    this.userStorage.store(u);

    this.mailService.sendPasswordReset(email, u.getFullName(), createUri(request, u.getResetCode()));

    return HttpResponse.noContent();
  }

  @Post("/")
  public HttpResponse<?> createUser(@Body UserDto dto, HttpRequest<?> request) {

    if (this.userStorage.getByEmail(dto.getEmail()).isPresent()) {
      return HttpResponse.notModified();
    }

    var user = new User();
    user.setEmail(dto.getEmail());
    user.setResetCode("c" + UUID.randomUUID().toString());
    user.setResetCodeValid(LocalDateTime.now().plusHours(4));

    this.mailService.sendValidateEmail(user.getEmail(), user.getFullName(), createUri(request, user.getResetCode()));
    this.userStorage.store(user);

    return HttpResponse.noContent();
  }
}
