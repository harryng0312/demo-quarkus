package org.harryng.demo.quarkus.controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import org.harryng.demo.quarkus.base.controller.AbstractController;
import org.harryng.demo.quarkus.base.service.BaseService;
import org.harryng.demo.quarkus.user.entity.UserImpl;
import org.harryng.demo.quarkus.user.service.UserService;
import org.harryng.demo.quarkus.util.SessionHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import java.util.Collections;
import java.util.Map;

@ApplicationScoped
// @RequestScoped
@Path("/user")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class UserController extends AbstractController {
    @Inject
    protected UserService userService;
    protected Logger logger = LoggerFactory.getLogger(UserController.class);

    @GET
    @Path("/get-username-block")
    @Produces({MediaType.TEXT_PLAIN})
    public String getUsernameSync(@QueryParam("id") long id) {
        String rs = "";
        try {
            var strBuilder = new StringBuilder();
            strBuilder.append(getServerRequest().uri()).append("\n");
            var opt = userService.getById(SessionHolder.createAnonymousSession(), id, Collections.emptyMap());
            var user = opt
                    .onItem().ifNull().continueWith(UserImpl::new)
                    .await().indefinitely();
            strBuilder.append(user.getUsername());
            rs = strBuilder.toString();
        } catch (Exception e) {
            logger.error("", e);
        }
        return rs;
    }

    @GET
    @Path("/get-user-by-id-block")
    @Blocking
    public UserImpl getUserByIdBlock(@QueryParam("id") long id) {
        UserImpl rs = null;
        try {
            var opt = sessionFactory.withStatelessTransaction(Unchecked.function((session, trans) ->
                    userService.getById(SessionHolder.createAnonymousSession(), id,
                            Map.of(BaseService.TRANS_STATELESS_SESSION, session,
                                    BaseService.TRANSACTION, trans
                            ))));
            rs = opt.onItem().ifNull().continueWith(UserImpl::new).await().indefinitely();
        } catch (Exception e) {
            logger.error("", e);
        }
        return rs;
    }

    @GET
    @Path("/get-user-by-id-nonblock")
    public Uni<Response> getUserByIdNonBlock(@QueryParam("id") long id) {
        return sessionFactory.withStatelessTransaction(Unchecked.function((session, trans) -> {
            Uni<Response> rs = Uni.createFrom().item(Response.status(Status.NOT_FOUND).build());
            try {
                rs = userService.getById(SessionHolder.createAnonymousSession(), id,
                                Map.of(BaseService.TRANS_STATELESS_SESSION, session,
                                        BaseService.TRANSACTION, trans))
                        .map(Unchecked.function(user -> {
                            String val = "{}";
                            Response result = null;
                            try {
                                if (user != null) {
                                    val = getObjectMapper().writeValueAsString(user);
                                    result = Response.ok().entity(val).build();
                                } else {
                                    // throw new NotFoundException();
                                    // throw new ClientErrorException(Response.Status.NOT_FOUND);
                                    result = Response.status(Status.NOT_FOUND).build();
                                }
                            } catch (JsonProcessingException e) {
                                result = Response.status(Status.INTERNAL_SERVER_ERROR).build();
                                logger.error("", e);
                            }
                            return result;
                        }));
            } catch (Exception e) {
                logger.error("", e);
            }
            return rs;
        }));
//        return vertx.executeBlocking(rs);
    }

    @POST
    @Path("/add-user-nonblock")
    public Uni<Response> addUserNonBlock(String reqBodyStr) throws RuntimeException, Exception {
        var userImpl = getObjectMapper().readValue(reqBodyStr, UserImpl.class);
        return sessionFactory.withStatelessTransaction(Unchecked.function((session, trans) -> {
                    logger.info("controller transSession:" + session.hashCode());
                    return userService.add(
                            SessionHolder.createAnonymousSession(), userImpl,
                            Map.of(BaseService.TRANS_STATELESS_SESSION, session,
                                    BaseService.TRANSACTION, trans));
                })).flatMap(item -> Uni.createFrom().item(Response.ok(item).build()))
                .onFailure().recoverWithItem(ex -> Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex).build());
    }

    @POST
    @Path("/edit-user-nonblock")
    public Uni<Response> editUserNonBlock(String reqBodyStr) throws RuntimeException, Exception {
        // return sessionFactory.withTransaction(Unchecked.function((session, trans) -> {
        //     logger.info("controller transSession:" + session.hashCode());
        //     return userService.edit(
        //         SessionHolder.createAnonymousSession(), userImpl, 
        //         Collections.singletonMap(BaseService.TRANS_SESSION, session));
        // })).flatMap(item -> Uni.createFrom().item(Response.ok(item).build()));
        return sessionFactory.withStatelessTransaction(Unchecked.function((session, trans) -> {
                    logger.info("controller sessFact: " + sessionFactory.hashCode());
                    var userImpl = getObjectMapper().readValue(reqBodyStr, UserImpl.class);
                    logger.info("controller transSession:" + session.hashCode());
                    return userService.edit(
                            SessionHolder.createAnonymousSession(), userImpl,
                            Map.of(BaseService.TRANS_STATELESS_SESSION, session,
                                    BaseService.TRANSACTION, trans));
                })).flatMap(item -> Uni.createFrom().item(Response.ok(item).build()))
                .onFailure()
                .recoverWithItem(ex -> Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build());
    }

    @GET
    @Path("/remove-user-nonblock")
    public Uni<Response> removeUserNonBlock(@QueryParam("id") Long userId) throws RuntimeException, Exception {
        return sessionFactory.withStatelessTransaction(Unchecked.function((session, trans) -> {
                    logger.info("controller transSession:" + session.hashCode());
                    return userService.remove(
                            SessionHolder.createAnonymousSession(), userId,
                            Map.of(BaseService.TRANS_STATELESS_SESSION, session,
                                    BaseService.TRANSACTION, trans));
                })).flatMap(item -> Uni.createFrom().item(Response.ok(item).build()))
                .onFailure().recoverWithItem(ex -> Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex).build());
    }
}
