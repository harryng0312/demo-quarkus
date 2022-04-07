package org.harryng.demo.quarkus.controller;

import io.quarkus.logging.Log;
import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.mutiny.core.Vertx;
import org.harryng.demo.quarkus.user.entity.UserImpl;
import org.harryng.demo.quarkus.user.service.UserService;
import org.harryng.demo.quarkus.util.SessionHolder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collections;

@ApplicationScoped
@Path("/user")
public class UserController {

    @Inject
    protected UserService userService;

    @Inject
    protected HttpServerRequest request;

    @Inject
    protected Vertx vertx;

    @GET
    @Path("/get-username-sync")
    @Produces({MediaType.TEXT_PLAIN})
//    @NonBlocking
    public String getUsernameSync(@QueryParam("id") long id) {
        String rs = "";
        try {
            var strBuilder = new StringBuilder();
            strBuilder.append(request.uri()).append("\n");
            var opt = userService.getById(SessionHolder.createAnonymousSession(), id, Collections.emptyMap());
            var user = opt
                    .onItem().ifNull().continueWith(UserImpl::new)
                    .await().indefinitely();
            strBuilder.append(user.getUsername());
            rs = strBuilder.toString();
        } catch (Exception e) {
            Log.error("", e);
        }
        return rs;
    }

    @GET
    @Path("/get-user-by-id-sync")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public UserImpl getUserByIdSync(@QueryParam("id") long id) {
        UserImpl rs = null;
        try {
            var opt = userService.getById(SessionHolder.createAnonymousSession(), id, Collections.emptyMap());
            rs = opt.onItem().ifNull().continueWith(UserImpl::new).await().indefinitely();
        } catch (Exception e) {
            Log.error("", e);
        }
        return rs;
    }

    @GET
    @Path("/get-user-by-id-async")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Uni<UserImpl> getUserByIdAsync(@QueryParam("id") long id) {
        Uni<UserImpl> rs = null;
        try {
            rs = userService.getById(SessionHolder.createAnonymousSession(), id, Collections.emptyMap());
            rs.onItem().ifNull().continueWith(UserImpl::new);
        } catch (Exception e) {
            Log.error("", e);
        }
        return rs;
//        return vertx.executeBlocking(rs);
    }
}
