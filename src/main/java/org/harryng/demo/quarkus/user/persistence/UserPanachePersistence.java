package org.harryng.demo.quarkus.user.persistence;

import org.harryng.demo.quarkus.base.persistence.AbstractPanachePersistence;
import org.harryng.demo.quarkus.user.entity.UserImpl;

import javax.inject.Singleton;

@Singleton
public class UserPanachePersistence implements AbstractPanachePersistence<Long, UserImpl> {

}
