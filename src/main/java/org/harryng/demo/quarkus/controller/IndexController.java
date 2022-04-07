package org.harryng.demo.quarkus.controller;

import io.quarkus.logging.Log;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class IndexController {

    @Location("pages/index.html")
    protected Template indexTempl;

    @PostConstruct
    public void init() {
        Log.info("Index Controller init");
    }

    @PreDestroy
    public void destroy() {
        Log.info("Index Controller destroy");
    }

    @GET
    @Path("/index")
    @Produces({MediaType.TEXT_HTML})
    public TemplateInstance goIndex() {
        Log.info("Datasource: ");
        return indexTempl.instance();
    }
}
