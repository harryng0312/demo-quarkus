package org.harryng.demo.quarkus.base.service;

import org.harryng.demo.quarkus.base.entity.BaseEntity;
import org.harryng.demo.quarkus.base.persistence.BasePersistence;
import org.harryng.demo.quarkus.util.SessionHolder;

import java.io.Serializable;
import java.util.Map;

public class AbstractService<Id extends Serializable, T extends BaseEntity<Id>> implements BaseService<Id, T> {
    protected BasePersistence<Id, T> persistence;

    @Override
    public BasePersistence<Id, T> getPersistence() {
        return persistence;
    }

    @Override
    public T getById(SessionHolder session, Id id, Map<String, Serializable> extras) throws RuntimeException, Exception {
        return getPersistence().selectById(id);
    }

    @Override
    public int add(SessionHolder session, T obj, Map<String, Serializable> extras) throws RuntimeException, Exception {
        return getPersistence().insert(obj);
    }

    @Override
    public int edit(SessionHolder session, T obj, Map<String, Serializable> extras) throws RuntimeException, Exception {
        return getPersistence().update(obj);
    }

    @Override
    public int remove(SessionHolder session, Id id, Map<String, Serializable> extras) throws RuntimeException, Exception {
        return getPersistence().delete(id);
    }
}
