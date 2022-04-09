package org.harryng.demo.quarkus.user.service;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;

import org.harryng.demo.quarkus.base.persistence.BaseSearchableReactivePersistence;
import org.harryng.demo.quarkus.base.service.AbstractSearchableService;
import org.harryng.demo.quarkus.user.entity.UserImpl;
import org.harryng.demo.quarkus.user.persistence.UserPersistence;
import org.harryng.demo.quarkus.user.persistence.UserReactivePersistence;
import org.harryng.demo.quarkus.util.SessionHolder;
import org.harryng.demo.quarkus.util.page.Page;
import org.harryng.demo.quarkus.util.page.PageInfo;
import org.harryng.demo.quarkus.util.page.Sort;
import org.hibernate.reactive.mutiny.Mutiny;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Named("userService")
// @Transactional(Transactional.TxType.NOT_SUPPORTED)
public class UserServiceImpl extends AbstractSearchableService<Long, UserImpl> implements UserService {

    static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Inject
    protected UserPersistence userPersistence;

    @Inject
    protected UserReactivePersistence userReactivePersistence;

    @Override
    public UserPersistence getPersistence() {
        return this.userPersistence;
    }

    @Override
    public BaseSearchableReactivePersistence<Long, UserImpl> getReactivePersistence() {
        return this.userReactivePersistence;
    }
    
    @Override
    public Uni<UserImpl> getById(SessionHolder sessionHolder, Long id, Map<String, Object> extras) throws RuntimeException, Exception {
//        var user = getPersistence().selectById(id);
        var transSession = (Uni<Mutiny.Session>) extras.get("transSession");
        return transSession.flatMap(Unchecked.function(
            session -> getReactivePersistence().selectById(session, id)));
        // return Uni.createFrom().item((UserImpl) null);
    }

    @Override
    public Uni<Integer> add(SessionHolder sessionHolder, UserImpl user, Map<String, Object> extras) throws RuntimeException, Exception {
        var transSession = (Mutiny.Session) extras.get("transSession");
        logger.info("service transSession:" + transSession.hashCode());
        return Uni.combine().all().unis(
            Uni.createFrom().item(() -> {
                return 0;
            }),
            // transSession.flatMap(Unchecked.function(
            //     session -> super.add(sessionHolder, user, extras)
            //         .flatMap(Unchecked.function( itm -> {
            //             session.flush();
            //             return Uni.createFrom().item(itm);
            //         }))
            //         .eventually(session::close))),
            // getReactivePersistence().insert(transSession, user),
            super.add(sessionHolder, user, extras),
            Uni.createFrom().item(() -> {
                return -1;
            }))
            .combinedWith(lsRs ->(Integer)lsRs.get(1));
    }

    @Override
    public UserImpl getByUsername(SessionHolder sessionHolder, String username, Map<String, Object> extras) throws RuntimeException, Exception {
        var transSession = (Uni<Mutiny.Session>) extras.get("transSession");
        UserImpl result = null;
        // var pageInfo = new PageInfo(0, 5, 0, Sort.by(Sort.Direction.ASC, "id")); //PageRequest.of(0, 5, Sort.Direction.ASC, "id");
        // var jpql = "select u from " + UserImpl.class.getCanonicalName() + " u where u.username = :username";
        // var params = new HashMap<String, Serializable>();
        // params.put("username", username);
        // Page<UserImpl> pageResult = findByConditions(sessionHolder, jpql, params, pageInfo, 1, Collections.emptyMap());
        // if (pageResult.getTotal() > 0) {
        //     result = pageResult.getContent().get(0);
        // }
        return result;
    }
}
