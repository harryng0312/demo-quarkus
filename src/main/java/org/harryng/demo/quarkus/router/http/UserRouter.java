package org.harryng.demo.quarkus.router.http;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.harryng.demo.quarkus.user.entity.UserImpl;
import org.harryng.demo.quarkus.user.service.UserService;
import org.harryng.demo.quarkus.util.SessionHolder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.HashMap;

@ApplicationScoped
@RouteBase(path = "/http/user", produces = {MediaType.APPLICATION_JSON})
public class UserRouter extends AbstractController {
    static Logger logger = LoggerFactory.getLogger(UserRouter.class);
    @Inject
    protected UserService userService;

    protected Uni<UserImpl> getUserById(RoutingExchange exc, HttpServerResponse response, String id) throws Exception {
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
//        return sessionFactory.withTransaction(Unchecked.function((session, trans) ->
//                        userService.getById(SessionHolder.createAnonymousSession(),
//                                Long.parseLong(id),
//                                Map.of(BaseService.TRANS_SESSION, session,
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
        return userService.getById(SessionHolder.createAnonymousSession(), Long.parseLong(id), new HashMap<>());
    }

    protected Uni<Integer> editUser(RoutingContext ctx, Buffer buffer) throws Exception {
        return userService.edit(SessionHolder.createAnonymousSession(),
                getObjectMapper().readValue(buffer.toString(), UserImpl.class), new HashMap<>());
    }

    @Route(path = "/:id", methods = Route.HttpMethod.GET, order = 500)
    public Uni<UserImpl> getUserByIdNonBlocking(RoutingExchange exc, HttpServerResponse response, @Param("id") String id) throws Exception {
        logger.info("into /http/user/:id get");
        return getUserById(exc, response, id);
    }

    @Route(path = "/:id/blocking", methods = Route.HttpMethod.GET, type = Route.HandlerType.BLOCKING, order = 200)
    @Blocking
    public UserImpl getUserByIdBlocking(RoutingExchange exc, HttpServerResponse response, @Param("id") String id) throws Exception {
        logger.info("into /http/user/:id/blocking get");
        return getVertx().executeBlockingAndAwait(getUserById(exc, response, id));
    }

    @Route(path = "/*", methods = Route.HttpMethod.POST, order = 500)
    public Uni<Integer> addUserNonBlocking(RoutingContext ctx, @Body Buffer buffer) throws Exception {
        logger.info("into /http/user post");
        return userService.add(SessionHolder.createAnonymousSession(),
                getObjectMapper().readValue(buffer.toString(), UserImpl.class), new HashMap<>());
    }

    @Route(path = "/*", methods = Route.HttpMethod.PUT, order = 500)
    public Uni<Integer> editUserNonBlocking(RoutingContext ctx, @Body Buffer buffer) throws Exception {
        logger.info("into /http/user put");
        return editUser(ctx, buffer);
    }

    @Route(path = "/blocking/*", methods = Route.HttpMethod.PUT, type = Route.HandlerType.BLOCKING, order = 200)
    public Uni<Integer> editUserBlocking(RoutingContext ctx, @Body Buffer buffer) throws Exception {
        logger.info("into /http/user/nonblocking put");
        return editUser(ctx, buffer);
    }

    @Route(path = "/:id", methods = Route.HttpMethod.DELETE, order = 500)
    public Uni<Integer> removeUser(RoutingContext ctx, @Param("id") String id) throws Exception {
        logger.info("into /http/user delete");
        return userService.remove(SessionHolder.createAnonymousSession(), Long.parseLong(id), new HashMap<>());
    }

    @Route(path = "/username/:username", methods = Route.HttpMethod.GET, order = 200)
    public Uni<UserImpl> getByUsername(RoutingContext ctx, @Param("username") String username) throws Exception {
        logger.info("into /http/user/username get");
        return userService.getByUsername(SessionHolder.createAnonymousSession(), username, new HashMap<>())
                .onFailure().invoke(Unchecked.consumer((ex) -> {
                    if (ex instanceof NoResultException || ex instanceof NonUniqueResultException) {
                        ctx.response().setStatusCode(404).setStatusMessage(ex.getMessage());
                    } else {
                        throw new Exception(ex);
                    }
                }));
    }
}
