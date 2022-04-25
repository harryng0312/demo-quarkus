package org.harryng.demo.quarkus.controller;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Duration;
import java.time.LocalDateTime;

@Path("/")
public class IndexController {

    static Logger logger = LoggerFactory.getLogger(IndexController.class);
    @Location("pages/index.html")
    protected Template indexTempl;

    @PostConstruct
    public void init() {
        logger.info("Index Controller init");
    }

    @PreDestroy
    public void destroy() {
        logger.info("Index Controller destroy");
    }

    @GET
    @Path("/index")
    @Produces({MediaType.TEXT_HTML})
    public TemplateInstance goIndex() {
        logger.info("Datasource: ");
        return indexTempl.instance();
    }

    @GET
    @Path("/test-duration-nonblock")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> testDuration() {
        // var res = Response.status(Status.INTERNAL_SERVER_ERROR).build();
        logger.info("test uni");
        return Uni.createFrom().item("string value")
                .map(val -> {
                    var startTime = LocalDateTime.now();
                    logger.info("START AT: " + startTime);
                    logger.info(val);
                    return val + " after 1";
                })
                .map(Unchecked.function(val -> {
                    var startTime = LocalDateTime.now();
                    logger.info("start1 at: " + startTime);
                    Thread.sleep(2 * 1_000);
                    var finishedTime = LocalDateTime.now();
                    logger.info("duration time 1: " + (Duration.between(startTime, finishedTime)));
                    return val;
                }))
                .map(Unchecked.function(val -> {
                    var startTime = LocalDateTime.now();
                    logger.info("start2 at: " + startTime);
                    Thread.sleep(3 * 1_000);
                    var finishedTime = LocalDateTime.now();
                    logger.info("duration time 2: " + (Duration.between(startTime, finishedTime)));
                    return val;
                }))
                // .onItem().delayIt().by(Duration.ofMillis(100))
                // .map(Unchecked.function(val -> {
                //     var finishedTime = LocalDateTime.now();
                //     logger.info("finished at:" + finishedTime);
                //     return val;
                // }))
                .map(Unchecked.function(val -> {
                    var nowTime = LocalDateTime.now();
                    logger.info("FINISHED AT: " + nowTime);
                    logger.info("sub: " + val);
                    return Response.ok("OK").build();
                }))
                .onFailure().transform(Unchecked.function(err -> {
                    logger.error("sub err: ", err);
                    return new WebApplicationException(err);
                }));
        // .subscribe().with(
        //     val -> {
        //         var nowTime = LocalDateTime.now();
        //         logger.info("FINISHED AT: " + nowTime);
        //         logger.info("sub: " + val);
        //     },
        //     err -> logger.error("sub err: ", err)
        // );
        // return Uni.createFrom().item(res);
    }

    @GET
    @Path("/test-duration2-nonblock")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> testDuration2() {
        logger.info("test uni2");
        return Uni.createFrom().item("string value")
                .map(val -> {
                    var startTime = LocalDateTime.now();
                    logger.info("START AT: " + startTime);
                    logger.info(val);
                    return val + " after 1";
                })
                .map(Unchecked.function(val -> {
                    var startTime = LocalDateTime.now();
                    logger.info("start1 at: " + startTime);
                    return val;
                }))
                .onItem().delayIt().by(Duration.ofSeconds(2))
                .map(val -> {
                    var finishedTime = LocalDateTime.now();
                    logger.info("finished1 at: " + finishedTime);
                    return val;
                })
                .map(Unchecked.function(val -> {
                    var startTime = LocalDateTime.now();
                    logger.info("start2 at: " + startTime);
                    return val;
                }))
                .onItem().delayIt().by(Duration.ofSeconds(3))
                .map(val -> {
                    var finishedTime = LocalDateTime.now();
                    logger.info("finished2 at: " + finishedTime);
                    return val;
                })
                .map(Unchecked.function(val -> {
                    var nowTime = LocalDateTime.now();
                    logger.info("FINISHED AT: " + nowTime);
                    logger.info("sub: " + val);
                    return Response.ok("OK").build();
                }))
                .onFailure().transform(Unchecked.function(err -> {
                    logger.error("sub err: ", err);
                    return new WebApplicationException(err);
                }));
    }

}
