package org.harryng.demo.quarkus.router.ws;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import org.harryng.demo.quarkus.base.controller.AbstractController;
import org.harryng.demo.quarkus.base.service.BaseService;
import org.harryng.demo.quarkus.user.entity.UserImpl;
import org.harryng.demo.quarkus.user.service.UserService;
import org.harryng.demo.quarkus.util.SessionHolder;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Map;

@Singleton
@Named("userRouter")
public class UserRouter extends AbstractController {

    @Inject
    protected UserService userService;

    public Uni<UserImpl> getUserById(long id) {
        return sessionFactory.withStatelessTransaction(Unchecked.function(
                (session, trans) -> userService.getById(SessionHolder.createAnonymousSession(), id,
                Map.of(BaseService.TRANS_STATELESS_SESSION, session,
                        BaseService.TRANSACTION, trans))
                )
        );
    }

    public Uni<Integer> addUser(UserImpl user) {
        return sessionFactory.withStatelessTransaction(Unchecked.function(
                        (session, trans) -> userService.add(SessionHolder.createAnonymousSession(), user,
                                Map.of(BaseService.TRANS_STATELESS_SESSION, session,
                                        BaseService.TRANSACTION, trans))
                )
        );
    }
}
