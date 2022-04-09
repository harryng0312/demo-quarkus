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
    // public Uni<Mutiny.Session> getTransSession();
    // public void setTransSession(Uni<Mutiny.Session> transSession);

    public Uni<T> getById(SessionHolder sessionHolder, Id id, Map<String, Object> extras) throws RuntimeException, Exception;
    public Uni<Integer> add(SessionHolder sessionHolder, T obj, Map<String, Object> extras) throws RuntimeException, Exception;
    public Uni<Integer> edit(SessionHolder sessionHolder, T obj, Map<String, Object> extras) throws RuntimeException, Exception;
    public Uni<Integer> remove(SessionHolder sessionHolder, Id id, Map<String, Object> extras) throws RuntimeException, Exception;
}
