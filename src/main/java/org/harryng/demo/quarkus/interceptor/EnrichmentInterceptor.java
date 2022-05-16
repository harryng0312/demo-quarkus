package org.harryng.demo.quarkus.interceptor;

import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import org.harryng.demo.quarkus.base.service.BaseService;
import org.harryng.demo.quarkus.util.SessionHolder;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.Locale;
import java.util.Map;

import static javax.interceptor.Interceptor.Priority.APPLICATION;

@Priority(APPLICATION + 200)
@Enriched
@Interceptor
public class EnrichmentInterceptor {
    static Logger logger = LoggerFactory.getLogger(EnrichmentInterceptor.class);

    @Inject
    protected RoutingContext ctx;

    @AroundInvoke
    protected Object invoke(InvocationContext context) throws Exception {
//        logger.info("+++++");
        if (context.getParameters().length > 0) {
            var sessionHolder = (SessionHolder) context.getParameters()[0];
            if (ctx.request().headers().get("Accept-Language") != null) {
                sessionHolder.setLocale(Locale.forLanguageTag(ctx.request().headers().get("Accept-Language")));
            } else {
                sessionHolder.setLocale(Locale.ENGLISH);
            }
            if (context.getParameters().length > 1) {
                var extras = (Map<String, Object>) context.getParameters()[context.getParameters().length - 1];
                extras.put(BaseService.HTTP_HEADERS, ctx.request().headers());
                extras.put(BaseService.HTTP_COOKIES, ctx.request().cookies());
            }
        }
        Object ret = context.proceed();
//        logger.info("-----");
        return ret;
    }
}
