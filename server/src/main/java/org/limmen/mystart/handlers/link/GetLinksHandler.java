package org.limmen.mystart.handlers.link;

import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import java.util.ArrayList;
import java.util.List;
import org.limmen.mystart.Link;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.User;
import org.limmen.mystart.exception.StorageException;
import org.limmen.mystart.handlers.AbstractHandler;

public class GetLinksHandler extends AbstractHandler {

	private final LinkStorage storage;

	public GetLinksHandler(LinkStorage storage) {
		this.storage = storage;
	}

	@Override
	public void handle(RoutingContext event) {
		User user = event.get("user");
		String label = event.request().formAttributes().get("label");
		if (label == null || label.equals("all")) {
			try {
				event.response().end(Json.encode(this.storage.getAll(user.getId())));
			} catch (StorageException ex) {
				throw new RuntimeException(ex);
			}
		} else {
			String[] labels;
			if (label.contains(",")) {
				labels = label.split(",");
			} else if (label.contains(";")) {
				labels = label.split(";");
			} else if (label.contains(" ")) {
				labels = label.split(" ");
			} else {
				labels = new String[1];
				labels[0] = label;
			}

			List<Link> links = new ArrayList<>();
			for (String lbl : labels) {
				try {
					links.addAll(this.storage.getAllByLabel(user.getId(), lbl));
				} catch (StorageException ex) {
					throw new RuntimeException(ex);
				}
			}
			event.response().end(Json.encode(links));
		}
	}
}
