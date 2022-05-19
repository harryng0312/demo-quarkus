package org.harryng.demo.quarkus.base.persistence;

import org.harryng.demo.quarkus.base.entity.BaseEntity;
import org.harryng.demo.quarkus.util.page.PagedResult;
import org.harryng.demo.quarkus.util.page.PageInfo;

import java.io.Serializable;
import java.util.Map;

public interface BaseSearchablePersistence<Id extends Serializable, T extends BaseEntity<Id>> extends BasePersistence<Id, T> {

//    long countByConditions(
//            String countJpql,
//            Map<String, Serializable> params
//    ) throws RuntimeException, Exception;
//
//    PagedResult<T> selectByConditions(
//            String queryJpql,
//            Map<String, Serializable> params,
//            PageInfo pageInfo,
//            long total
//    ) throws RuntimeException, Exception;
}