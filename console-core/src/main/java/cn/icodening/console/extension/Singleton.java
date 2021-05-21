package cn.icodening.console.extension;

import cn.icodening.console.ObjectFactory;
import cn.icodening.console.util.Holder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author icodening
 * @date 2021.01.01
 */
public class Singleton implements Scope {

    private final Map<Class<?>, Holder<Object>> instanceHolderMap = new ConcurrentHashMap<>();

    @Override
    public Object getObject(Class<?> clz, ObjectFactory<?> objectFactory) {
        Holder<Object> holder = instanceHolderMap.get(clz);
        if (holder == null) {
            instanceHolderMap.putIfAbsent(clz, new Holder<>());
        }
        holder = instanceHolderMap.get(clz);
        Object instance = holder.get();
        if (instance != null) {
            return instance;
        }
        synchronized (holder) {
            instance = holder.get();
            if (instance == null) {
                Object extensionInstance = objectFactory.getObject();
                holder.set(extensionInstance);
                instance = holder.get();
            }
        }
        return instance;
    }

    @Override
    public String toString() {
        return "singleton";
    }
}
