package org.harryng.demo.quarkus.user.persistence;


import org.harryng.demo.quarkus.base.persistence.AbstractSearchableReactivePersistence;
import org.harryng.demo.quarkus.user.entity.UserImpl;

import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Named("userReactivePersistence")
public class UserReactivePersistenceImpl extends AbstractSearchableReactivePersistence<Long, UserImpl> implements UserReactivePersistence {
    protected UserReactivePersistenceImpl() {
        super(UserImpl.class);
    }
}
