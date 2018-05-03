package org.limmen.mystart.cleanup;

import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.User;
import org.limmen.mystart.UserStorage;
import org.limmen.mystart.handlers.AbstractHandler;

public class CleanupLinksHandler extends AbstractHandler {

	private final LinkStorage linkStorage;

	private final UserStorage userStorage;

	private final ExecutorService executorService = Executors.newScheduledThreadPool(5);

	public CleanupLinksHandler(final LinkStorage linkStorage, final UserStorage userStorage) {
		this.linkStorage = linkStorage;
		this.userStorage = userStorage;
	}

	@Override
	public void handle(RoutingContext event) {
		MultiMap form = event.request().formAttributes();

		User user = event.get("user");

		String url = form.get("url");
		String markPn = form.get("markAsPrivateNetworkOnDomainError");

		CleanupContext cleanupContext = new CleanupContext();
		cleanupContext.setUserId(user.getId());
		cleanupContext.setMarkAsPrivateNetworkOnDomainError("true".equals(markPn));
		cleanupContext.setUrl(url);
		executorService.submit(new CleanupTask(userStorage, linkStorage, cleanupContext));
		
		event.response().setStatusCode(200).end();
	}
}
