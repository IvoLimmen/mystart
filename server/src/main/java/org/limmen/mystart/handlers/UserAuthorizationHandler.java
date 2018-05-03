package org.limmen.mystart.handlers;

import io.vertx.ext.web.RoutingContext;
import org.limmen.mystart.User;
import org.limmen.mystart.UserStorage;
import org.limmen.mystart.exception.StorageException;

public class UserAuthorizationHandler extends AbstractHandler {

	private final UserStorage userStorage;

	public UserAuthorizationHandler(final UserStorage userStorage) {
		this.userStorage = userStorage;
	}

	@Override
	public void handle(RoutingContext event) {

		if (!event.request().path().startsWith("user")) {

			String email = null;
			String password = null;

			String auth = event.request().headers().get("Auth");
			if (auth != null) {
				email = auth.split(":")[0];
				password = auth.split(":")[1];
			}

			User user = null;
			if (email != null && password != null) {
				try {
					user = userStorage.getByEmail(email);
				} catch (StorageException ex) {
					LOGGER.info("Failed to find user in storage");
				}
				if (user != null && !user.getPassword().equals(password)) {
					user = null;
				}
			}

			if (user == null) {
				LOGGER.debug("No user. 401.");
				event.response().setStatusCode(401).setStatusMessage("Not authorized").end();
			} else {
				LOGGER.debug("Continueing to next handler...");
				event.put("user", user);
				event.next();
			}
		} else {
			LOGGER.debug("Continueing to next handler...");
			event.next();
		}
	}

}
