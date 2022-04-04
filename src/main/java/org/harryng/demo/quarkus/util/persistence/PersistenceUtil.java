package org.harryng.demo.quarkus.util.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

public class PersistenceUtil {
    public static long countObjectByQuery(
            EntityManager entityManager,
            String countJpql,
            Map<String, Serializable> params
    ) throws RuntimeException, Exception {
        TypedQuery<Long> typedQuery = entityManager.createQuery(countJpql, Long.class);
        if (params != null) {
            for (Map.Entry<String, ?> item : params.entrySet()) {
                typedQuery.setParameter(item.getKey(), item.getValue());
            }
        }
        return typedQuery.getResultList().size() > 0 ? typedQuery.getResultList().get(0) : 0L;
    }

    public static <T> Page<T> selectObjectByQuery(
            EntityManager entityManager,
            Class<T> returnType,
            String queryJpql,
            Map<String, Serializable> params,
            Pageable pageInfo,
            long total,
            LockModeType lockModeType
    ) throws RuntimeException, Exception {
        TypedQuery<T> typedQuery = entityManager.createQuery(queryJpql, returnType);
        typedQuery.setLockMode(lockModeType);
        typedQuery.setFirstResult(pageInfo.getPageNumber() * pageInfo.getPageSize());
        typedQuery.setMaxResults(pageInfo.getPageSize());
        for(Map.Entry<String, ?> item : params.entrySet()){
            typedQuery.setParameter(item.getKey(), item.getValue());
        }
        List<T> resultList = typedQuery.getResultList();
        long size = total >= 0 ? total : resultList.size();
        return new PageImpl(resultList, pageInfo, size);
    }
}