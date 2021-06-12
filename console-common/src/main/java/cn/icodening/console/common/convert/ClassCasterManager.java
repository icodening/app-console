package cn.icodening.console.common.convert;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author icodening
 * @date 2021.06.12
 */
public class ClassCasterManager {

    private static final Map<Class<?>, ClassCaster> CLASS_CASTER_MAP = new ConcurrentHashMap<>(8);

    static {
        CLASS_CASTER_MAP.putIfAbsent(Date.class, new DateClassCaster());
        CLASS_CASTER_MAP.putIfAbsent(Long.class, new LongClassCaster());
        CLASS_CASTER_MAP.putIfAbsent(long.class, new LongClassCaster());
    }

    private ClassCasterManager() {
    }

    public static Object cast(Object obj, Class<?> targetClass) {
        ClassCaster classCaster = CLASS_CASTER_MAP.get(targetClass);
        if (classCaster == null) {
            return obj;
        }
        return classCaster.convert(obj);
    }
}
