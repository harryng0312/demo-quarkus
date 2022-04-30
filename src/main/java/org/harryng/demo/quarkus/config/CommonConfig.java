package org.harryng.demo.quarkus.config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.hibernate.reactive.mutiny.Mutiny;

import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class CommonConfig {

    @Inject
    public Mutiny.SessionFactory sessionFactory;

//    @Produces
//    @Default
//    public Uni<Mutiny.Session> getTransactionSession(){
//        return sessionFactory.openSession();
//    }

}
