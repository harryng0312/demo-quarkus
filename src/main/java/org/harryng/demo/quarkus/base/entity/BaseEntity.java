package org.harryng.demo.quarkus.base.entity;

import java.io.Serializable;

public interface BaseEntity<Id extends Serializable> extends Serializable {
    public Id getId();
    public void setId(Id id);
}
