package org.harryng.demo.quarkus.base.service;

import io.smallrye.mutiny.Uni;

import io.vertx.mutiny.core.Vertx;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.harryng.demo.quarkus.base.entity.AbstractEntity;
import org.harryng.demo.quarkus.base.persistence.BasePersistence;
import org.harryng.demo.quarkus.base.persistence.BaseReactivePersistence;
import org.harryng.demo.quarkus.util.SessionHolder;
import org.hibernate.reactive.mutiny.Mutiny;

import javax.inject.Inject;
import javax.validation.ValidatorFactory;
import java.io.Serializable;
import java.util.Map;

// @Transactional(Transactional.TxType.NOT_SUPPORTED)
public abstract class AbstractService<Id extends Serializable, T extends AbstractEntity<Id>> implements BaseService<Id, T> {

    @Inject
    protected ManagedExecutor managedExecutor;

    @Inject
    protected ValidatorFactory validatorFactory;

    @Inject
    protected Vertx vertx;

    // @Inject
    // protected Mutiny.SessionFactory sessionFactory;

    @Override
    public abstract BasePersistence<Id, T> getPersistence();

    @Override
    public abstract BaseReactivePersistence<Id, T> getReactivePersistence();

    @Override
    public Uni<T> getById(SessionHolder sessionHolder, Id id, Map<String, Object> extras) throws RuntimeException, Exception {
        // return Uni.createFrom().item(getPersistence().selectById(id));
        var transSession = (Mutiny.StatelessSession) extras.get(TRANS_STATELESS_SESSION);
        return getReactivePersistence().selectById(transSession, id);
    }

    @Override
    public Uni<Integer> add(SessionHolder sessionHolder, T obj, Map<String, Object> extras) throws RuntimeException, Exception {
        // return Uni.createFrom().item(getPersistence().insert(obj));
        var transSession = (Mutiny.StatelessSession) extras.get(TRANS_STATELESS_SESSION);
        return getReactivePersistence().insert(transSession, obj);
    }

    @Override
    public Uni<Integer> edit(SessionHolder sessionHolder, T obj, Map<String, Object> extras) throws RuntimeException, Exception {
        var transSession = (Mutiny.StatelessSession) extras.get(TRANS_STATELESS_SESSION);
        return getReactivePersistence().update(transSession, obj);
    }

    @Override
    public Uni<Integer> remove(SessionHolder sessionHolder, Id id, Map<String, Object> extras) throws RuntimeException, Exception {
        // return Uni.createFrom().item(getPersistence().delete(id));
        var transSession = (Mutiny.StatelessSession) extras.get(TRANS_STATELESS_SESSION);
        return getReactivePersistence().delete(transSession, id);
    }
}
