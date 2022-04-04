package org.harryng.demo.quarkus.base.persistence;

import org.harryng.demo.quarkus.base.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.Map;

public interface BaseSearchablePersistence<Id extends Serializable, T extends BaseEntity<Id>> extends BasePersistence<Id, T> {

    public long countByConditions(
            String countJpql,
            Map<String, Serializable> params
    ) throws RuntimeException, Exception;

    public Page<T> selectByConditions(
            String queryJpql,
            Map<String, Serializable> params,
            Pageable pageInfo,
            long total
    ) throws RuntimeException, Exception;
}