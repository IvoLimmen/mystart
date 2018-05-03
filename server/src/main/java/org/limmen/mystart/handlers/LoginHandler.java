package org.limmen.mystart.handlers;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.limmen.mystart.UserStorage;

public class LoginHandler extends AbstractHandler {

   private final UserStorage userStorage;

   public LoginHandler(UserStorage userStorage) {
      this.userStorage = userStorage;
   }

	@Override
	public void handle(RoutingContext event) {
      event.response().setStatusCode(200).end();
	}
}
