package org.limmen.mystart.handlers.link;

import io.vertx.ext.web.RoutingContext;
import org.limmen.mystart.Link;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.User;
import org.limmen.mystart.exception.StorageException;
import org.limmen.mystart.handlers.AbstractHandler;

public class VisitLinkHandler extends AbstractHandler {

   private final LinkStorage storage;

   public VisitLinkHandler(LinkStorage linkStorage) {
      this.storage = linkStorage;
   }

	@Override
	public void handle(RoutingContext event) {

		User user = event.get("user");
      String url = event.request().formAttributes().get("url");
      if (url != null) {
			try {
				Link link = this.storage.get(user.getId(), new Link(url).getId());
				link.visited();
				this.storage.store(user.getId(), link);
			} catch (StorageException ex) {
				throw new RuntimeException(ex);
			}
         event.response().setStatusCode(200).end();
      }
   }
}
