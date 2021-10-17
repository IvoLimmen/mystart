package org.limmen.mystart.config;

import java.util.Map;
import java.util.Optional;

import org.limmen.mystart.User;
import org.limmen.mystart.UserStorage;
import org.reactivestreams.Publisher;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider {

    private final SettingsProvider settingsConfig;
    private final UserStorage userStorage;
    private final String serverSalt;

    @Inject
    public AuthenticationProviderUserPassword(UserStorage userStorage, SettingsProvider settingsConfig) {
        this.userStorage = userStorage;
        this.settingsConfig = settingsConfig;
        this.serverSalt = this.settingsConfig.getProperties().getProperty("server.salt");
    }

    @Override
    public Publisher<AuthenticationResponse> authenticate(HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        return Flux.create(emitter -> {
            String email = authenticationRequest.getIdentity().toString();
            String password = authenticationRequest.getSecret().toString();
            Optional<User> user = this.userStorage.getByEmail(email);
            if (user.isPresent() && user.get().check(this.serverSalt, password)) {
                emitter.next(AuthenticationResponse.success((String) authenticationRequest.getIdentity(), Map.of("id", user.get().getId())));
                emitter.complete();
            } else {
                emitter.error(AuthenticationResponse.exception());
            }
        }, FluxSink.OverflowStrategy.ERROR);        
    }
}