package org.harryng.demo.quarkus.base.persistence;

import java.io.Serializable;
import java.util.Map;

import org.harryng.demo.quarkus.base.entity.BaseEntity;
import org.harryng.demo.quarkus.util.page.Page;
import org.harryng.demo.quarkus.util.page.PageInfo;
import org.hibernate.reactive.mutiny.Mutiny;

import io.smallrye.mutiny.Uni;

public abstract class AbstractSearchableReactivePersistence<Id extends Serializable, T extends BaseEntity<Id>>
        extends AbstractReactivePersistence<Id, T> implements BaseSearchableReactivePersistence<Id, T> {

    protected AbstractSearchableReactivePersistence(Class<T> entityClass) {
        super(entityClass);
    }

    //    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Uni<Long> countByConditions(
        Mutiny.StatelessSession session,
        String countJpql,
        Map<String, Serializable> params
    ) throws RuntimeException, Exception {
//        return PersistenceUtil.countObjectByQuery(
//                getEntityManager(),
//                countJpql,
//                params
//        );
        return Uni.createFrom().nothing();
    }

    //    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public Uni<Page<T>> selectByConditions(
        Mutiny.StatelessSession session,
        String queryJpql,
        Map<String, Serializable> params,
        PageInfo pageInfo,
        long total
    ) throws RuntimeException, Exception {
//        return PersistenceUtil.selectObjectByQuery(
//                getEntityManager(),
//                getEntityClass(),
//                queryJpql,
//                params,
//                pageInfo,
//                total,
//                LockModeType.NONE
//        );
        return Uni.createFrom().nothing();
    }
}