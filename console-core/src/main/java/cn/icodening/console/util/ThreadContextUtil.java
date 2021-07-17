package cn.icodening.console.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author icodening
 * @date 2021.07.17
 */
public class ThreadContextUtil {

    private static final ThreadLocal<Map<String, Object>> CONTEXT = ThreadLocal.withInitial(HashMap::new);

    public static <T> T get(String name, Class<T> targetClass) {
        Map<String, Object> map = CONTEXT.get();
        Object target = map.get(name);
        return castIfNecessary(target, targetClass);
    }

    public static void set(String name, Object object) {
        Map<String, Object> map = CONTEXT.get();
        map.put(name, object);
    }

    public static <T> T remove(String name, Class<T> targetClass) {
        Map<String, Object> map = CONTEXT.get();
        Object remove = map.remove(name);
        return castIfNecessary(remove, targetClass);
    }

    private static <T> T castIfNecessary(Object target, Class<T> targetClass) {
        if (target == null
                || !targetClass.isAssignableFrom(target.getClass())) {
            return null;
        }
        return targetClass.cast(target);
    }

    public static void remove() {
        CONTEXT.remove();
    }
}
