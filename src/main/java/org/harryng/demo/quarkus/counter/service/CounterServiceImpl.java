package org.harryng.demo.quarkus.counter.service;

import org.harryng.demo.quarkus.counter.entity.CounterImpl;
import org.harryng.demo.quarkus.counter.kernel.counter.CounterPersistence;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Named("counterService")
public class CounterServiceImpl implements CounterService {

//    @Autowired
//    @Qualifier("counterPersistence")
//    @Qualifier("counterLockerPersistence")
    @Inject
    @Named("counterPersistence")
    protected CounterPersistence persistence;

    public CounterImpl insert(String id, long initValue) throws RuntimeException, Exception {
        return persistence.insert(id, CounterPersistence.DEFAULT_INIT_VALUE);
    }

    public long increment(String id, int step) throws RuntimeException, Exception {
        return persistence.increment(id, step);
    }

    public long currentValue(String id) throws RuntimeException, Exception {
        return persistence.currentValue(id);
    }
}