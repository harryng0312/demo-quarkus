package org.harryng.demo.quarkus.base.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractStatedEntity<Id extends Serializable> extends AbstractEntity<Id> implements BaseStatedEntity<Id>{
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
    @Basic
    @Column(name = "created_date")
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    @Basic
    @Column(name = "modified_date")
    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    @Override
    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }
}
