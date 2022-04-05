package org.harryng.demo.quarkus.base.service;

import org.harryng.demo.quarkus.base.entity.BaseEntity;
import org.harryng.demo.quarkus.base.persistence.BaseSearchablePersistence;
import org.harryng.demo.quarkus.util.SessionHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.Map;

public abstract class AbstractSearchableService<Id extends Serializable, T extends BaseEntity<Id>>
        extends AbstractService<Id, T> implements BaseSearchableService<Id, T> {

    @Override
    public BaseSearchablePersistence<Id, T> getPersistence() {
        return (BaseSearchablePersistence<Id, T>) super.getPersistence();
    }

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
            Pageable pageInfo,
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
