package org.harryng.demo.quarkus.base.mapper;

import org.harryng.demo.quarkus.base.entity.BaseEntity;

import java.io.Serializable;

public interface BaseMapper <Dto, Entity extends BaseEntity<? extends Serializable>> {
    Entity mapDtoToEntity(Dto dto);
    Dto mapEntityToDto(Entity entity);
}
