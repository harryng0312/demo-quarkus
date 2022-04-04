package org.harryng.demo.quarkus.base.entity;


import java.io.Serializable;

public abstract class AbstractEntity<Id extends Serializable> implements BaseEntity<Id>{
    private Id id;

    protected AbstractEntity(){}

    protected AbstractEntity(Id id){
        this.id = id;
    }

    @Override
    public Id getId() {
        return id;
    }

    @Override
    public void setId(Id id) {
        this.id = id;
    }
}
