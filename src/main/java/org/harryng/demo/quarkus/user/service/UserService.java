package org.harryng.demo.quarkus.user.service;

import org.harryng.demo.quarkus.base.service.BaseSearchableService;
import org.harryng.demo.quarkus.user.entity.UserImpl;
import org.harryng.demo.quarkus.util.SessionHolder;

import java.io.Serializable;
import java.util.Map;

public interface UserService extends BaseSearchableService<Long, UserImpl> {
    public UserImpl getByUsername(SessionHolder session, String username, Map<String, Serializable> extras) throws RuntimeException, Exception;
}
