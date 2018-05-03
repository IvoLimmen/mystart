package org.limmen.mystart.handlers;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractHandler implements Handler<RoutingContext> {

	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractHandler.class);

}
