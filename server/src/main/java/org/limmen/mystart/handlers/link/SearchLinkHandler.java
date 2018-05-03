package org.limmen.mystart.handlers.link;

import io.vertx.core.MultiMap;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.limmen.mystart.Link;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.User;
import org.limmen.mystart.exception.StorageException;
import org.limmen.mystart.handlers.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchLinkHandler extends AbstractHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchLinkHandler.class);

	private final LinkStorage storage;

	public SearchLinkHandler(LinkStorage linkStorage) {
		this.storage = linkStorage;
	}

	@Override
	public void handle(RoutingContext event) {

		try {
			MultiMap form = event.request().formAttributes();
			User user = event.get("user");

			String description = form.get("description");
			String title = form.get("title");
			String label = form.get("label");
			String host = form.get("host");
			String source = form.get("source");
			String keyword = form.get("keyword");

			LOGGER.info("Retrieving all links...");
			Stream<Link> stream = storage.getAll(user.getId()).stream();

			if (source != null && source.length() > 0) {
				LOGGER.info("Filter on source: {}", source);
				stream = stream.filter(p -> p.getSource().toLowerCase().contains(source.toLowerCase()));
			}
			if (title != null && title.length() > 0) {
				LOGGER.info("Filter on title: {}", title);
				stream = stream.filter(p -> p.getTitle().toLowerCase().contains(title.toLowerCase()));
			}
			if (description != null && description.length() > 0) {
				LOGGER.info("Filter on description: {}", description);
				stream = stream.filter(p -> p.getDescription().toLowerCase().contains(description.toLowerCase()));
			}
			if (keyword != null && keyword.length() > 0) {
				LOGGER.info("Filter on keyword: {}", keyword);
				stream = stream.filter(p -> p.hasKeyword(keyword.toLowerCase()));
			}
			if (label != null && label.length() > 0) {
				LOGGER.info("Filter on label: {}", label);
				stream = stream.filter(p -> p.hasKeywordInLabel(label.toLowerCase()));
			}
			if (host != null && host.length() > 0) {
				LOGGER.info("Filter on host: {}", host);
				stream = stream.filter(p -> p.getHost().toLowerCase().contains(host.toLowerCase()));
			}

			List<Link> data = stream.collect(Collectors.toList());
			LOGGER.info("Total data after filtering: {}", data.size());
			event.response().end(Json.encode(data));
		} catch (StorageException ex) {
			throw new RuntimeException(ex);
		}
	}
}
