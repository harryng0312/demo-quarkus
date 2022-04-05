package org.harryng.demo.quarkus.user.service;

import org.harryng.demo.quarkus.base.service.AbstractSearchableService;
import org.harryng.demo.quarkus.user.entity.UserImpl;
import org.harryng.demo.quarkus.user.persistence.UserPersistence;
import org.harryng.demo.quarkus.util.SessionHolder;
import org.harryng.demo.quarkus.util.page.Page;
import org.harryng.demo.quarkus.util.page.PageInfo;
import org.harryng.demo.quarkus.util.page.Sort;

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Named("userService")
public class UserServiceImpl extends AbstractSearchableService<Long, UserImpl> implements UserService {

    @Override
    public UserPersistence getPersistence() {
        return (UserPersistence) super.getPersistence();
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
