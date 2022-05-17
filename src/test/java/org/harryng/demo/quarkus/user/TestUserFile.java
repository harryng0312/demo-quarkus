package org.harryng.demo.quarkus.user;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import io.vertx.core.file.OpenOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.buffer.Buffer;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.stream.IntStream;

@QuarkusTest
public class TestUserFile {
    static Logger logger = LoggerFactory.getLogger(TestUserFile.class);
    @Inject
    protected Vertx vertx;

    @Test
    public void createUserFile() {
        var filePath = "./setup/jmeter/data/users.csv";
        int numberOfUser = 10;
        vertx.fileSystem().exists(filePath)
                .invoke(aBoolean -> {
                    if (!aBoolean) {
                        vertx.fileSystem().createFileAndForget(filePath);
                    } else {
                        vertx.fileSystem().deleteAndForget(filePath).createFileAndForget(filePath);
                    }
                })
                .flatMap(aBoolean -> vertx.fileSystem().open(filePath, new OpenOptions().setAppend(true)))
                .flatMap(asyncFile -> {
                    logger.info("creating ...");
                    var header = "user.id, user.createdDate, user.modifiedDate, user.status, user.username, " +
                            "user.password, user.screenName, user.dob, user.passwdEncryptedMethod\n";
                    return asyncFile.write(Buffer.buffer(header)).flatMap(v -> asyncFile.flush())
                            .flatMap(v -> asyncFile.close());
                })
                .onItem().transformToMulti(v -> Multi.createFrom().<String>emitter(emitter ->
                        IntStream.range(0, numberOfUser).forEach(index -> {
                            var dateFormat = DateTimeFormatter.ISO_LOCAL_DATE;
                            var dateTimeFormat = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                            var strBuilder = new StringBuilder();
                            var now = LocalDateTime.now();
                            strBuilder.append(index + 1).append(",")
                                    .append("\"").append(dateTimeFormat.format(now)).append("\",")
                                    .append("\"").append(dateTimeFormat.format(now)).append("\",")
                                    .append("\"").append("1").append("\",")
                                    .append("\"").append("username").append(index + 1).append("\",")
                                    .append("\"").append("passwd").append(index + 1).append("\",")
                                    .append("\"").append("screenname ").append(index + 1).append("\",")
                                    .append("\"").append(dateFormat.format(now.minus(20, ChronoUnit.YEARS))).append("\",")
                                    .append("\"").append("plain").append("\"\n");
                            var row = strBuilder.toString();
                            emitter.emit(row);
                        })))
                .concatMap(itm -> {
                    logger.info("dest: itm: " + itm);
//                    return vertx.fileSystem().open(filePath, new OpenOptions().setAppend(true))
////                            .toMulti()
//                            .flatMap(asyncFile -> asyncFile.write(Buffer.buffer(itm))
//                                    .flatMap(v -> asyncFile.flush())
////                                    .toMulti()
//                                    .map(v -> asyncFile)).toMulti();
                    return Multi.createFrom().item(itm);
                })
                .collect().last().invoke(asyncFile -> {
                    logger.info("created done!");
//                    asyncFile.closeAndForget();
                })
                .subscribe().with(v -> logger.info("Done all!"));
    }
}
