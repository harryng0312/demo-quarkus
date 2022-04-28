package org.harryng.demo.quarkus.util;

import io.vertx.core.Future;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

import java.util.function.Consumer;
import java.util.function.Function;

public class ReactiveUtil {
    static Logger logger = LoggerFactory.getLogger(ReactiveUtil.class);

    public static Function defaultSuccessFunction() {
        return (v) -> v;
    }

    public static <T extends Throwable, R> Function<T, Future<R>> defaultFailureFunction() {
        return ex -> Future.failedFuture(ex);
    }

    public static <T extends Throwable, R> Function<T, Future<R>> defaultFailureWithLogFunction() {
        return (ex) -> {
            logger.error("Default failure:", ex);
            return Future.failedFuture(ex);
        };
    }

    public static Consumer defaultSuccessConsumer() {
        return (v) -> {
        };
    }

    public static <T extends Throwable> Consumer<T> defaultFailureConsumer() throws RuntimeException{
        return ex -> {
            try {
                throw ex;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static <T extends Throwable> Consumer<T> defaultFailureConsumer(Throwable ex) {
        return v -> logger.error("Default failure:", ex);
    }
}
