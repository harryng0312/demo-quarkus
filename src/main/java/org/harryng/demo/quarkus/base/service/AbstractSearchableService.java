package org.harryng.demo.quarkus.base.service;

import org.harryng.demo.quarkus.base.entity.BaseEntity;
import org.harryng.demo.quarkus.base.persistence.BaseSearchablePersistence;
import org.harryng.demo.quarkus.util.SessionHolder;
import org.harryng.demo.quarkus.util.page.Page;
import org.harryng.demo.quarkus.util.page.PageInfo;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Map;

@Transactional(Transactional.TxType.NOT_SUPPORTED)
public abstract class AbstractSearchableService<Id extends Serializable, T extends BaseEntity<Id>>
        extends AbstractService<Id, T> implements BaseSearchableService<Id, T> {

    @Override
    public abstract BaseSearchablePersistence<Id, T> getPersistence();

    @Override
    public long findByConditions(
            SessionHolder session,
            String countJpql,
            Map<String, Serializable> params,
            Map<String, Serializable> extras
    ) throws RuntimeException, Exception {
        return getPersistence().countByConditions(
                countJpql,
                params
        );
    }

    @Override
    public Page<T> findByConditions(
            SessionHolder session,
            String queryStr,
            Map<String, Serializable> params,
            PageInfo pageInfo,
            long total,
            Map<String, Serializable> extras
    ) throws RuntimeException, Exception {
        return getPersistence().selectByConditions(
                queryStr,
                params,
                pageInfo,
                total
        );
    }
}
