package org.harryng.demo.quarkus.base.service;

import io.smallrye.mutiny.Uni;
import org.harryng.demo.quarkus.base.entity.BaseEntity;
import org.harryng.demo.quarkus.base.persistence.BasePersistence;
import org.harryng.demo.quarkus.base.persistence.BaseReactivePersistence;
import org.harryng.demo.quarkus.util.SessionHolder;

import java.io.Serializable;
import java.util.Map;

public interface BaseService<Id extends Serializable, T extends BaseEntity<Id>> {
    public BasePersistence<Id, T> getPersistence();
    public BaseReactivePersistence<Id, T> getReactivePersistence();

    public Uni<T> getById(SessionHolder session, Id id, Map<String, Serializable> extras) throws RuntimeException, Exception;
    public Uni<Integer> add(SessionHolder session, T obj, Map<String, Serializable> extras) throws RuntimeException, Exception;
    public Uni<Integer> edit(SessionHolder session, T obj, Map<String, Serializable> extras) throws RuntimeException, Exception;
    public Uni<Integer> remove(SessionHolder session, Id id, Map<String, Serializable> extras) throws RuntimeException, Exception;
}
