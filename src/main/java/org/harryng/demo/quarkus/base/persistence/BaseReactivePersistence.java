package org.harryng.demo.quarkus.base.persistence;

import io.smallrye.mutiny.Uni;
import org.harryng.demo.quarkus.base.entity.BaseEntity;
import org.hibernate.reactive.mutiny.Mutiny;

import java.io.Serializable;

public interface BaseReactivePersistence<Id extends Serializable, T extends BaseEntity<Id>> {
    public Class<T> getEntityClass();

    public Uni<T> selectById(Mutiny.StatelessSession session, Id id) throws RuntimeException, Exception;
    public Uni<Integer> insert(Mutiny.StatelessSession session, T obj) throws RuntimeException, Exception;
    public Uni<Integer> update(Mutiny.StatelessSession session, T obj) throws RuntimeException, Exception;
    public Uni<Integer> delete(Mutiny.StatelessSession session, Id obj) throws RuntimeException, Exception;
}