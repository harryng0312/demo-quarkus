package org.harryng.demo.quarkus.user.service;

import io.smallrye.mutiny.Uni;
import org.harryng.demo.quarkus.base.service.AbstractSearchableService;
import org.harryng.demo.quarkus.user.entity.UserImpl;
import org.harryng.demo.quarkus.user.persistence.UserPersistence;
import org.harryng.demo.quarkus.util.SessionHolder;
import org.harryng.demo.quarkus.util.page.Page;
import org.harryng.demo.quarkus.util.page.PageInfo;
import org.harryng.demo.quarkus.util.page.Sort;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Named("userService")
@Transactional(Transactional.TxType.NOT_SUPPORTED)
public class UserServiceImpl extends AbstractSearchableService<Long, UserImpl> implements UserService {

    @Inject
    protected UserPersistence userPersistence;

    @Override
    public UserPersistence getPersistence() {
        return this.userPersistence;
    }

    @Override
    public Uni<UserImpl> getById(SessionHolder session, Long id, Map<String, Serializable> extras) throws RuntimeException, Exception {
        var user = getPersistence().selectById(id);
        return Uni.createFrom().item(user);
    }

    @Override
    public UserImpl getByUsername(SessionHolder session, String username, Map<String, Serializable> extras) throws RuntimeException, Exception {
        UserImpl result = null;
        var pageInfo = new PageInfo(0, 5, 0, Sort.by(Sort.Direction.ASC, "id")); //PageRequest.of(0, 5, Sort.Direction.ASC, "id");
        var jpql = "select u from " + UserImpl.class.getCanonicalName() + " u where u.username = :username";
        var params = new HashMap<String, Serializable>();
        params.put("username", username);
        Page<UserImpl> pageResult = findByConditions(session, jpql, params, pageInfo, 1, Collections.emptyMap());
        if (pageResult.getTotal() > 0) {
            result = pageResult.getContent().get(0);
        }
        return result;
    }
}
