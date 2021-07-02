package org.limmen.mystart.config;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.limmen.mystart.User;
import org.limmen.mystart.UserStorage;
import org.reactivestreams.Publisher;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationException;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UserDetails;
import io.reactivex.Maybe;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider {

    private SettingsConfig settingsConfig;
    private UserStorage userStorage;

    @Inject
    public AuthenticationProviderUserPassword(UserStorage userStorage, SettingsConfig settingsConfig) {
        this.userStorage = userStorage;
        this.settingsConfig = settingsConfig;
    }

    @Override
    public Publisher<AuthenticationResponse> authenticate(HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        return Maybe.<AuthenticationResponse>create(emitter -> {
            String email = authenticationRequest.getIdentity().toString();
            String password = authenticationRequest.getSecret().toString();
            Optional<User> user = this.userStorage.getByEmail(email);
            
            if (user.isPresent() && user.get().check(this.settingsConfig.getProperties().getProperty("server.salt"), password)) {
                UserDetails details = new UserDetails(email, Collections.emptyList());
                details.setAttributes(Map.of("id", user.get().getId()));
                emitter.onSuccess(details);
            } else {
                emitter.onError(new AuthenticationException(new AuthenticationFailed()));
            }
        }).toFlowable();
    }
}