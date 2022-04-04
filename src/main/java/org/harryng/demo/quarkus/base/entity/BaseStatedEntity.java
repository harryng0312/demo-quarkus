package org.harryng.demo.quarkus.base.entity;

import java.time.LocalDateTime;

public interface BaseStatedEntity {
    public LocalDateTime getCreatedDate();
    public void setCreatedDate(LocalDateTime createdDate);
    public LocalDateTime getModifiedDate();
    public void setModifiedDate(LocalDateTime modifiedDate);
    public String getStatus();
    public void setStatus(String status);
}
