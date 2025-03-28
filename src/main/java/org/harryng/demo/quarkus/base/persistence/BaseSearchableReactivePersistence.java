package org.harryng.demo.quarkus.base.persistence;

import io.smallrye.mutiny.Uni;
import org.harryng.demo.quarkus.base.entity.BaseEntity;
import org.harryng.demo.quarkus.util.page.Page;
import org.harryng.demo.quarkus.util.page.PageInfo;
import org.hibernate.reactive.mutiny.Mutiny;

import java.io.Serializable;
import java.util.Map;

public interface BaseSearchableReactivePersistence<Id extends Serializable, T extends BaseEntity<Id>> extends BaseReactivePersistence<Id, T> {

    public Uni<Long> countByConditions(
            Mutiny.StatelessSession session,
            String countJpql,
            Map<String, Serializable> params
    ) throws RuntimeException, Exception;

    public Uni<Page<T>> selectByConditions(
            Mutiny.StatelessSession session,
            String queryJpql,
            Map<String, Serializable> params,
            PageInfo pageInfo,
            long total
    ) throws RuntimeException, Exception;
}