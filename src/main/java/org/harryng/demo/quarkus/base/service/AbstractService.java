package org.harryng.demo.quarkus.base.service;

import io.quarkus.hibernate.orm.PersistenceUnit;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.harryng.demo.quarkus.base.entity.AbstractEntity;
import org.harryng.demo.quarkus.base.persistence.BasePersistence;
import org.harryng.demo.quarkus.base.persistence.BaseReactivePersistence;
import org.harryng.demo.quarkus.util.SessionHolder;
import org.hibernate.reactive.mutiny.Mutiny;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Map;

@Transactional(Transactional.TxType.NOT_SUPPORTED)
public abstract class AbstractService<Id extends Serializable, T extends AbstractEntity<Id>> implements BaseService<Id, T> {

    @Inject
    protected ManagedExecutor managedExecutor;

    @Inject
    protected Mutiny.SessionFactory sessionFactory;

    @Override
    public abstract BasePersistence<Id, T> getPersistence();

    @Override
    public abstract BaseReactivePersistence<Id, T> getReactivePersistence();

    @Override
    public Uni<T> getById(SessionHolder session, Id id, Map<String, Serializable> extras) throws RuntimeException, Exception {
        return Uni.createFrom().item(getPersistence().selectById(id));
    }

    @Override
    public Uni<Integer> add(SessionHolder session, T obj, Map<String, Serializable> extras) throws RuntimeException, Exception {
        return Uni.createFrom().item(getPersistence().insert(obj));
    }

    @Override
    public Uni<Integer> edit(SessionHolder session, T obj, Map<String, Serializable> extras) throws RuntimeException, Exception {
        return Uni.createFrom().item(getPersistence().update(obj));
    }

    @Override
    public Uni<Integer> remove(SessionHolder session, Id id, Map<String, Serializable> extras) throws RuntimeException, Exception {
        return Uni.createFrom().item(getPersistence().delete(id));
    }
}
