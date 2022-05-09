package org.harryng.demo.quarkus.filter;

import io.quarkus.vertx.web.RouteFilter;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HttpFilter {
    static Logger logger = LoggerFactory.getLogger(HttpFilter.class);

//    @Inject
//    protected CurrentVertxRequest currentVertxRequest;

//    @RouteFilter(100)
//    public void allFilter(RoutingContext rc) {
//        logger.info("all filter");
//        rc.response().putHeader("X-Header", "intercepting the request");
//        rc.next();
//    }

    @RouteFilter(100)
    public void authFilter(RoutingContext context){
        logger.info("auth filter");
//        currentVertxRequest.setCurrent(rc);
        var tokenCookie = context.request().getCookie("token");
        if(tokenCookie != null){
            var token = tokenCookie.getName();
        }
        context.next();
    }
}
