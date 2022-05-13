package org.harryng.demo.quarkus.user.service;

import io.smallrye.mutiny.Uni;
import org.harryng.demo.quarkus.base.persistence.BaseSearchableReactivePersistence;
import org.harryng.demo.quarkus.base.service.AbstractSearchableService;
import org.harryng.demo.quarkus.user.entity.UserImpl;
import org.harryng.demo.quarkus.user.persistence.UserPanachePersistence;
import org.harryng.demo.quarkus.user.persistence.UserPersistence;
import org.harryng.demo.quarkus.user.persistence.UserReactivePersistence;
import org.harryng.demo.quarkus.util.SessionHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Singleton
@Named("userService")
// @Transactional(Transactional.TxType.NOT_SUPPORTED)
public class UserServiceImpl extends AbstractSearchableService<Long, UserImpl> implements UserService {

    static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Inject
    protected UserPersistence userPersistence;

    @Inject
    protected UserReactivePersistence userReactivePersistence;

    @Inject
    protected UserPanachePersistence userPanachePersistence;

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
        // var transSession = (Uni<Mutiny.Session>) extras.get(TRANS_SESSION);
//        var transSession = (Mutiny.StatelessSession) extras.get(TRANS_STATELESS_SESSION);
//         return transSession.flatMap(Unchecked.function(
        //     session -> getReactivePersistence().selectById(session, id)));
//        return transSession.get(getReactivePersistence().getEntityClass(), id);
        return userPanachePersistence.findById(id);
    }

    @Override
    public Uni<Integer> add(SessionHolder sessionHolder, UserImpl user, Map<String, Object> extras) throws RuntimeException, Exception {
        // var transSession = (Mtiny.Session) extras.get("transSession");
//        var transSession = (Mutiny.StatelessSession) extras.get(TRANS_STATELESS_SESSION);
//        logger.info("service transSession:" + transSession.hashCode());
//        return Uni.combine().all().unis(
//            Uni.createFrom().item(() -> {
//                return 0;
//            }),
//            transSession.insert(user),
//            Uni.createFrom().item(() -> {
//                // throw new RuntimeException("break trans from item");
//                return -1;
//            // }).call(item -> {
//            //     throw new RuntimeException("break trans from call");
//            }))
//            .combinedWith(lsRs ->(Integer)lsRs.get(1));
        return Uni.createFrom().item(userPanachePersistence.persist(user) != null ? 1 : 0);
    }

    @Override
    public Uni<Integer> edit(SessionHolder sessionHolder, UserImpl user, Map<String, Object> extras) throws RuntimeException, Exception {
        // var transSession = (Mutiny.Session) extras.get(TRANS_SESSION);
//        var transSession = (Mutiny.StatelessSession) extras.get(TRANS_STATELESS_SESSION);
//        logger.info("service transSession:" + transSession.hashCode());
//        return Uni.combine().all().unis(
//                        Uni.createFrom().item(() -> {
//                            return 0;
//                        }),
//                        super.edit(sessionHolder, user, extras),
//                        // transSession.update(user),
//                        Uni.createFrom().item(() -> {
//                            // throw new RuntimeException("break trans from item");
//                            return -1;
//                            // }).call(item -> {
//                            // throw new RuntimeException("break trans from call");
//                        }))
//                .combinedWith(lsRs -> (Integer) lsRs.get(1));
        var uniRs = new AtomicReference<>(Uni.createFrom().item(0));
        if (!userPanachePersistence.isPersistent(user)) {
            var userUni = userPanachePersistence.persist(user);
            userUni.subscribe().with(userImpl -> {
                uniRs.set(Uni.createFrom().item(1));
            });
        }
        return uniRs.get();
    }

    @Override
    public Uni<Integer> remove(SessionHolder sessionHolder, Long id, Map<String, Object> extras) throws RuntimeException, Exception {
//        var transSession = (Mutiny.StatelessSession) extras.get(TRANS_STATELESS_SESSION);
//        logger.info("service transSession:" + transSession.hashCode());
//        return Uni.combine().all().unis(
//                        Uni.createFrom().item(() -> {
//                            return 0;
//                        }),
//                        super.remove(sessionHolder, id, extras),
//                        Uni.createFrom().item(() -> {
//                            // throw new RuntimeException("break trans from item");
//                            return -1;
//                            // }).call(item -> {
//                            // throw new RuntimeException("break trans from call");
//                        }))
//                .combinedWith(lsRs -> (Integer) lsRs.get(1));
        return userPanachePersistence.deleteById(id).flatMap(result -> Uni.createFrom().item(result ? 1 : 0));
    }


    @Override
    public Uni<UserImpl> getByUsername(SessionHolder sessionHolder, String username, Map<String, Object> extras) throws RuntimeException, Exception {
//        var transSession = (Mutiny.StatelessSession) extras.get(TRANS_STATELESS_SESSION);
//        UserImpl result = null;
        // var pageInfo = new PageInfo(0, 5, 0, Sort.by(Sort.Direction.ASC, "id")); //PageRequest.of(0, 5, Sort.Direction.ASC, "id");
        var jpql = "select u from " + UserImpl.class.getCanonicalName() + " u where u.username = :username";
        var params = new HashMap<String, Serializable>();
        params.put("username", username);
        // Page<UserImpl> pageResult = findByConditions(sessionHolder, jpql, params, pageInfo, 1, Collections.emptyMap());
        // if (pageResult.getTotal() > 0) {
        //     result = pageResult.getContent().get(0);
        // }

        return userPanachePersistence.find(jpql, params).firstResult();
    }
}
