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
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Singleton
public class MapperRouter extends AbstractRouter {
    protected final Map<String, Object> beanMap = new LinkedHashMap<>();
    protected final Map<String, Function<Object[], ?>> methodMap = new LinkedHashMap<>();
    protected final Map<String, String> methodNameMap = new LinkedHashMap<>();
    protected final Map<String, Class<?>[]> paramsClassMap = new LinkedHashMap<>();

    @Inject
    protected BeanManager beanManager;

    protected <T> T initBeanConfiguration(String bizMethodKey) {
        var methodArr = bizMethodKey.split("\\.");
        var setBean = beanManager.getBeans(methodArr[0]);
        var proxiedBean = beanManager.resolve(setBean);
        var creationalContext = beanManager.createCreationalContext(proxiedBean);
        var bean = beanManager.getReference(proxiedBean,
                proxiedBean.getBeanClass(), creationalContext);
        var methods = Arrays.stream(bean.getClass().getMethods())
                .filter(m -> m.getName().equals(methodArr[1]))
                .toList();
        paramsClassMap.put(bizMethodKey, methods.get(methods.size() - 1).getParameterTypes());
        beanMap.putIfAbsent(methodArr[0], bean);
        return (T) bean;
    }

    public <T> T getBean(String bizMethodName) {
        var methodArr = bizMethodName.split("\\.");
        return (T) beanMap.get(methodArr[0]);
    }

    protected void initMethodName() {
        methodNameMap.put("addUser", "userService.add");
        methodNameMap.put("getUserById", "userService.getById");
    }

    protected void initMethodMap() {
        methodMap.put("addUser", Unchecked.function(params -> this.<UserService>getBean(methodNameMap.get("addUser"))
                .add((SessionHolder) params[0], (UserImpl) params[1], (Map<String, Object>) params[2])));
        methodMap.put("getUserById", Unchecked.function(params -> this.<UserService>getBean(methodNameMap.get("getUserById"))
                .getById((SessionHolder) params[0], (long) params[1], (Map<String, Object>) params[2])));
    }

    protected void initBeans() {
        for (Map.Entry<String, String> entry : methodNameMap.entrySet()) {
            initBeanConfiguration(entry.getValue());
        }
    }

    @PostConstruct
    protected void init() {
        initMethodName();
        initMethodMap();
        initBeans();
    }

    public boolean isRegisteredMethod(String methodId) {
        return methodMap.get(methodId) != null && methodNameMap.get(methodId) != null;
    }

    public Class<?>[] getParamClasses(String methodId) {
        return paramsClassMap.get(methodNameMap.get(methodId));
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
