package org.harryng.demo.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.stream.IntStream;

@QuarkusTest
public class TestVertx {
    static Logger logger = LoggerFactory.getLogger(TestVertx.class);
    @Inject
    protected Vertx vertx;

    @Test
    public void transformUniToMulti() {
        var filePath = "./setup/jmeter/data/users.csv";
        vertx.fileSystem().exists(filePath)
                .flatMap(existed -> {
                    logger.info("Existed: " + existed);
                    return Uni.createFrom().voidItem();
                })
                .onItem().transformToMulti(v -> Multi.createFrom().emitter(multiEmitter -> {
                    IntStream.range(0, 10).forEach(multiEmitter::emit);
                }))
                .concatMap(itm -> {
                    logger.info("index concat: " + itm);
                    return Multi.createFrom().item(itm);
                })
//                .invoke(itm -> {
//                    logger.info("index: " + itm);
//                })
                .collect().last()
                .onItemOrFailure().invoke((itm, thr) -> {
                    logger.info("Finished..." + itm);
                })
                .subscribe().with(v -> logger.info("Finished!"));
    }
}
