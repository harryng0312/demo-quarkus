package org.harryng.demo.quarkus.router.ws;

import io.smallrye.mutiny.unchecked.Unchecked;
import org.harryng.demo.quarkus.user.entity.UserImpl;
import org.harryng.demo.quarkus.user.service.UserService;
import org.harryng.demo.quarkus.util.SessionHolder;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.util.Map;

@Singleton
public class MethodMapperRouter extends AbstractMapperRouter {

    @Override
    @PostConstruct
    protected void init(){
        super.init();
    }

    @Override
    protected void initMethodName() {
        // user
        methodNameMap.put("getUserById", "userService.getById");
        methodNameMap.put("addUser", "userService.add");
        methodNameMap.put("editUser", "userService.edit");
        methodNameMap.put("removeUser", "userSerivce.remove");
    }

    @Override
    protected void initMethodMap() {
        // user
        methodMap.put("getUserById", Unchecked.function(params -> this.<UserService>getBean(methodNameMap.get("getUserById"))
                .getById((SessionHolder) params[0], (long) params[1], (Map<String, Object>) params[2])));
        methodMap.put("addUser", Unchecked.function(params -> this.<UserService>getBean(methodNameMap.get("addUser"))
                .add((SessionHolder) params[0], (UserImpl) params[1], (Map<String, Object>) params[2])));
        methodMap.put("editUser", Unchecked.function(params -> this.<UserService>getBean(methodNameMap.get("editUser"))
                .edit((SessionHolder) params[0], (UserImpl) params[1], (Map<String, Object>) params[2])));
        methodMap.put("removeUser", Unchecked.function(params -> this.<UserService>getBean(methodNameMap.get("removeUser"))
                .remove((SessionHolder) params[0], (long) params[1], (Map<String, Object>) params[2])));
    }

    @Override
    protected void initMethodParamClasses() {
        // user
        paramsClassMap.put("getUserById", new Class[]{SessionHolder.class, long.class, Map.class});
        paramsClassMap.put("addUser", new Class[]{SessionHolder.class, UserImpl.class, Map.class});
        paramsClassMap.put("editUser", new Class[]{SessionHolder.class, UserImpl.class, Map.class});
        paramsClassMap.put("removeUser", new Class[]{SessionHolder.class, long.class, Map.class});
    }
}
