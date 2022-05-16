package org.harryng.demo.quarkus.interceptor;

import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import static javax.interceptor.Interceptor.Priority.APPLICATION;

@Priority(APPLICATION + 500)
@Authenticated
@Interceptor
public class AuthInterceptor {
    static Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    @AroundInvoke
    protected Object invoke(InvocationContext context) throws Exception {
//        logger.info("+++++");
        Object ret = context.proceed();
//        logger.info("-----");
        return ret;
    }
}
