package org.harryng.demo.quarkus.base.persistence;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import org.harryng.demo.quarkus.base.entity.BaseEntity;

import java.io.Serializable;

public interface AbstractPanachePersistence<Id extends Serializable, T extends BaseEntity<Id>> extends PanacheRepositoryBase<T, Id> {
}
