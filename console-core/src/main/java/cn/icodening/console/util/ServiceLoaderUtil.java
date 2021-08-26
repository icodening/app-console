package cn.icodening.console.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

/**
 * ServiceLoader util
 *
 * @author icodening
 * @date 2021.08.26
 */
public class ServiceLoaderUtil {

    private ServiceLoaderUtil() {
    }

    public static <T> List<T> getExtensions(Class<T> type, ClassLoader classLoader) {
        ServiceLoader<T> extensions = ServiceLoader.load(type, classLoader);
        if (!extensions.iterator().hasNext()) {
            return Collections.unmodifiableList(Collections.emptyList());
        }
        List<T> ret = new ArrayList<>();
        for (T value : extensions) {
            ret.add(value);
        }
        return ret;
    }

    public static <T> List<T> getExtensions(Class<T> type) {
        return getExtensions(type, Thread.currentThread().getContextClassLoader());
    }
}
