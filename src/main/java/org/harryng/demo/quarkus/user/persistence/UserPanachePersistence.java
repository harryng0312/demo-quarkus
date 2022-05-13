package org.harryng.demo.quarkus.user.persistence;

import org.harryng.demo.quarkus.base.persistence.AbstractPanachePersistence;
import org.harryng.demo.quarkus.user.entity.UserImpl;

public interface UserPanachePersistence extends AbstractPanachePersistence<Long, UserImpl> {
}
