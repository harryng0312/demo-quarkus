package org.harryng.demo.quarkus.base.persistence;

import io.quarkus.hibernate.orm.PersistenceUnit;
import org.harryng.demo.quarkus.base.entity.BaseEntity;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.Serializable;

public abstract class AbstractPersistence<Id extends Serializable, T extends BaseEntity<Id>>
        implements BasePersistence<Id, T> {

    private Class<T> entityClass;

//    @PersistenceUnit("primary_pu")
    protected EntityManager defaultEntityManager;

    public EntityManager getEntityManager() {
        return defaultEntityManager;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    protected AbstractPersistence(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public T selectById(Id id) throws RuntimeException, Exception {
        return getEntityManager().find(getEntityClass(), id);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public int insert(T obj) throws RuntimeException, Exception {
        getEntityManager().persist(obj);
        return 1;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public int update(T obj) throws RuntimeException, Exception {
        getEntityManager().merge(obj);
        return 1;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public int delete(Id id) throws RuntimeException, Exception {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteriaDelete = cb.createCriteriaDelete(getEntityClass());
        var root = criteriaDelete.from(getEntityClass());
        criteriaDelete.where(cb.equal(root.get("id"), id));
        var query = getEntityManager().createQuery(criteriaDelete);
        return query.executeUpdate();
    }
}
