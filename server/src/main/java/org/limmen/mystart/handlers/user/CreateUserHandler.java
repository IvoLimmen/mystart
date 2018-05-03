package org.limmen.mystart.handlers.user;

import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;
import org.limmen.mystart.User;
import org.limmen.mystart.UserStorage;
import org.limmen.mystart.exception.StorageException;
import org.limmen.mystart.handlers.AbstractHandler;

public class CreateUserHandler extends AbstractHandler {
	
	private final UserStorage userStorage;

	public CreateUserHandler(final UserStorage userStorage) {
		this.userStorage = userStorage;
	}

	@Override
	public void handle(RoutingContext event) {
		MultiMap form = event.request().formAttributes();
		String email = form.get("email");
		String password = form.get("password");

		LOGGER.info("User: {}, {}", email, password);
		
		User user = new User(email, password);

		try {
			this.userStorage.store(user);
		} catch (StorageException ex) {
			throw new RuntimeException(ex);
		}

		event.response().setStatusCode(201).end();
	}
}
