package org.harryng.demo.quarkus.router.ws;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import org.harryng.demo.quarkus.base.router.AbstractRouter;
import org.harryng.demo.quarkus.user.entity.UserImpl;
import org.harryng.demo.quarkus.user.service.UserService;
import org.harryng.demo.quarkus.util.SessionHolder;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Singleton
public class MapperRouter extends AbstractRouter {
    private final Map<String, Function<Object[], ?>> methodMap = new LinkedHashMap<>();
    private final Map<String, Class<?>[]> paramsClassMap = new LinkedHashMap<>();

    @Inject
    protected BeanManager beanManager;

    protected <T> T resolveBean(String key){
        var methodArr = key.split(".");
        var setBean = beanManager.getBeans(methodArr[0]);
        Bean<?> proxiedBean = beanManager.resolve(setBean);
        CreationalContext<?> creationalContext = beanManager.createCreationalContext(proxiedBean);
        var bean = beanManager.getReference(proxiedBean,
                proxiedBean.getBeanClass(), creationalContext);
        Method method = Arrays.stream(bean.getClass().getMethods())
                .filter(m -> m.getName().equals(methodArr[1]))
                .findFirst().orElseThrow();
        paramsClassMap.put(key, method.getParameterTypes());
        return (T) bean;
    }
    protected void initMethodMap() {
        methodMap.put("userService.add", Unchecked.function(params -> this.<UserService>resolveBean("userService")
                .add((SessionHolder) params[0], (UserImpl) params[1], (Map<String, Object>) params[2])));
    }

    public Map<String, Function<Object[], ?>> getMethodMap() {
        if (methodMap == null) {
            initMethodMap();
        }
        return methodMap;
    }

    public Uni<?> invokeMethod(String methodName, SessionHolder sessionHolder, Map<String, Object> extras, Object... params) {
        var methodParams = new Object[params.length + 2];
        methodParams[0] = sessionHolder;
        methodParams[methodParams.length - 1] = extras;
        for (int i = 0; i < params.length; i++) {
            methodParams[i + 1] = params[i];
        }
        return (Uni<?>) getMethodMap().get(methodName).apply(methodParams);
    }
}
