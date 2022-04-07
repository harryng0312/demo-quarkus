package org.harryng.demo.quarkus.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import org.harryng.demo.quarkus.base.controller.AbstractController;
import org.harryng.demo.quarkus.user.entity.UserImpl;
import org.harryng.demo.quarkus.user.service.UserService;
import org.harryng.demo.quarkus.util.SessionHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collections;

@ApplicationScoped
@Path("/user")
public class UserController extends AbstractController {

    @Inject
    protected UserService userService;

    protected Logger logger = LoggerFactory.getLogger(UserController.class);

    @GET
    @Path("/get-username-sync")
    @Produces({MediaType.TEXT_PLAIN})
//    @NonBlocking
    public String getUsernameSync(@QueryParam("id") long id) {
        String rs = "";
        try {
            var strBuilder = new StringBuilder();
            strBuilder.append(getRequest().uri()).append("\n");
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
            logger.error("", e);
        }
        return rs;
    }

    @GET
    @Path("/get-user-by-id-async")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Uni<String> getUserByIdAsync(@QueryParam("id") long id) {
        Uni<String> rs = Uni.createFrom().item("{}");
        try {
            rs = userService.getById(SessionHolder.createAnonymousSession(), id, Collections.emptyMap())
                    .flatMap(Unchecked.function(user -> {
                        String val = "{}";
                        try {
                            if (user != null) {
                                val = getObjectMapper().writeValueAsString(user);
                            } else {
                                throw new NotFoundException();
                            }
                        } catch (JsonProcessingException e) {
                            logger.error("", e);
                        }
                        return Uni.createFrom().item(val);
                    }));
        } catch (Exception e) {
            logger.error("", e);
        }
        return rs;
//        return vertx.executeBlocking(rs);
    }
}
