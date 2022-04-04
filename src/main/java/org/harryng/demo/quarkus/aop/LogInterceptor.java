package org.harryng.demo.quarkus.aop;

import io.quarkus.logging.Log;

import javax.inject.Singleton;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

@Singleton()
public class LogInterceptor {

    @AroundInvoke
    public Object invoke(InvocationContext context) throws Exception {
        Log.info("===== into: " + context.getMethod().getName() + " =====");
        Object result = context.proceed();
        Log.info("----- out of: " + context.getMethod().getName() + "-----");
        return result;
    }
}
