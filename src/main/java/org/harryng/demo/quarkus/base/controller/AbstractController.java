package org.harryng.demo.quarkus.base.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.mutiny.core.Vertx;

import javax.inject.Inject;

public abstract class AbstractController {

    @Inject
    HttpServerRequest request;
    @Inject
    HttpServerResponse response;
    @Inject
    Vertx vertx;
    @Inject
    ObjectMapper objectMapper;

    public HttpServerRequest getRequest() {
        return request;
    }

    public HttpServerResponse getResponse() {
        return response;
    }

    public Vertx getVertx() {
        return vertx;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
