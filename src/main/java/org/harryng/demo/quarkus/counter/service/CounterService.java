package org.harryng.demo.quarkus.counter.service;

import org.harryng.demo.quarkus.counter.entity.CounterImpl;

public interface CounterService{
    CounterImpl insert(String id, long initValue) throws RuntimeException, Exception;

    long increment(String id, int step) throws RuntimeException, Exception;

    long currentValue(String id) throws RuntimeException, Exception;
}