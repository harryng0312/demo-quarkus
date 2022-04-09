package org.harryng.demo.quarkus.base.service;

import org.harryng.demo.quarkus.base.entity.BaseEntity;
import org.harryng.demo.quarkus.util.SessionHolder;
import org.harryng.demo.quarkus.util.page.Page;
import org.harryng.demo.quarkus.util.page.PageInfo;

import io.smallrye.mutiny.Uni;

import java.io.Serializable;
import java.util.Map;

public interface BaseSearchableService<Id extends Serializable, T extends BaseEntity<Id>> extends BaseService<Id, T> {

    public Uni<Long> findByConditions(
            SessionHolder sessionHolder,
            String countJpql,
            Map<String, Object> params,
            Map<String, Object> extras
    ) throws RuntimeException, Exception;

    public Uni<Page<T>> findByConditions(
            SessionHolder sessionHolder,
            String queryJpql,
            Map<String, Object> params,
            PageInfo pageInfo,
            long total,
            Map<String, Object> extras
    ) throws RuntimeException, Exception;
}
