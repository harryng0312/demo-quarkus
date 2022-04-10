package org.harryng.demo.quarkus.base.persistence;

import java.io.Serializable;

import javax.inject.Inject;

import org.harryng.demo.quarkus.base.entity.BaseEntity;
import org.hibernate.reactive.mutiny.Mutiny;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;

public abstract class AbstractReactivePersistence<Id extends Serializable, T extends BaseEntity<Id>>
        implements BaseReactivePersistence<Id, T> {

    protected static Logger logger = LoggerFactory.getLogger(AbstractReactivePersistence.class);

    private Class<T> entityClass;
    
    @Inject
    Mutiny.SessionFactory sessionFactory;
//    @Inject
//    Uni<Mutiny.Session> session;

    public Class<T> getEntityClass() {
        return entityClass;
    }

//    @Override
//    public Uni<Mutiny.Session> getSession() {
//        return session;
//    }

    protected AbstractReactivePersistence(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    //    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Uni<T> selectById(Mutiny.Session session, Id id) throws RuntimeException, Exception {
        return session.find(getEntityClass(), id);
    }

    //    @Transactional(Transactional.TxType.SUPPORTS)
    public Uni<Integer> insert(Mutiny.Session session, T obj) throws RuntimeException, Exception {
        return session.persist(obj).flatMap(item -> Uni.createFrom().item(1));
    }

    //    @Transactional(Transactional.TxType.SUPPORTS)
    public Uni<Integer> update(Mutiny.Session session, T obj) throws RuntimeException, Exception {
        // logger.info("persistence sessFact: " + sessionFactory.hashCode());
        // var cb = sessionFactory.getCriteriaBuilder();
        // var cQuery = cb.createQuery(Long.class);
        // var root = cQuery.from(getEntityClass());
        // cQuery.select(cb.count(root))
        //     .where(cb.equal(root.get("id"), obj.getId()));
        // var query = session.createQuery(cQuery);
        // return query.getSingleResult().flatMap(Unchecked.function(
        return Uni.createFrom().item(1).flatMap(Unchecked.function(
            count -> count > 0 ? 
                session.merge(obj).flatMap(
                    item -> item == null ?
                    Uni.createFrom().item(0) : Uni.createFrom().item(count.intValue())
                )
                : Uni.createFrom().item(count.intValue())
        ));
    }

    //    @Transactional(Transactional.TxType.SUPPORTS)
    public Uni<Integer> delete(Mutiny.Session session, Id id) throws RuntimeException, Exception {
        var cb = sessionFactory.getCriteriaBuilder();
        var criteriaDelete = cb.createCriteriaDelete(getEntityClass());
        var root = criteriaDelete.from(getEntityClass());
        criteriaDelete.where(cb.equal(root.get("id"), id));
        var query = session.createQuery(criteriaDelete);
        return query.executeUpdate();
    }
}
