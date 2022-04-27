package org.harryng.demo.quarkus.router;

import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.vertx.core.Future;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@RouteBase(path = "/ws")
public class WsRouter {
    static Logger logger = LoggerFactory.getLogger(WsRouter.class);
    @Route(path = "/*")
    public void handleDefault(RoutingContext context) {
        logger.info("into /ws handler");
        context.next();
    }

    @Route(path = "/*", type = Route.HandlerType.FAILURE)
    public void handleError(RoutingContext context){
        logger.error("Error[" + context.response().getStatusCode()+"]:" + context.response().getStatusMessage());
        context.response().end(context.response().getStatusMessage()).compose(v-> Future.succeededFuture());
    }
}
