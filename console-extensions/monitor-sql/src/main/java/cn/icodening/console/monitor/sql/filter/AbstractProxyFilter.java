package cn.icodening.console.monitor.sql.filter;

import cn.icodening.console.monitor.sql.ProxyFilter;

import java.lang.reflect.ParameterizedType;

/**
 * @author icodening
 * @date 2021.07.30
 */
public abstract class AbstractProxyFilter<T> implements ProxyFilter<T> {

    private Class<T> type;

    public AbstractProxyFilter() {
        Class<?> superclass = getClass();
        do {
            if (superclass.getSuperclass().equals(AbstractProxyFilter.class)) {
                ParameterizedType p = (ParameterizedType) superclass.getGenericSuperclass();
                this.type = (Class<T>) p.getActualTypeArguments()[0];
            }
        } while ((superclass = superclass.getSuperclass()) != Object.class);
    }

    public Class<T> getType() {
        return type;
    }
}
