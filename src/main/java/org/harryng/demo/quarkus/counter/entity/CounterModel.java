package org.harryng.demo.quarkus.counter.entity;

import org.harryng.demo.quarkus.base.entity.AbstractEntity;
import org.harryng.demo.quarkus.base.entity.BaseEntity;

import java.util.concurrent.atomic.AtomicLong;

public class CounterModel extends AbstractEntity<String> implements BaseEntity<String> {

    private AtomicLong value;
    private long maxValue;

    public CounterModel() {
    }

    public CounterModel(String id, AtomicLong value, long maxValue) {
        super(id);
    }

    public AtomicLong getValue() {
        return value;
    }

    public void setValue(AtomicLong value) {
        this.value = value;
    }

    public long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(long maxValue) {
        this.maxValue = maxValue;
    }
}