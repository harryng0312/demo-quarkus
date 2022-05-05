package org.harryng.demo.quarkus.router;

import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.vertx.core.Future;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
@RouteBase(path = "/http", produces = {MediaType.APPLICATION_JSON})
public class HttpRouter {
    static Logger logger = LoggerFactory.getLogger(HttpRouter.class);

    @Route(path = "/*", order = 1000)
    public void handleDefault(RoutingContext context) {
        logger.info("into /http handler");
        context.next();
    }

    @Route(path = "/*", type = Route.HandlerType.FAILURE, order = 1500)
    public void handleError(RoutingContext context) {
        logger.error("Error[" + context.response().getStatusCode() + "]:" + context.response().getStatusMessage());
//        context.response().end(String.join("", "{\"code\":", "\"404\"", ",\"message\":\"",
//                context.response().getStatusMessage(), "\"}")).compose(v -> Future.succeededFuture());
        context.next();
    }
}
