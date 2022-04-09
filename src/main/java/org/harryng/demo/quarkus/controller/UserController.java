package org.harryng.demo.quarkus.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import org.harryng.demo.quarkus.base.controller.AbstractController;
import org.harryng.demo.quarkus.user.entity.UserImpl;
import org.harryng.demo.quarkus.user.service.UserService;
import org.harryng.demo.quarkus.util.SessionHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import java.util.Collections;

// @ApplicationScoped
@RequestScoped
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
    public UserImpl getUserByIdSync(@QueryParam("id") long id) {
        UserImpl rs = null;
        try {
            var opt = userService.getById(SessionHolder.createAnonymousSession(), id, Collections.emptyMap());
            rs = opt.onItem().ifNull().continueWith(UserImpl::new).await().indefinitely();
        } catch (Exception e) {
            logger.error("", e);
        }
        return rs;
    }

    @GET
    @Path("/get-user-by-id-nonblock")
    public Uni<Response> getUserByIdAsync(@QueryParam("id") long id) {
        Uni<Response> rs = Uni.createFrom().item(Response.status(Status.NOT_FOUND).build());
        try {
            rs = userService.getById(SessionHolder.createAnonymousSession(), id, Collections.emptyMap())
                    .flatMap(Unchecked.function(user -> {
                        String val = "{}";
                        Response result = null;;
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
                        return Uni.createFrom().item(result);
                    }));
        } catch (Exception e) {
            logger.error("", e);
        }
        return rs;
//        return vertx.executeBlocking(rs);
    }
    
    @POST
    @Path("/add-user-nonblock")
    public Uni<Response> addUserNonBlock(String reqBodyStr) throws RuntimeException, Exception{
        var userImpl = getObjectMapper().readValue(reqBodyStr, UserImpl.class);
        return sessionFactory.withTransaction(Unchecked.function((session, trans) -> {
            logger.info("controller transSession:" + session.hashCode());
            return userService.add(
                SessionHolder.createAnonymousSession(), userImpl, 
                Collections.singletonMap("transSession", session));
        })).flatMap(item -> Uni.createFrom().item(Response.ok(item).build()));
    }
}
