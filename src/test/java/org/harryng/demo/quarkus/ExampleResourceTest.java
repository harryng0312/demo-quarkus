package org.harryng.demo.quarkus;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ExampleResourceTest {

    static Logger logger = LoggerFactory.getLogger(ExampleResourceTest.class);

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(is("Hello RESTEasy"));
        logger.info("test hello");
    }

    @Test
    public void testStringUni() {
        logger.info("test uni");
        Uni.createFrom().item("string value")
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
            .subscribe().with(
                val -> {
                    var nowTime = LocalDateTime.now();
                    logger.info("FINISHED AT: " + nowTime);
                    logger.info("sub: " + val);
                },
                err -> logger.error("sub err: ", err)
            );
    }

    @Test
    public void testStringMulti() {
        logger.info("test multi");
        var values = new String[]{"0", "1", "3", "4", "5"};
        Multi.createFrom()
                .emitter(emitter -> {
                    Arrays.stream(values).forEach(itm -> {
                        try {
                            TimeUnit.SECONDS.sleep(2);
                            emitter.emit(itm);
//                            Log.info("emit: " + itm);
                        } catch (InterruptedException e) {
                            logger.error("", e);
                        }
                    });
                })
                .subscribe().with(
                        item -> logger.info("Item val:" + item),
                        failure -> logger.error("Failed with " + failure),
                        () -> logger.info("Completed"));
    }

    @Test
    public void testStringUniDelayed() {
        logger.info("test uni delayed");
        Uni.createFrom().item("string value")
            .map(val -> {
                var startTime = LocalDateTime.now();
                logger.info("START AT: " + startTime);
                logger.info(val);
                return val + " after 1";
            })
            .map(Unchecked.function(val -> {
                var startTime = LocalDateTime.now();
                logger.info("start1 at: " + startTime);
                // Thread.sleep(2 * 1_000);
                // var finishedTime = LocalDateTime.now();
                // logger.info("duration time 1: " + (Duration.between(startTime, finishedTime)));
                return val;
            }))
            .onItem().delayIt().by(Duration.ofSeconds(2))
            // .subscribe().with(
            //     val -> {
            //         var nowTime = LocalDateTime.now();
            //         logger.info("FINISHED AT: " + nowTime);
            //         logger.info("sub: " + val);
            //     },
            //     err -> logger.error("sub err: ", err)
            // );
            .map(val -> {
                var finishedTime = LocalDateTime.now();
                logger.info("finished1 at: " + finishedTime);
                return val;
            })
            .map(Unchecked.function(val -> {
                var startTime = LocalDateTime.now();
                logger.info("start2 at: " + startTime);
                // Thread.sleep(3 * 1_000);
                // var finishedTime = LocalDateTime.now();
                // logger.info("duration time 2: " + (Duration.between(startTime, finishedTime)));
                return val;
            }))
            .onItem().delayIt().by(Duration.ofSeconds(3))
            .map(val -> {
                var finishedTime = LocalDateTime.now();
                logger.info("finished2 at: " + finishedTime);
                return val;
            })
            .subscribe().with(
                val -> {
                    var nowTime = LocalDateTime.now();
                    logger.info("FINISHED AT: " + nowTime);
                    logger.info("sub: " + val);
                },
                err -> logger.error("sub err: ", err)
            );

            // .map(val -> {
            //     var nowTime = LocalDateTime.now();
            //     logger.info("FINISHED AT: " + nowTime);
            //     logger.info("sub: " + val);
            //     return val;
            // })
            // .await().indefinitely();
    }

    @Test
    public void testDurationNonBlock() {
        logger.info("start testing duration block");
        given()
            .when().get("/test-duration-nonblock")
            .then()
            .statusCode(200)
            .body(is("OK"));
    }

    @Test
    public void testDuration2NonBlock() {
        logger.info("start testing duration block");
        given()
            .when().get("/test-duration2-nonblock")
            .then()
            .statusCode(200)
            .body(is("OK"));
    }

}