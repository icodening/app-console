package cn.icodening.console.monitor.sql;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.util.StringUtils;

import java.util.function.Function;

/**
 * @author icodening
 * @date 2021.07.28
 */
public class BeanProxyProcessor<T> {

    private final Class<T> beanClass;

    private Function<T, Object> proxyFunction;

    private String beanName;

    public static <T> BeanProxyProcessor<T> of(Class<T> beanClass) {
        return new BeanProxyProcessor<>(beanClass);
    }

    private BeanProxyProcessor(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    public BeanProxyProcessor<T> named(String beanName) {
        this.beanName = beanName;
        return this;
    }

    public BeanProxyProcessor<T> proxyBean(Function<T, Object> proxyFunction) {
        this.proxyFunction = proxyFunction;
        return this;
    }

    public SmartInstantiationAwareBeanPostProcessor buildProcessor() {
        return new SmartInstantiationAwareBeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (proxyFunction == null) {
                    return bean;
                }
                if (!beanClass.isAssignableFrom(bean.getClass())) {
                    return bean;
                }
                if (StringUtils.hasText(BeanProxyProcessor.this.beanName)
                        && BeanProxyProcessor.this.beanName.equals(beanName)) {
                    return proxyFunction.apply((T) bean);
                }
                return proxyFunction.apply((T) bean);
            }
        };
    }

}
