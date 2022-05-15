package org.harryng.demo.quarkus.base.mapper;

import org.harryng.demo.quarkus.base.entity.BaseEntity;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.io.Serializable;

public interface BaseMapper <Dto, Entity extends BaseEntity<? extends Serializable>> {
    Entity mapDtoToEntity(Dto dto);
    Dto mapEntityToDto(Entity entity);
    @Mapping(target = "id", ignore = true)
    void populateEntity(Entity src, @MappingTarget Entity dest);
}
