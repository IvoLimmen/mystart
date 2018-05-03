package org.limmen.mystart;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.limmen.mystart.cleanup.CleanupLinksHandler;
import org.limmen.mystart.handlers.user.GetUserHandler;
import org.limmen.mystart.handlers.user.CreateUserHandler;
import org.limmen.mystart.handlers.user.DeleteUserHandler;
import org.limmen.mystart.handlers.UserAuthorizationHandler;
import org.limmen.mystart.handlers.link.GetStatsHandler;
import org.limmen.mystart.handlers.link.SearchLinkHandler;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import java.text.SimpleDateFormat;
import org.limmen.mystart.handlers.LoginHandler;
import org.limmen.mystart.handlers.link.BookmarkFileImportHandler;
import org.limmen.mystart.handlers.link.CreateLinkHandler;
import org.limmen.mystart.handlers.link.DeleteLinkHandler;
import org.limmen.mystart.handlers.link.GetLinksHandler;
import org.limmen.mystart.handlers.link.VisitLinkHandler;

public class MyStart extends AbstractVerticle {

	@Override
	public void start() throws Exception {

		Config conf = ConfigFactory.load();

		Storage storage = StorageProvider.getStorageByName(conf, conf.getString("server.storage"));
		UserStorage userStorage = storage.getUserStorage();
		LinkStorage linkStorage = storage.getLinkStorage();

		Parser parser = new AutoDetectParser();
	
		Json.mapper.registerModule(new JavaTimeModule());
		Json.mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

		Json.prettyMapper.registerModule(new JavaTimeModule());
		Json.prettyMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		
		
		Router router = Router.router(vertx);
	
		router.route().handler(BodyHandler.create().setMergeFormAttributes(true));
		router.route().handler(new UserAuthorizationHandler(userStorage));

		router.route(HttpMethod.GET, "/login").handler(new LoginHandler(userStorage));

		router.route(HttpMethod.POST, "/user").handler(new CreateUserHandler(userStorage));
		router.route(HttpMethod.GET, "/user").handler(new GetUserHandler(userStorage));
		router.route(HttpMethod.DELETE, "/user").handler(new DeleteUserHandler(userStorage));

		router.route(HttpMethod.POST, "/link").handler(new CreateLinkHandler(linkStorage));
		router.route(HttpMethod.GET, "/link").handler(new GetLinksHandler(linkStorage));
		router.route(HttpMethod.DELETE, "/link").handler(new DeleteLinkHandler(linkStorage));

		router.route(HttpMethod.GET, "/link/search").handler(new SearchLinkHandler(linkStorage));
		router.route(HttpMethod.POST, "/link/import").handler(new BookmarkFileImportHandler(linkStorage, parser));
		router.route(HttpMethod.POST, "/link/visit").handler(new VisitLinkHandler(linkStorage));
		router.route(HttpMethod.POST, "/link/cleanup").handler(new CleanupLinksHandler(linkStorage, userStorage));
		router.route(HttpMethod.GET, "/link/stats/:key").handler(new GetStatsHandler(linkStorage));

		HttpServerOptions options = new HttpServerOptions();
		options.setReceiveBufferSize(31457280); // 30MB
		HttpServer server = vertx.createHttpServer(options);		
		server.requestHandler(router::accept).listen(conf.getInt("server.port"));
	}
}
