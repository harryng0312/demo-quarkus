package org.harryng.demo.quarkus.base.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class AbstractStatedEntity<Id extends Serializable> extends AbstractEntity<Id> implements BaseStatedEntity{
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String status;

    protected AbstractStatedEntity(){super();}

    protected AbstractStatedEntity(Id id, LocalDateTime createdDate, LocalDateTime modifiedDate, String status){
        super(id);
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.status = status;
    }

    @Override
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    @Override
    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }
}
