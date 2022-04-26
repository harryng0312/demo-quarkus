package org.harryng.demo.quarkus.router.http;

import io.quarkus.vertx.web.Param;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.quarkus.vertx.web.RoutingExchange;
import io.smallrye.mutiny.unchecked.Unchecked;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import org.harryng.demo.quarkus.base.controller.AbstractController;
import org.harryng.demo.quarkus.base.service.BaseService;
import org.harryng.demo.quarkus.user.service.UserService;
import org.harryng.demo.quarkus.util.SessionHolder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@ApplicationScoped
@RouteBase(path = "/http/user", produces = {MediaType.APPLICATION_JSON})
public class UserRouter extends AbstractController {
    static Logger logger = LoggerFactory.getLogger(UserRouter.class);
    @Inject
    protected UserService userService;

    @Route(path = ":id", methods = Route.HttpMethod.GET)
    public void getUserById(RoutingExchange exc, @Param("id") String id) {
        logger.info("user id:" + id);
        // get user by id
        sessionFactory.withStatelessTransaction(Unchecked.function((session, trans) ->
                        userService.getById(SessionHolder.createAnonymousSession(),
                                Long.parseLong(id),
                                Map.of(BaseService.TRANS_STATELESS_SESSION, session,
                                        BaseService.TRANSACTION, trans
                                )
                        ))
                )
                // write response
                .invoke(Unchecked.consumer(user -> {
                    if (user == null) throw new RuntimeException("user is not found");
                }))
                .subscribe().with(Unchecked.consumer(user -> exc.response().setChunked(true)
                        .write(Buffer.buffer(getObjectMapper().writeValueAsString(user)))
                        .eventually(v -> exc.response().end())
                ), ex -> exc.response().setStatusCode(404).end(
                        String.join("", "{\"code\":", "\"404\"", ",\"message\":\"", ex.getMessage(), "\"}")
                ));
    }

    @Route(path = "/", methods = Route.HttpMethod.POST)
    public void addUser(RoutingContext ctx) {

    }

    @Route(path = "/", methods = Route.HttpMethod.PUT)
    public void editUser(RoutingContext ctx) {

    }

    @Route(path = "/:id", methods = Route.HttpMethod.DELETE)
    public void removeUser(RoutingContext ctx, @Param("id") String id) {

    }
}
