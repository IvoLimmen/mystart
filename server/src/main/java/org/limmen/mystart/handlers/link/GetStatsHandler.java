package org.limmen.mystart.handlers.link;

import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.limmen.mystart.Link;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.User;
import org.limmen.mystart.exception.StorageException;
import org.limmen.mystart.handlers.AbstractHandler;

public class GetStatsHandler extends AbstractHandler {

	private final LinkStorage storage;

	public GetStatsHandler(LinkStorage linkStorage) {
		this.storage = linkStorage;
	}

	@Override
	public void handle(RoutingContext event) {
		User user = event.get("user");

		String key = event.request().getParam("key").toLowerCase();

		try {
			Collection<Link> links = storage.getAll(user.getId());
			Collection<String> labels = storage.getAllLabels(user.getId());

			switch (key) {
				case "source":
					event.response().end(Json.encode(
							  links.stream().collect(Collectors.groupingBy(g -> g.getSource(), Collectors.counting()))));
					break;
				case "host":
					event.response().end(Json.encode(
							  links.stream().map((t) -> {
								  if (t.getHost() == null) {
									  t.setHost("?");
								  }
								  return t;
							  }).collect(Collectors.groupingBy(g -> g.getHost(), Collectors.counting()))));
					break;
				case "create_year":
					event.response().end(Json.encode(links.stream().collect(Collectors.groupingBy(g -> g.getCreationDate().getYear(), Collectors.counting()))));
					break;
				case "labels":
					Map<String, Long> stats = new HashMap<>();
					labels.forEach(l -> {
						stats.put(l, links.stream().filter(f -> f.getLabels().contains(l)).count());
					});
					event.response().end(Json.encode(stats));
					break;
			}
		} catch (StorageException ex) {
			throw new RuntimeException(ex);
		}
	}
}
