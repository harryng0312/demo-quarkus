package org.harryng.demo.quarkus.controller;

import io.quarkus.logging.Log;
import io.smallrye.common.annotation.NonBlocking;
import io.vertx.core.http.HttpServerRequest;
import org.harryng.demo.quarkus.user.entity.UserImpl;
import org.harryng.demo.quarkus.user.service.UserService;
import org.harryng.demo.quarkus.util.SessionHolder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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

    @GET
    @Path("/get-username-sync")
    @Produces({MediaType.TEXT_PLAIN})
//    @NonBlocking
    public String getUsernameSync(@QueryParam("id") long id){
        String rs = "";
        try {
            var strBuilder = new StringBuilder();
            strBuilder.append(request.uri())
                    .append("\n");
            var opt = userService.getById(SessionHolder.createAnonymousSession(), id, Collections.emptyMap());
            if(opt.isPresent()){
                rs = opt.get().getUsername();
                strBuilder.append(rs);
            }
            rs = strBuilder.toString();
        } catch (Exception e) {
            Log.error("", e);
        }
        return rs;
    }

    @GET
    @Path("/get-user-by-id")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public UserImpl getUserById(@QueryParam("id") long id){
        UserImpl rs = null;
        try {
            var opt = userService.getById(SessionHolder.createAnonymousSession(), id, Collections.emptyMap());
            if(opt.isPresent()){
                rs = opt.get();
            }
        } catch (Exception e) {
            Log.error("", e);
        }
        return rs;
    }
}
