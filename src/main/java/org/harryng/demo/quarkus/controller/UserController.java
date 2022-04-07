package org.harryng.demo.quarkus.controller;

import io.quarkus.logging.Log;
import io.vertx.core.http.HttpServerRequest;
import org.harryng.demo.quarkus.user.service.UserService;
import org.harryng.demo.quarkus.util.SessionHolder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
}
