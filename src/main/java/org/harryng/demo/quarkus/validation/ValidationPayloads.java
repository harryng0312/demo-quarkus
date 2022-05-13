package org.harryng.demo.quarkus.validation;

import java.util.LinkedHashMap;
import java.util.Map;

public class ValidationPayloads {
    protected Map<Class<?>, Object> payloadMap = new LinkedHashMap<>();
    public <T> T get(Class<T> clazz) {
        return (T) payloadMap.get(clazz);
    }
    public <T> void put(Class<T> key, T value){
        payloadMap.put(key, value);
    }

    public static ValidationPayloads newInstance(){
        return new ValidationPayloads();
    }
}
