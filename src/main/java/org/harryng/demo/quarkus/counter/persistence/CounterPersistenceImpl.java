package org.harryng.demo.quarkus.counter.persistence;

import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheManager;
import io.quarkus.hibernate.orm.PersistenceUnit;
import org.harryng.demo.quarkus.counter.entity.CounterImpl;
import org.harryng.demo.quarkus.kernel.counter.CounterPersistence;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.concurrent.atomic.AtomicLong;

@Repository("counterPersistence")
public class CounterPersistenceImpl implements CounterPersistence {

    protected static Object lock = new Object();

    @PersistenceUnit("primary_pu")
    private EntityManager defaultEntityManager;

    //    @Autowired
//    @Qualifier("localCacheManager")
    private CacheManager localCacheManager;
    protected Cache cache = localCacheManager.getCache("counter").isPresent() ?
            localCacheManager.getCache("counter").get() : null;

    private EntityManager getEntityManager() {
        return defaultEntityManager;
    }

    protected long updateCounter(String id) {
//        var value = value + step + CounterPersistence.DEFAULT_CACHE_STEP
        var cb = getEntityManager().getCriteriaBuilder();
        var selectCri = cb.createQuery(CounterImpl.class);
        var selectRoot = selectCri.from(CounterImpl.class);
        var updateCri = cb.createCriteriaUpdate(CounterImpl.class);
        var updateRoot = updateCri.from(CounterImpl.class);

        selectCri.where(cb.equal(selectRoot.get("id"), id));
        var selectQuery = getEntityManager().createQuery(selectCri);
        selectQuery.setLockMode(LockModeType.PESSIMISTIC_WRITE);
        var value = selectQuery.getResultList().get(0).getValue();
        var newValue = (value.get() + CounterPersistence.DEFAULT_CACHE_STEP);

        updateCri.set("value", newValue).where(cb.equal(updateRoot.get("id"), id));
        var updateQuery = getEntityManager().createQuery(updateCri);
        updateQuery.executeUpdate();
        return value.get();
    }

    public CounterImpl currentCounter(String id) {
        CounterImpl counter = cache.get(id, k -> (CounterImpl)null).await().indefinitely();
        if (counter == null) {
            // if not exits in cache - select from db
            counter = getEntityManager().find(CounterImpl.class, id, LockModeType.PESSIMISTIC_WRITE);
            counter.setMaxValue(counter.getValue().get() + CounterPersistence.DEFAULT_CACHE_STEP);
            counter = new CounterImpl(id, new AtomicLong(updateCounter(id)), counter.getMaxValue());
            // if not exits in db - create into db and cache
            if (counter == null) {
                counter = insert(id, DEFAULT_INIT_VALUE);
            }
            CounterImpl finalCounter = counter;
            counter = cache.get(id, k -> finalCounter).await().indefinitely();
        }
        return counter;
    }

    public long doIncrement(String id, int step) {
        var counter = currentCounter(id);
        if (counter.getValue().get() + step >= counter.getMaxValue()) {
//            counter.maxValue = counter.value + step + CounterPersistence.DEFAULT_CACHE_STEP
            counter.getValue().set(updateCounter(id));
            counter.setMaxValue(counter.getValue().get() + CounterPersistence.DEFAULT_CACHE_STEP);
//            counter.value -= step
            counter.getValue().getAndAdd(-step);
        }
        counter.getValue().getAndAdd(step);
        cache.get(id, k -> counter).await().indefinitely();
        return counter.getValue().get();
    }

    public CounterImpl insert(String id, long initValue) {
        var counter = new CounterImpl(id, new AtomicLong(initValue), initValue + DEFAULT_STEP);
        var currVal = counter.getValue().get();
        counter.getValue().set(counter.getMaxValue());
        getEntityManager().persist(counter);
        getEntityManager().flush();
        getEntityManager().detach(counter);
        counter.getValue().set(currVal);
        counter.setMaxValue(counter.getValue().get() + CounterPersistence.DEFAULT_CACHE_STEP);
        return counter;
    }

    public long increment(String id, int step) {
        long rs = 0;
        synchronized (lock) {
            rs = doIncrement(id, step);
        }
        return rs;
    }

    public long currentValue(String id) {
        long rs = 0;
        synchronized (lock) {
            rs = currentCounter(id).getValue().get();
        }
        return rs;
    }
}