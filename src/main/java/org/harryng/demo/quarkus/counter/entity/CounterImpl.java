package org.harryng.demo.quarkus.counter.entity;

import java.util.concurrent.atomic.AtomicLong;

public class CounterImpl extends CounterModel {
    public CounterImpl() {
    }

    public CounterImpl(String id, AtomicLong value, long maxValue) {
        super(id, value, maxValue);
    }
}