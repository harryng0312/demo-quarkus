package org.harryng.demo.quarkus.base.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public interface BaseStatedEntity<Id extends Serializable> extends BaseEntity<Id> {
    public LocalDateTime getCreatedDate();
    public void setCreatedDate(LocalDateTime createdDate);
    public LocalDateTime getModifiedDate();
    public void setModifiedDate(LocalDateTime modifiedDate);
    public String getStatus();
    public void setStatus(String status);
}
