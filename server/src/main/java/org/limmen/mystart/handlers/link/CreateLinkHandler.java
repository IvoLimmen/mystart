package org.limmen.mystart.handlers.link;

import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import org.limmen.mystart.Link;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.User;
import org.limmen.mystart.exception.StorageException;
import org.limmen.mystart.handlers.AbstractHandler;

public class CreateLinkHandler extends AbstractHandler {

	private final LinkStorage storage;

	public CreateLinkHandler(LinkStorage storage) {
		this.storage = storage;
	}

	@Override
	public void handle(RoutingContext event) {

		User user = event.get("user");

		MultiMap form = event.request().formAttributes();
		String label = form.get("labels");
		String[] labels;
		if (label == null) {
			labels = new String[1];
			labels[0] = "Unknown";
		} else if (label.contains(",")) {
			labels = label.split(",");
		} else if (label.contains(";")) {
			labels = label.split(";");
		} else if (label.contains(" ")) {
			labels = label.split(" ");
		} else {
			labels = new String[1];
			labels[0] = label;
		}

		for (int i = 0; i < labels.length; i++) {
			labels[i] = labels[i].trim();
		}

		String url = form.get("url");
		String title = form.get("title");
		String description = form.get("description");
		String lastVisit = form.get("lastVisit");
		boolean privateNetwork = "true".equalsIgnoreCase(form.get("privateNetwork"));

		Link link = new Link(url);
		link.setTitle(title);
		link.setDescription(description);
		link.setLabels(Arrays.asList(labels));
		link.setSource("MyStart");
		link.setPrivateNetwork(privateNetwork);
		if (lastVisit != null) {
			try {
				link.setLastVisit(LocalDateTime.parse(lastVisit, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
			} catch (DateTimeParseException dtpe) {
				// safely ignore
			}
		}

		try {
			this.storage.store(user.getId(), link);
		} catch (StorageException ex) {
			throw new RuntimeException(ex);
		}

		event.response().setStatusCode(201).end();
	}
}
