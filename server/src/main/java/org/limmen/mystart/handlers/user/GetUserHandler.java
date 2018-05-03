package org.limmen.mystart.handlers.user;

import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.limmen.mystart.User;
import org.limmen.mystart.UserStorage;
import org.limmen.mystart.exception.StorageException;
import org.limmen.mystart.handlers.AbstractHandler;

public class GetUserHandler extends AbstractHandler {

	private final UserStorage userStorage;

	public GetUserHandler(final UserStorage userStorage) {
		this.userStorage = userStorage;
	}

	@Override
	public void handle(RoutingContext event) {
		String email = event.request().formAttributes().get("email");
		if (email == null) {
			try {
				event.response().end(Json.encode(this.userStorage.getAll()));
			} catch (StorageException ex) {
				throw new RuntimeException(ex);
			}
		} else {
			try {
				event.response().end(Json.encode(this.userStorage.get(new User(email, null).getId())));
			} catch (StorageException ex) {
				throw new RuntimeException(ex);
			}
		}
	}
}
