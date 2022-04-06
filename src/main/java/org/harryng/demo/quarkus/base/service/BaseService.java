package org.harryng.demo.quarkus.base.service;

import org.harryng.demo.quarkus.base.entity.BaseEntity;
import org.harryng.demo.quarkus.base.persistence.BasePersistence;
import org.harryng.demo.quarkus.util.SessionHolder;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

public interface BaseService<Id extends Serializable, T extends BaseEntity<Id>> {
    public BasePersistence<Id, T> getPersistence();

    public Optional<T> getById(SessionHolder session, Id id, Map<String, Serializable> extras) throws RuntimeException, Exception;
    public int add(SessionHolder session, T obj, Map<String, Serializable> extras) throws RuntimeException, Exception;
    public int edit(SessionHolder session, T obj, Map<String, Serializable> extras) throws RuntimeException, Exception;
    public int remove(SessionHolder session, Id id, Map<String, Serializable> extras) throws RuntimeException, Exception;
}
