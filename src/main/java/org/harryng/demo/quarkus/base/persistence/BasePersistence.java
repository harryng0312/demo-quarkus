package org.harryng.demo.quarkus.base.persistence;

import org.harryng.demo.quarkus.base.entity.BaseEntity;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.lang.RuntimeException;

interface BasePersistence<Id extends Serializable, T extends BaseEntity<Id>> {
    public Class<T> getEntityClass();
    public EntityManager getEntityManager();

    public T selectById(Id id) throws RuntimeException, Exception;
    public int insert(T obj) throws RuntimeException, Exception;
    public int update(T obj) throws RuntimeException, Exception;
    public int delete(Id obj) throws RuntimeException, Exception;
}