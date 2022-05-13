package org.harryng.demo.quarkus.router.ws;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import org.harryng.demo.quarkus.base.router.AbstractRouter;
import org.harryng.demo.quarkus.user.entity.UserImpl;
import org.harryng.demo.quarkus.user.service.UserService;
import org.harryng.demo.quarkus.util.SessionHolder;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class AbstractMapperRouter extends AbstractRouter {
    protected final Map<String, Object> beanMap = new LinkedHashMap<>();
    protected final Map<String, Function<Object[], ?>> methodMap = new LinkedHashMap<>();
    protected final Map<String, String> methodNameMap = new LinkedHashMap<>();
    protected final Map<String, Class<?>[]> paramsClassMap = new LinkedHashMap<>();

    @Inject
    protected BeanManager beanManager;

    protected <T> T initBeanConfiguration(String bizMethodKey) {
        var methodArr = bizMethodKey.split("\\.");
        var setBean = beanManager.getBeans(methodArr[0]);
        var proxiedBeanWrapper = beanManager.resolve(setBean);
        var creationalContext = beanManager.createCreationalContext(proxiedBeanWrapper);
        var proxiedBean = beanManager.getReference(proxiedBeanWrapper,
                proxiedBeanWrapper.getBeanClass(), creationalContext);
//        var method = Arrays.stream(proxiedBeanWrapper.getBeanClass().getDeclaredMethods())
//                .filter(m -> m.getName().equals(methodArr[1]))
//                .findFirst().orElseThrow();
//        paramsClassMap.putIfAbsent(bizMethodKey, method.getParameterTypes());
        beanMap.putIfAbsent(methodArr[0], proxiedBean);
        return (T) proxiedBean;
    }

    public <T> T getBean(String bizMethodName) {
        var methodArr = bizMethodName.split("\\.");
        return (T) beanMap.get(methodArr[0]);
    }

    protected abstract void initMethodName();

    protected abstract void initMethodMap();

    protected abstract void initMethodParamClasses();

    protected void initBeans() {
        for (Map.Entry<String, String> entry : methodNameMap.entrySet()) {
            initBeanConfiguration(entry.getValue());
        }
    }

    @PostConstruct
    protected void init() {
        initMethodName();
        initMethodMap();
        initMethodParamClasses();
        initBeans();
    }

    public boolean isRegisteredMethod(String methodId) {
        return methodMap.get(methodId) != null && methodNameMap.get(methodId) != null;
    }

    public Class<?>[] getParamClasses(String methodId) {
        return paramsClassMap.get(methodId);
    }

    public Uni<?> invokeMethod(String methodName, SessionHolder sessionHolder, Map<String, Object> extras, Object... params) {
        var methodParams = new Object[params.length + 2];
        methodParams[0] = sessionHolder;
        methodParams[methodParams.length - 1] = extras;
        for (int i = 0; i < params.length; i++) {
            methodParams[i + 1] = params[i];
        }
        return (Uni<?>) methodMap.get(methodName).apply(methodParams);
    }
}
