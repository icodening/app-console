package cn.icodening.console.common.util;

import cn.icodening.console.common.convert.ClassCasterManager;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author icodening
 * @date 2021.06.12
 */
public class BeanMapUtil {

    private static final Map<Class<?>, BeanInfo> BEAN_INFO_MAP = new ConcurrentHashMap<>();

    private static final Set<String> IGNORE_PROPERTIES =
            Collections.synchronizedSet(new HashSet<>(Collections.singletonList("class")));

    private BeanMapUtil() {
    }

    private static BeanInfo getBeanInfo(Class<?> beanClass) throws IntrospectionException {
        BeanInfo beanInfo = BEAN_INFO_MAP.get(beanClass);
        if (beanInfo != null) {
            return beanInfo;
        }
        beanInfo = Introspector.getBeanInfo(beanClass);
        BEAN_INFO_MAP.putIfAbsent(beanClass, beanInfo);
        return beanInfo;
    }

    public static <T> T mapToBean(Map<String, Object> map, Class<T> clz) throws Exception {
        BeanInfo beanInfo = getBeanInfo(clz);
        T ret = clz.newInstance();
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String propertyName = propertyDescriptor.getName();
            if (IGNORE_PROPERTIES.contains(propertyName)) {
                continue;
            }
            Object value = map.get(propertyName);
            Method writeMethod = propertyDescriptor.getWriteMethod();
            Class<?> propertyType = propertyDescriptor.getPropertyType();
            value = ClassCasterManager.cast(value, propertyType);
            if (writeMethod != null) {
                writeMethod.invoke(ret, value);
            }
        }
        return ret;
    }
}
