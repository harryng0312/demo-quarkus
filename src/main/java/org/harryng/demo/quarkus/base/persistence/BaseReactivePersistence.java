package org.harryng.demo.quarkus.base.persistence;

import io.smallrye.mutiny.Uni;
import org.harryng.demo.quarkus.base.entity.BaseEntity;
import org.hibernate.reactive.mutiny.Mutiny;

import java.io.Serializable;

public interface BaseReactivePersistence<Id extends Serializable, T extends BaseEntity<Id>> {
    public Class<T> getEntityClass();

    public Uni<T> selectById(Mutiny.Session session, Id id) throws RuntimeException, Exception;
    public Uni<Integer> insert(Mutiny.Session session, T obj) throws RuntimeException, Exception;
    public Uni<Integer> update(Mutiny.Session session, T obj) throws RuntimeException, Exception;
    public Uni<Integer> delete(Mutiny.Session session, Id obj) throws RuntimeException, Exception;
}