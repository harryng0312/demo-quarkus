package org.harryng.demo.quarkus.base.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.hibernate.reactive.mutiny.Mutiny;

import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.RoutingContext;

import javax.inject.Inject;
import javax.ws.rs.core.Request;

public abstract class AbstractController {

    @Inject
    Vertx vertx;
    @Inject
    ObjectMapper objectMapper;
    @Inject
    HttpServerRequest serverRequest;
    @Inject
    HttpServerResponse serverResponse;
    // @Inject
    // protected RoutingContext routingContext;

    @Inject
    protected Mutiny.SessionFactory sessionFactory;

    @Inject
    protected Uni<Mutiny.Session> transSession;

    public HttpServerRequest getServerRequest() {
        return serverRequest;
    }

    public HttpServerResponse getServerResponse() {
        return serverResponse;
    }

    // public RoutingContext getRoutingContext(){
        // return routingContext;
    // }

    public Vertx getVertx() {
        return vertx;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
