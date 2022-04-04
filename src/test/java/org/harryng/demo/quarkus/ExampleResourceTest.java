package org.harryng.demo.quarkus;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ExampleResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(is("Hello RESTEasy"));
        Log.info("test hello");
    }

    @Test
    public void testStringUni() {
        Log.info("test uni");
        Uni.createFrom().item("string value")
            .onItem().transform(val -> {
                Log.info(val);
                return val + " after 1";
            })
            .onItem().transform(val -> {
                Log.info(val);
                return val;
            })
            .subscribe().with(val -> Log.info(val));
    }

    @Test
    public void testStringMulti() {
        Log.info("test multi");
        var values = new String[]{"0", "1", "3", "4", "5"};
        Multi.createFrom()
            .emitter(emitter -> {
                Arrays.stream(values).forEach(itm -> {
                    try {
                        Thread.sleep(2 * 1_000);
                        emitter.emit(itm);
//                            Log.info("emit: " + itm);
                    } catch (InterruptedException e) {
                        Log.error("", e);
                    }
                });
            })
            .subscribe().with(
                    item -> Log.info("Item val:" + item),
                    failure -> Log.error("Failed with " + failure),
                    () -> Log.info("Completed"));
    }
}