package org.harryng.demo.quarkus;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;

@QuarkusIntegrationTest
public class ExampleResourceIT extends ExampleResourceTest {

    // Execute the same tests but in native mode.
    @Test
    public void testStringUni() {
        Log.info("test uni in native");
        Uni.createFrom().item("string value")
                .onItem().transform(val -> {
                    Log.info(val);
                    return val + " after 1";
                })
                .onItem().transform(val -> {
                    Log.info(val);
                    return val;
                })
                .subscribe().with(item -> Log.info(item));
    }

}
