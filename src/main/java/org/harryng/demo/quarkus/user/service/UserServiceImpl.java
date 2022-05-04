package org.harryng.demo.quarkus.user.service;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import io.vertx.core.MultiMap;
import org.harryng.demo.quarkus.base.persistence.BaseSearchableReactivePersistence;
import org.harryng.demo.quarkus.base.service.AbstractSearchableService;
import org.harryng.demo.quarkus.base.service.BaseService;
import org.harryng.demo.quarkus.user.entity.UserImpl;
import org.harryng.demo.quarkus.user.persistence.UserPersistence;
import org.harryng.demo.quarkus.user.persistence.UserReactivePersistence;
import org.harryng.demo.quarkus.util.SessionHolder;
import org.harryng.demo.quarkus.validation.ValidationPayloads;
import org.harryng.demo.quarkus.validation.ValidationResult;
import org.harryng.demo.quarkus.validation.annotation.EditUserContraint;
import org.hibernate.reactive.mutiny.Mutiny;
import org.hibernate.validator.HibernateValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
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
//    @Inject
//    protected Validator validator;

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
        var transSession = (Mutiny.StatelessSession) extras.get(TRANS_STATELESS_SESSION);
        // return transSession.flatMap(Unchecked.function(
        //     session -> getReactivePersistence().selectById(session, id)));
        return transSession.get(getReactivePersistence().getEntityClass(), id);
    }

    @Override
    public Uni<Integer> add(SessionHolder sessionHolder, UserImpl user, Map<String, Object> extras) throws RuntimeException, Exception {
        return super.add(sessionHolder, user, extras);
    }

    @Override
    public Uni<Integer> edit(SessionHolder sessionHolder, UserImpl user, Map<String, Object> extras) throws RuntimeException, Exception {
        return vertx.executeBlocking(Uni.createFrom().item(() -> {
            var payloadMap = ValidationPayloads.newInstance();
            payloadMap.put(SessionHolder.class, sessionHolder);
            payloadMap.put(Map.class, extras);
            payloadMap.put(UserService.class, this);
            var validator = validatorFactory.unwrap(HibernateValidatorFactory.class)
                    .usingContext()
                    .constraintValidatorPayload(payloadMap)
                    .getValidator();
            var valRs = validator.validate(user, EditUserContraint.class);
            var headers = (MultiMap) extras.get(BaseService.HTTP_HEADERS);
            return ValidationResult.getInstance(valRs, headers.get("Accept-Language"));
        })).flatMap(Unchecked.function(valiRs -> {
            if (!valiRs.isSuccess()) {
                throw new Exception(valiRs.getMessagesInJson());
            }
            return super.edit(sessionHolder, user, extras);
        }));
    }

    @Override
    public Uni<Integer> remove(SessionHolder sessionHolder, Long id, Map<String, Object> extras) throws RuntimeException, Exception {
        return super.remove(sessionHolder, id, extras);
    }


    @Override
    public Uni<UserImpl> getByUsername(SessionHolder sessionHolder, String username, Map<String, Object> extras) throws RuntimeException, Exception {
        var transSession = (Mutiny.StatelessSession) extras.get(TRANS_STATELESS_SESSION);
        // var pageInfo = new PageInfo(0, 5, 0, Sort.by(Sort.Direction.ASC, "id")); //PageRequest.of(0, 5, Sort.Direction.ASC, "id");
        var jpql = "select u from " + UserImpl.class.getCanonicalName() + " u where u.username = :username";
        // var params = new HashMap<String, Serializable>();
        // params.put("username", username);
        // Page<UserImpl> pageResult = findByConditions(sessionHolder, jpql, params, pageInfo, 1, Collections.emptyMap());
        // if (pageResult.getTotal() > 0) {
        //     result = pageResult.getContent().get(0);
        // }

        return transSession.<UserImpl>createQuery(jpql)
                .setParameter("username", username).getSingleResultOrNull();
    }
}
