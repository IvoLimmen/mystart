package org.limmen.mystart.handlers.link;

import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;
import org.limmen.mystart.Link;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.User;
import org.limmen.mystart.exception.StorageException;
import org.limmen.mystart.handlers.AbstractHandler;

public class DeleteLinkHandler extends AbstractHandler {

	private final LinkStorage storage;

	public DeleteLinkHandler(LinkStorage linkStorage) {
		this.storage = linkStorage;
	}

	@Override
	public void handle(RoutingContext event) {
		User user = event.get("user");
		MultiMap form = event.request().formAttributes();
		String url = form.get("url");
		if (url != null) {
			try {
				this.storage.remove(user.getId(), new Link(url).getId());
			} catch (StorageException ex) {
				throw new RuntimeException(ex);
			}
			event.response().setStatusCode(200).end();
			return;
		}
		event.response().setStatusCode(404).end();
	}
}
