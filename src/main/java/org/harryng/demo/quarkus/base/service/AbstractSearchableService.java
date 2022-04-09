package org.harryng.demo.quarkus.base.service;

import org.harryng.demo.quarkus.base.entity.AbstractEntity;
import org.harryng.demo.quarkus.base.persistence.BaseSearchablePersistence;
import org.harryng.demo.quarkus.base.persistence.BaseSearchableReactivePersistence;
import org.harryng.demo.quarkus.util.SessionHolder;
import org.harryng.demo.quarkus.util.page.Page;
import org.harryng.demo.quarkus.util.page.PageInfo;

import io.smallrye.mutiny.Uni;

import java.io.Serializable;
import java.util.Map;

// @Transactional(Transactional.TxType.NOT_SUPPORTED)
public abstract class AbstractSearchableService<Id extends Serializable, T extends AbstractEntity<Id>>
        extends AbstractService<Id, T> implements BaseSearchableService<Id, T> {

    @Override
    public abstract BaseSearchablePersistence<Id, T> getPersistence();

    @Override
    public abstract BaseSearchableReactivePersistence<Id, T> getReactivePersistence();

    @Override
    public Uni<Long> findByConditions(
            SessionHolder sessionHolder,
            String countJpql,
            Map<String, Object> params,
            Map<String, Object> extras
    ) throws RuntimeException, Exception {
        return Uni.createFrom().nothing();
    }

    @Override
    public Uni<Page<T>> findByConditions(
            SessionHolder sessionHolder,
            String queryStr,
            Map<String, Object> params,
            PageInfo pageInfo,
            long total,
            Map<String, Object> extras
    ) throws RuntimeException, Exception {
        return Uni.createFrom().nothing();
    }
}
