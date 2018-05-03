package org.limmen.mystart.handlers.user;

import io.vertx.ext.web.RoutingContext;
import org.limmen.mystart.UserStorage;
import org.limmen.mystart.exception.StorageException;
import org.limmen.mystart.handlers.AbstractHandler;

public class DeleteUserHandler extends AbstractHandler {

   private final UserStorage userStorage;

   public DeleteUserHandler(UserStorage userStorage) {
      this.userStorage = userStorage;
   }

	@Override
	public void handle(RoutingContext event) {
      String id = event.request().formAttributes().get("id");
      if (id != null) {
			try {
				this.userStorage.remove(Long.parseLong(id));
			} catch (StorageException ex) {
				throw new RuntimeException(ex);
			}
         event.response().setStatusCode(200).end();
      }
   }
}
