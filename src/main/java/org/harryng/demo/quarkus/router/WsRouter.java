package org.harryng.demo.quarkus.router;

import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
@RouteBase(path = "/http", produces = {MediaType.APPLICATION_JSON}, consumes = {MediaType.APPLICATION_JSON})
public class WsRouter {
    static Logger logger = LoggerFactory.getLogger(WsRouter.class);

    @Route(path = "/")
    public void getWs(RoutingContext context) {
        logger.info("into /ws handler");
        context.next();
    }
}
