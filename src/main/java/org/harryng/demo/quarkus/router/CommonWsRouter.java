package org.harryng.demo.quarkus.router;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.smallrye.mutiny.unchecked.Unchecked;
import io.vertx.core.Future;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.harryng.demo.quarkus.base.router.AbstractRouter;
import org.harryng.demo.quarkus.base.service.BaseService;
import org.harryng.demo.quarkus.router.ws.AbstractMapperRouter;
import org.harryng.demo.quarkus.router.ws.MethodMapperRouter;
import org.harryng.demo.quarkus.util.ReactiveUtil;
import org.harryng.demo.quarkus.util.SessionHolder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

@ApplicationScoped
@RouteBase(path = "/ws")
public class CommonWsRouter extends AbstractRouter {

    static Logger logger = LoggerFactory.getLogger(CommonWsRouter.class);

    @Inject
    protected MethodMapperRouter mapperRouter;

    protected String getMethodId(JsonObject rootJson) {
        return rootJson.getString("method");
    }

    protected Object[] getParamValues(JsonArray paramsJsonArr, Class<?>[] paramClasses) throws Exception {
        var paramValues = new Object[paramsJsonArr.size()];
        if (paramsJsonArr.size() == paramClasses.length - 2) {
            for (int i = 0; i < paramsJsonArr.size(); i++) {
                try {
                    paramValues[i] = getObjectMapper().readValue(
                            paramsJsonArr.getValue(i).toString(),
                            paramClasses[i + 1]);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            throw new Exception("method params are not matched");
        }
        return paramValues;
    }

    protected SessionHolder getSessionHolder(RoutingContext ctx) {
        return SessionHolder.createAnonymousSession();
    }

    @Route(path = "/*", type = Route.HandlerType.NORMAL, order = 100)
    public void handleDefault(RoutingContext ctx) {
        // accept websocket
        ctx.request().toWebSocket()
                .map(webSocket -> webSocket
                        .handler(buff -> {
                            var reqRootJson = buff.toJsonObject();
                            var resRootJson = new JsonObject();
                            // get methodId
                            var methodId = getMethodId(reqRootJson);
                            // check method existed
                            if (mapperRouter.isRegisteredMethod(methodId)) {
                                // parse params
                                var paramClasses = mapperRouter.getParamClasses(methodId);
                                try {
                                    var params = getParamValues(
                                            reqRootJson.getJsonArray("params", new JsonArray()), paramClasses);
                                    // begin trans
                                    sessionFactory.withStatelessTransaction((sess, trans) -> {
                                                var sessionHolder = getSessionHolder(ctx);
                                                Map<String, Object> extras = new LinkedHashMap<>();
                                                extras.putIfAbsent(BaseService.TRANS_STATELESS_SESSION, sess);
                                                extras.putIfAbsent(BaseService.TRANSACTION, trans);
                                                // invoke
                                                return mapperRouter.invokeMethod(methodId, sessionHolder, extras, params);
                                                // end trans
                                            })
                                            // write response
                                            .map(Unchecked.function(
                                                    itm -> webSocket.writeTextMessage(getObjectMapper().writeValueAsString(itm))
                                            ))
                                            // write error
                                            .onFailure().invoke(ex -> {
                                                logger.error("" , ex);
                                                resRootJson.put("code", "500")
                                                        .put("message", ex.getMessage());
                                                webSocket.writeTextMessage(resRootJson.toString())
                                                        .compose(v -> webSocket.end());
                                            })
                                            .eventually(() -> webSocket.end())
                                            .subscribe().with(ReactiveUtil.defaultSuccessConsumer(), ReactiveUtil.defaultFailureConsumer());
                                } catch (Exception e) {
                                    logger.error("" , e);
                                    resRootJson.put("code", "500")
                                            .put("message", e.getMessage());
                                    webSocket.writeTextMessage(resRootJson.toString())
                                            .compose(v -> webSocket.end());
                                }
                            } else {
                                resRootJson.put("code", "404")
                                        .put("message", "method is not found");
                                webSocket.writeTextMessage(resRootJson.toString())
                                        .compose(v -> webSocket.end());
                            }
                        })
                        .drainHandler(v -> {
                        })
                        .closeHandler(v -> {
                        })
                        .endHandler(v -> {
                        })
                        .exceptionHandler(thr -> {
                            logger.error("" , thr);
                            var resRootJson = new JsonObject();
                            resRootJson.put("code", "500")
                                    .put("message", thr.getMessage());
                            webSocket.writeTextMessage(resRootJson.toString())
                                    .compose(v -> webSocket.end());
                        })
                )
                .compose(ReactiveUtil.defaultSuccessFunction(), ReactiveUtil.defaultFailureFunction());
    }

    @Route(path = "/*", type = Route.HandlerType.FAILURE, order = 50)
    public void handleError(RoutingContext ctx) {
        logger.error("Error[" + ctx.response().getStatusCode() + "]:" + ctx.response().getStatusMessage());
        ctx.response().end(ctx.response().getStatusMessage()).compose(v -> Future.succeededFuture());
    }
}
