package org.harryng.demo.quarkus.user.persistence;


import org.harryng.demo.quarkus.base.persistence.AbstractSearchablePersistence;
import org.harryng.demo.quarkus.user.entity.UserImpl;

import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Named("userPersistence")
public class UserPersistenceImpl extends AbstractSearchablePersistence<Long, UserImpl> implements UserPersistence {
    protected UserPersistenceImpl() {
        super(UserImpl.class);
    }
}
