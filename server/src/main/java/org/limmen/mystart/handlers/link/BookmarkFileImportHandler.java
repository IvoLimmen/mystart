package org.limmen.mystart.handlers.link;

import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.limmen.mystart.Link;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.ParseContext;
import org.limmen.mystart.Parser;
import org.limmen.mystart.User;
import org.limmen.mystart.exception.StorageException;
import org.limmen.mystart.handlers.AbstractHandler;

public class BookmarkFileImportHandler extends AbstractHandler {

	private final ExecutorService background = Executors.newFixedThreadPool(10);

	private final LinkStorage storage;

	private final Parser parser;

	public BookmarkFileImportHandler(LinkStorage storage, Parser parser) {
		this.storage = storage;
		this.parser = parser;
	}

	@Override
	public void handle(RoutingContext event) {

		MultiMap form = event.request().formAttributes();
		Set<FileUpload> files = event.fileUploads();

		FileUpload file = null;
		String url = form.get("url");
		User user = event.get("user");

		if (files.size() > 0) {
			file = files.iterator().next();
		}
		
		ParseContext parseContext = null;

		if (url != null) {
			parseContext = new ParseContext(url);
		} else if (file != null) {
			Buffer uploadedFile = event.vertx().fileSystem().readFileBlocking(file.uploadedFileName());
			ByteArrayInputStream inputStream = new ByteArrayInputStream(uploadedFile.getBytes());
			parseContext = new ParseContext(inputStream, file.uploadedFileName(), file.fileName());
		}

		if (parseContext != null) {

			try {

				List<Link> links = parser.parse(parseContext);
				LOGGER.info("Parsed {} links from {}", links.size(), parser.getName());

				background.submit(() -> {
					try {
						this.storage.storeCollection(user.getId(), links);
					} catch (StorageException ex) {
						throw new RuntimeException(ex);
					}
					return null;
				});

				event.response().setStatusCode(201).end();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		} else {
			LOGGER.warn("Unparsable file");
			event.response().setStatusCode(404).end();
		}
	}
}
