package org.harryng.demo.quarkus.base.service;

import org.harryng.demo.quarkus.base.entity.BaseEntity;
import org.harryng.demo.quarkus.util.SessionHolder;
import org.harryng.demo.quarkus.util.page.Page;
import org.harryng.demo.quarkus.util.page.PageInfo;

import java.io.Serializable;
import java.util.Map;

public interface BaseSearchableService<Id extends Serializable, T extends BaseEntity<Id>> extends BaseService<Id, T> {

    public long findByConditions(
            SessionHolder session,
            String countJpql,
            Map<String, Serializable> params,
            Map<String, Serializable> extras
    ) throws RuntimeException, Exception;

    public Page<T> findByConditions(
            SessionHolder session,
            String queryJpql,
            Map<String, Serializable> params,
            PageInfo pageInfo,
            long total,
            Map<String, Serializable> extras
    ) throws RuntimeException, Exception;
}
