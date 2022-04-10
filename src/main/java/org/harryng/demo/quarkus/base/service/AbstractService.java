package org.harryng.demo.quarkus.base.service;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;

import org.eclipse.microprofile.context.ManagedExecutor;
import org.harryng.demo.quarkus.base.entity.AbstractEntity;
import org.harryng.demo.quarkus.base.persistence.BasePersistence;
import org.harryng.demo.quarkus.base.persistence.BaseReactivePersistence;
import org.harryng.demo.quarkus.util.SessionHolder;
import org.hibernate.reactive.mutiny.Mutiny;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Map;

// @Transactional(Transactional.TxType.NOT_SUPPORTED)
public abstract class AbstractService<Id extends Serializable, T extends AbstractEntity<Id>> implements BaseService<Id, T> {

    @Inject
    protected ManagedExecutor managedExecutor;

    // @Inject
    // protected Mutiny.SessionFactory sessionFactory;

    @Override
    public abstract BasePersistence<Id, T> getPersistence();

    @Override
    public abstract BaseReactivePersistence<Id, T> getReactivePersistence();

    @Override
    public Uni<T> getById(SessionHolder sessionHolder, Id id, Map<String, Object> extras) throws RuntimeException, Exception {
        // return Uni.createFrom().item(getPersistence().selectById(id));
        Mutiny.Session transSession = (Mutiny.Session) extras.get("transSession");
        return getReactivePersistence().selectById(transSession, id);
    }

    @Override
    public Uni<Integer> add(SessionHolder sessionHolder, T obj, Map<String, Object> extras) throws RuntimeException, Exception {
        // return Uni.createFrom().item(getPersistence().insert(obj));
        Mutiny.Session transSession = (Mutiny.Session) extras.get(TRANS_SESSION);
        return getReactivePersistence().insert(transSession, obj);
    }

    @Override
    public Uni<Integer> edit(SessionHolder sessionHolder, T obj, Map<String, Object> extras) throws RuntimeException, Exception {
        Mutiny.Session transSession = (Mutiny.Session) extras.get(TRANS_SESSION);
        return getReactivePersistence().update(transSession, obj);
    }

    @Override
    public Uni<Integer> remove(SessionHolder sessionHolder, Id id, Map<String, Object> extras) throws RuntimeException, Exception {
        // return Uni.createFrom().item(getPersistence().delete(id));
        Mutiny.Session transSession = (Mutiny.Session) extras.get(TRANS_SESSION);
        return getReactivePersistence().delete(transSession, id);
    }
}
