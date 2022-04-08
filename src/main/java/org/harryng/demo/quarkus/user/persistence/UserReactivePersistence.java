package org.harryng.demo.quarkus.user.persistence;

import org.harryng.demo.quarkus.base.persistence.BaseSearchableReactivePersistence;
import org.harryng.demo.quarkus.user.entity.UserImpl;

public interface UserReactivePersistence extends BaseSearchableReactivePersistence<Long, UserImpl> {}
