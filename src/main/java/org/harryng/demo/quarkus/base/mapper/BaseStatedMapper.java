package org.harryng.demo.quarkus.base.mapper;

import org.harryng.demo.quarkus.base.entity.BaseStatedEntity;

import java.io.Serializable;

public interface BaseStatedMapper<Dto, Entity extends BaseStatedEntity<? extends Serializable>>
        extends BaseMapper<Dto, Entity> {
}
