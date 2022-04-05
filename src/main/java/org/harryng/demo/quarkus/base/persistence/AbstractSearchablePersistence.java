package org.harryng.demo.quarkus.base.persistence;

import org.harryng.demo.quarkus.base.entity.BaseEntity;
import org.harryng.demo.quarkus.util.page.Page;
import org.harryng.demo.quarkus.util.page.PageInfo;
import org.harryng.demo.quarkus.util.persistence.PersistenceUtil;

import javax.persistence.LockModeType;
import java.io.Serializable;
import java.util.Map;

public abstract class AbstractSearchablePersistence<Id extends Serializable, T extends BaseEntity<Id>>
        extends AbstractPersistence<Id, T> implements BaseSearchablePersistence<Id, T> {

    protected AbstractSearchablePersistence(Class<T> entityClass) {
        super(entityClass);
    }

    public long countByConditions(
            String countJpql,
            Map<String, Serializable> params
    ) throws RuntimeException, Exception {
        return PersistenceUtil.countObjectByQuery(
            getEntityManager(),
            countJpql,
            params
        );
    }

    public Page<T> selectByConditions(
        String queryJpql,
        Map<String, Serializable> params,
        PageInfo pageInfo,
        long total
    ) throws RuntimeException, Exception {
        return PersistenceUtil.selectObjectByQuery(
            getEntityManager(),
            getEntityClass(),
            queryJpql,
            params,
            pageInfo,
            total,
            LockModeType.NONE
        );
    }
}