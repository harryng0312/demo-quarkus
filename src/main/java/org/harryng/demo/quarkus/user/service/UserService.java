package org.harryng.demo.quarkus.user.service;

import io.smallrye.mutiny.Uni;
import org.harryng.demo.quarkus.base.service.BaseSearchableService;
import org.harryng.demo.quarkus.user.entity.UserImpl;
import org.harryng.demo.quarkus.util.SessionHolder;

import java.util.Map;

public interface UserService extends BaseSearchableService<Long, UserImpl> {
    public Uni<UserImpl> getByUsername(SessionHolder session, String username, Map<String, Object> extras) throws RuntimeException, Exception;
}
