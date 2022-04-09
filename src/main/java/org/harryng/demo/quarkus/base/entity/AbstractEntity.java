package org.harryng.demo.quarkus.base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractEntity<Tid extends Serializable> implements BaseEntity<Tid>{
    private Tid id;

    protected AbstractEntity(){}

    protected AbstractEntity(Tid id){
        this.id = id;
    }

    @Override
    @Id
    @Column(name = "id_")
    public Tid getId() {
        return id;
    }

    @Override
    public void setId(Tid id) {
        this.id = id;
    }
}
