package org.harryng.demo.quarkus.user;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Context;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.file.OpenOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.core.file.AsyncFile;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.stream.IntStream;

@QuarkusTest
public class TestUserFile {
    static Logger logger = LoggerFactory.getLogger(TestUserFile.class);
    //    @Inject
    protected Vertx vertx;

    @Test
    public void createUserFile() {
        var filePath = "./setup/jmeter/data/users.csv";
        int numberOfUser = 10_000;
        var vertx = Vertx.vertx();
        vertx.fileSystem()
                .exists(filePath).flatMap(aBoolean -> {
                    if (aBoolean) {
                        return vertx.fileSystem().delete(filePath);
                    }
                    return Uni.createFrom().voidItem();
                })
                .flatMap(itm -> vertx.fileSystem().open(filePath, new OpenOptions().setCreate(true).setAppend(true)))
                .attachContext()
                .flatMap(asyncFileItemWithContext -> {
                    logger.info("creating ...");
                    var asyncFile = asyncFileItemWithContext.get();
                    asyncFileItemWithContext.context().put("asyncFile", asyncFile);
                    var header = "user.id, user.createdDate, user.modifiedDate, user.status, user.username, " +
                            "user.password, user.screenName, user.dob, user.passwdEncryptedMethod\n";
                    return asyncFile.write(Buffer.buffer(header)).flatMap(v -> asyncFile.flush());
                })
                .onItem().transformToMulti(v -> Multi.createFrom().<String>emitter((emitter) -> {
                            IntStream.range(0, numberOfUser)
                                    .forEach(index -> {
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
                                    });
                            emitter.complete();
                        }
                ))
                .attachContext()
                .concatMap(itemWithContext -> {
                    logger.info("dest: itm: " + itemWithContext.get());
                    var asyncFile = itemWithContext.context().<AsyncFile>get("asyncFile");
                    return asyncFile.write(Buffer.buffer(itemWithContext.get()))
                            .flatMap(v -> asyncFile.flush())
                            .map(v -> asyncFile)
                            .toMulti();
//                    return vertx.fileSystem().open(filePath, new OpenOptions().setAppend(true))
//                            .flatMap(asyncFile -> asyncFile.write(Buffer.buffer(itm))
//                                    .flatMap(v -> asyncFile.flush())
//                                    .map(v -> asyncFile))
//                            .toMulti();
//                    return Multi.createFrom().item(itm);
                })
                .attachContext()
                .collect().last().invoke(itemWithContext -> {
                    logger.info("created done!");
                    var asyncFile = itemWithContext.context().<AsyncFile>get("asyncFile");
//                    asyncFile.closeAndForget();
                })
                .subscribe().with(Context.from(new HashMap<>()), v -> logger.info("Done all!"));
    }
}
