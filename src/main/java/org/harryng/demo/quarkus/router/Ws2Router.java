package org.harryng.demo.quarkus.router;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import io.vertx.core.Future;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;
import org.harryng.demo.quarkus.base.controller.AbstractController;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

//@ApplicationScoped
//@RouteBase(path = "/ws")
public class Ws2Router extends AbstractController {
    static Logger logger = LoggerFactory.getLogger(Ws2Router.class);

    @Inject
    protected BeanManager beanManager;

//    @Route(path = "/*")
    public void handleDefault(RoutingContext ctx) {
        logger.info("into /ws handler");
        ctx.request().toWebSocket()
                .map(Unchecked.function(ws -> ws.handler(buff -> {
                            // extract method & params
                            var req = buff.toJsonObject();
                            // validate
                            if (req != null) {
                                // invoke to biz
                                var setBean = beanManager.getBeans("userRouter");
                                if (!setBean.isEmpty()) {
                                    Bean<?> proxiedBean = beanManager.resolve(setBean);
                                    CreationalContext<?> creationalContext = beanManager.createCreationalContext(proxiedBean);
                                    var bean = beanManager.getReference(proxiedBean,
                                            proxiedBean.getBeanClass(), creationalContext);
                                    // find method
                                    Method method = null;
                                    var startTime = LocalDateTime.now();
                                    method = Arrays.stream(bean.getClass().getMethods())
                                            .filter(m -> m.getName().equals(req.getString("method")))
                                            .findFirst().orElseThrow();
                                    var endTime = LocalDateTime.now();
                                    logger.info("time to lookup method:" + Duration.between(startTime, endTime).toMillis());
                                    // map result
                                    var paramsJsonArr = req.getJsonArray("params", new JsonArray());
                                    if (paramsJsonArr.size() == method.getParameterCount()) {
                                        var params = new Object[paramsJsonArr.size()];
                                        for (int i = 0; i < paramsJsonArr.size(); i++) {
                                            try {
                                                params[i] = getObjectMapper().readValue(
                                                        paramsJsonArr.getJsonObject(i).toString(),
                                                        method.getParameterTypes()[i]);
                                            } catch (JsonProcessingException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                        Uni<?> rs = null;
                                        try {
                                            rs = (Uni<?>) method.invoke(bean, params);
                                        } catch (IllegalAccessException | InvocationTargetException e) {
                                            throw new RuntimeException(e);
                                        }
                                        rs.invoke(Unchecked.consumer(obj -> {
                                            ws.writeTextMessage(getObjectMapper().writeValueAsString(obj))
                                                    .eventually(v -> ws.end());
                                        })).subscribe().with(itm -> {
                                        }, Unchecked.consumer(ex -> {
                                            throw new RuntimeException(ex);
                                        }));
                                        // map ex
                                    }
                                }
                            }
                        })
                        .drainHandler(v -> {
                        })
                        .closeHandler(v -> {
                        })
                        .endHandler(v -> {
                        })))
                // accept socket
                .compose(ws -> Future.succeededFuture(), Future::failedFuture);
//        ctx.next();
    }

//    @Route(path = "/*", type = Route.HandlerType.FAILURE)
    public void handleError(RoutingContext ctx) {
        logger.error("Error[" + ctx.response().getStatusCode() + "]:" + ctx.response().getStatusMessage());
        ctx.response().end(ctx.response().getStatusMessage()).compose(v -> Future.succeededFuture());
    }
}
