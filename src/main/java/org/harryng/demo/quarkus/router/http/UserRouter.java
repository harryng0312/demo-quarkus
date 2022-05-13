package org.harryng.demo.quarkus.router.http;

import io.quarkus.vertx.web.*;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mutiny.core.http.HttpServerResponse;
import org.harryng.demo.quarkus.base.controller.AbstractController;
import org.harryng.demo.quarkus.base.service.BaseService;
import org.harryng.demo.quarkus.user.entity.UserImpl;
import org.harryng.demo.quarkus.user.service.UserService;
import org.harryng.demo.quarkus.util.ReactiveUtil;
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

    protected Uni<UserImpl> getUserById(RoutingExchange exc, HttpServerResponse response, String id) {
//        return sessionFactory.withStatelessTransaction(Unchecked.function((session, trans) ->
//                        userService.getById(SessionHolder.createAnonymousSession(),
//                                Long.parseLong(id),
//                                Map.of(BaseService.TRANS_STATELESS_SESSION, session,
//                                        BaseService.TRANSACTION, trans
//                                )
//                        ))
//                )
//                // write response
//                .invoke(Unchecked.consumer(user -> {
//                    if (user == null) throw new RuntimeException("user is not found");
//                }))
//                .onFailure().invoke(ex -> response.setStatusCode(404).end(
//                        String.join("", "{\"code\":", "\"404\"", ",\"message\":\"",
//                                ex.getMessage(), "\"}")
//                ));
        return sessionFactory.withTransaction(Unchecked.function((session, trans) ->
                        userService.getById(SessionHolder.createAnonymousSession(),
                                Long.parseLong(id),
                                Map.of(BaseService.TRANS_SESSION, session,
                                        BaseService.TRANSACTION, trans
                                )
                        ))
                )
                // write response
                .invoke(Unchecked.consumer(user -> {
                    if (user == null) throw new RuntimeException("user is not found");
                }))
                .onFailure().invoke(ex -> response.setStatusCode(404).end(
                        String.join("", "{\"code\":", "\"404\"", ",\"message\":\"",
                                ex.getMessage(), "\"}")
                ));
    }

    protected void editUser(RoutingContext ctx, Buffer buffer) {
        sessionFactory.withStatelessTransaction(Unchecked.function(
                (sess, trans) -> userService.edit(SessionHolder.createAnonymousSession(),
                        getObjectMapper().readValue(buffer.toString(), UserImpl.class),
                        Map.of(BaseService.TRANS_STATELESS_SESSION, sess,
                                BaseService.TRANSACTION, trans,
                                BaseService.HTTP_HEADERS, ctx.request().headers(),
                                BaseService.HTTP_COOKIES, ctx.request().cookies())
                ))).subscribe().with(itm -> {
                    var jsonRs = new JsonObject();
                    jsonRs.put("result", itm);
                    ctx.response().end(jsonRs.toString());
                },
                ex -> {
                    logger.error("", ex);
                    ctx.response().end(ex.getCause().getMessage());
                });
    }

    @Route(path = "/:id", methods = Route.HttpMethod.GET, order = 500)
    public Uni<UserImpl> getUserByIdNonBlocking(RoutingExchange exc, HttpServerResponse response, @Param("id") String id) {
        logger.info("into /http/user/:id get");
//        getUserById(exc, response, id).subscribe().with(ReactiveUtil.defaultSuccessConsumer());
        return getUserById(exc, response, id);
    }

    @Route(path = "/:id/blocking", methods = Route.HttpMethod.GET, type = Route.HandlerType.BLOCKING, order = 200)
    @Blocking
    public UserImpl getUserByIdBlocking(RoutingExchange exc, HttpServerResponse response, @Param("id") String id) {
        logger.info("into /http/user/:id/blocking get");
//        return getUserById(exc, response, id).await().indefinitely();
        return getVertx().executeBlockingAndAwait(getUserById(exc, response, id));
    }

    @Route(path = "/*", methods = Route.HttpMethod.POST, order = 500)
    public void addUserNonBlocking(RoutingContext ctx, @Body Buffer buffer) {
        logger.info("into /http/user post");
        ctx.next();
    }

    @Route(path = "/*", methods = Route.HttpMethod.PUT, order = 500)
    public void editUserNonBlocking(RoutingContext ctx, @Body Buffer buffer) {
        logger.info("into /http/user put");
        editUser(ctx, buffer);
    }

    @Route(path = "/blocking/*", methods = Route.HttpMethod.PUT, type = Route.HandlerType.BLOCKING, order = 200)
    public void editUserBlocking(RoutingContext ctx, @Body Buffer buffer) {
        logger.info("into /http/user/nonblocking put");
        editUser(ctx, buffer);
    }

    @Route(path = "/:id", methods = Route.HttpMethod.DELETE, order = 500)
    public void removeUser(RoutingContext ctx, @Param("id") String id) {
        logger.info("into /http/user delete");
    }
}
