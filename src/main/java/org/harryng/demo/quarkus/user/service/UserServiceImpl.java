package org.harryng.demo.quarkus.user.service;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import org.harryng.demo.quarkus.base.persistence.BaseSearchableReactivePersistence;
import org.harryng.demo.quarkus.base.service.AbstractSearchableService;
import org.harryng.demo.quarkus.interceptor.Authenticated;
import org.harryng.demo.quarkus.interceptor.Enriched;
import org.harryng.demo.quarkus.user.entity.UserImpl;
import org.harryng.demo.quarkus.user.mapper.UserMapper;
import org.harryng.demo.quarkus.user.persistence.UserPanachePersistence;
import org.harryng.demo.quarkus.user.persistence.UserPersistence;
import org.harryng.demo.quarkus.user.persistence.UserReactivePersistence;
import org.harryng.demo.quarkus.util.ReactiveUtil;
import org.harryng.demo.quarkus.util.SessionHolder;
import org.harryng.demo.quarkus.util.page.PageInfo;
import org.harryng.demo.quarkus.util.page.Sort;
import org.harryng.demo.quarkus.validation.ValidationPayloads;
import org.harryng.demo.quarkus.validation.ValidationResult;
import org.hibernate.LockMode;
import org.hibernate.validator.HibernateValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Named("userService")
@Enriched
@Authenticated
// @Transactional(Transactional.TxType.NOT_SUPPORTED)
public class UserServiceImpl extends AbstractSearchableService<Long, UserImpl> implements UserService {

    static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Inject
    protected UserPersistence userPersistence;
    @Inject
    protected UserReactivePersistence userReactivePersistence;

    @Inject
    protected Validator validator;

    @Inject
    protected UserPanachePersistence userPanachePersistence;

    @Inject
    protected UserMapper userMapper;

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
    @ReactiveTransactional
    public Uni<Integer> add(SessionHolder sessionHolder, UserImpl user, Map<String, Object> extras) throws RuntimeException, Exception {
        return userPanachePersistence.persist(user).flatMap(user1 -> Uni.createFrom().item(user1 == null ? 0 : 1));
    }

    @Override
    @ReactiveTransactional
    public Uni<Integer> edit(SessionHolder sessionHolder, UserImpl user, Map<String, Object> extras) throws RuntimeException, Exception {
        logger.info("edit user");
//        return vertx.executeBlocking(Uni.createFrom().item(() -> {
////                    logger.info("validate user in blocking");
//                    var payloadMap = ValidationPayloads.newInstance();
//                    payloadMap.put(SessionHolder.class, sessionHolder);
//                    payloadMap.put(Map.class, extras);
//                    payloadMap.put(UserService.class, this);
//                    var validator = validatorFactory.unwrap(HibernateValidatorFactory.class)
//                            .usingContext()
//                            .constraintValidatorPayload(payloadMap)
//                            .getValidator();
//                    var valRs = validator.validate(user, EditUserContraint.class);
//                    return ValidationResult.getInstance(valRs, sessionHolder.getLocale());
//                }))
//                .flatMap(Unchecked.function(valiRs -> {
//                    if (!valiRs.isSuccess()) {
//                        throw new Exception(valiRs.getMessagesInJson());
//                    }
//                    return userPanachePersistence.findById(user.getId())
//                            .invoke(Unchecked.consumer(oldUser -> {
//                                if (oldUser == null) {
//                                    throw new NoResultException();
//                                }
//                                userMapper.populateEntity(user, oldUser);
//                            }))
//                            .call(oldUser -> userPanachePersistence.persist(oldUser))
//                            .onFailure().recoverWithItem(Unchecked.function(ex -> {
//                                if (ex instanceof NoResultException) {
//                                    return new UserImpl();
//                                } else {
//                                    throw new Exception(ex);
//                                }
//                            }))
//                            .flatMap(newUser -> Uni.createFrom().item(newUser.getId() == 0L ? 0 : 1));
//                }));
        return Uni.createFrom().item(() -> {
                    var valRs = validator.validate(user);
                    return ValidationResult.getInstance(valRs, sessionHolder.getLocale());
                })
                .flatMap(Unchecked.function(validationResult -> {
                    if (!validationResult.isSuccess()) {
                        throw new Exception(validationResult.getMessagesInJson());
                    }
                    return getByUsername(sessionHolder, user.getUsername(), extras);
                }))
                .flatMap(Unchecked.function(user1 -> userPanachePersistence.getSession()
                                .flatMap(session -> session.lock(user1, LockMode.PESSIMISTIC_WRITE))
                                .flatMap(v -> Uni.createFrom().item(user1))
                ))
                .map(Unchecked.function(oldUser -> {
                    userMapper.populateEntity(user, oldUser);
                    oldUser.setModifiedDate(LocalDateTime.now());
                    return oldUser;
                }))
                .flatMap(oldUser -> userPanachePersistence.persist(oldUser))
                .onFailure().recoverWithItem(Unchecked.function(ex -> {
                    if (ex instanceof NoResultException || ex instanceof NonUniqueResultException) {
                        return new UserImpl();
                    } else {
                        throw new Exception(ex);
                    }
                }))
                .flatMap(newUser -> Uni.createFrom().item(newUser.getId() == null ? 0 : 1));
    }

    @Override
    @ReactiveTransactional
    public Uni<Integer> remove(SessionHolder sessionHolder, Long id, Map<String, Object> extras) throws RuntimeException, Exception {
        return userPanachePersistence.deleteById(id).flatMap(result -> Uni.createFrom().item(result ? 1 : 0));
//        var session = (Mutiny.StatelessSession) extras.get(BaseService.TRANS_STATELESS_SESSION);
//        return userReactivePersistence.delete(session, id);
    }


    @Override
    public Uni<UserImpl> getByUsername(SessionHolder sessionHolder, String username, Map<String, Object> extras) throws RuntimeException, Exception {
//        var transSession = (Mutiny.StatelessSession) extras.get(TRANS_STATELESS_SESSION);
        UserImpl result = null;
        var pageInfo = new PageInfo(0, 5, 0, Sort.by(Sort.Direction.ASC, "id")); //PageRequest.of(0, 5, Sort.Direction.ASC, "id");
        var jpql = "select u from " + UserImpl.class.getCanonicalName() + " u where u.username = :username";
        var params = new HashMap<String, Object>();
        params.put("username", username);
//        Uni<Page<UserImpl>> pageResult = findByConditions(sessionHolder, jpql, params, pageInfo, 1, Collections.emptyMap());
//        return pageResult.flatMap(userPage -> {
//            if (userPage.getTotal() > 0) {
//                return Uni.createFrom().item(userPage.getContent().stream().findFirst().get());
//            } else {
//                return Uni.createFrom().nullItem();
//            }
//        });
        return userPanachePersistence.find(jpql, params).singleResult();
    }
}
