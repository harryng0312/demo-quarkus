package org.harryng.demo.quarkus.user.persistence;

import org.harryng.demo.quarkus.base.persistence.BaseSearchablePersistence;
import org.harryng.demo.quarkus.user.entity.UserImpl;

public interface UserPersistence extends BaseSearchablePersistence<Long, UserImpl>{}
