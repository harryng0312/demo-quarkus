package org.harryng.demo.quarkus.counter.kernel.counter;

import org.harryng.demo.quarkus.counter.entity.CounterImpl;

public interface CounterPersistence {
    public int DEFAULT_STEP = 1;
    public long DEFAULT_INIT_VALUE = 1L;
    public int DEFAULT_CACHE_STEP = 20;

    public long doIncrement(String id, int step) throws RuntimeException;
    public CounterImpl currentCounter(String id) throws RuntimeException;

    public CounterImpl insert(String id, long initValue) throws RuntimeException;
    public long increment(String id, int step) throws RuntimeException;
    public long currentValue(String id) throws RuntimeException;
}