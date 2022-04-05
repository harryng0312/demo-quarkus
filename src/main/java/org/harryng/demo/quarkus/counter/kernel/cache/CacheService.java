package org.harryng.demo.quarkus.counter.kernel.cache;

//import com.hazelcast.map.IMap
import java.io.Serializable;
import java.util.Map;

public interface CacheService {
    public String K_SESSION = "session";
    public String K_SESSION_MAP = "sessionMap";

    public <K extends Serializable, V extends Serializable> Map<K, V> getMap(String name);
    public Map<String, Serializable> getSessionMap(String userKey);
    public Serializable getSessionValue(String userKey, String dataKey);
    public void putSessionValue(String userKey, String dataKey, Serializable value);
    public void invalidateSession(String userKey);
    public void removeSessionValues(String userKey, String... dataKeys);
}