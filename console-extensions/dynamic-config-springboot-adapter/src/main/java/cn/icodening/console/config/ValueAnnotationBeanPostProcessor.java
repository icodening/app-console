package cn.icodening.console.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.context.ApplicationListener;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.SystemPropertyUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.springframework.core.annotation.AnnotationUtils.getAnnotation;

/**
 * reference:NacosValueAnnotationBeanPostProcessor
 *
 * @author icodening
 * @date 2021.07.10
 */
public class ValueAnnotationBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter
        implements ApplicationListener<ConfigChangeEvent>, BeanFactoryAware {

    private final PropertyPlaceholderHelper propertyPlaceholderHelper =
            new PropertyPlaceholderHelper("${", "}", ":", true);

    private final Map<String, List<ValueTarget>> placeholderValueTargetMap = new HashMap<>();

    private ConfigurableBeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, final String beanName)
            throws BeansException {

        doWithFields(bean, beanName);

        doWithMethods(bean, beanName);

        return super.postProcessBeforeInitialization(bean, beanName);
    }

    private void doWithFields(final Object bean, final String beanName) {
        ReflectionUtils.doWithFields(bean.getClass(), field -> {
            Value annotation = getAnnotation(field, Value.class);
            doWithAnnotation(beanName, bean, annotation, field.getModifiers(), null, field);
        });
    }

    private void doWithMethods(final Object bean, final String beanName) {
        ReflectionUtils.doWithMethods(bean.getClass(), method -> {
            Value annotation = getAnnotation(method, Value.class);
            doWithAnnotation(beanName, bean, annotation, method.getModifiers(), method, null);
        });
    }


    private void doWithAnnotation(String beanName, Object bean, Value annotation, int modifiers, Method method,
                                  Field field) {
        if (annotation != null) {
            if (Modifier.isStatic(modifiers)) {
                return;
            }
            String placeholder = resolvePlaceholder(annotation.value());

            if (placeholder == null) {
                return;
            }

            ValueTarget valueTarget = new ValueTarget(bean, beanName, method, field);
            put2ListMap(placeholderValueTargetMap, placeholder, valueTarget);
        }
    }

    private <K, V> void put2ListMap(Map<K, List<V>> map, K key, V value) {
        List<V> valueList = map.get(key);
        if (valueList == null) {
            valueList = new ArrayList<>();
        }
        valueList.add(value);
        map.put(key, valueList);
    }


    private String resolvePlaceholder(String placeholder) {
        if (!placeholder.startsWith(SystemPropertyUtils.PLACEHOLDER_PREFIX)) {
            return null;
        }

        if (!placeholder.endsWith(SystemPropertyUtils.PLACEHOLDER_SUFFIX)) {
            return null;
        }

        if (placeholder.length() <= SystemPropertyUtils.PLACEHOLDER_PREFIX.length() + SystemPropertyUtils.PLACEHOLDER_SUFFIX.length()) {
            return null;
        }

        int beginIndex = SystemPropertyUtils.PLACEHOLDER_PREFIX.length();
        int endIndex = placeholder.length() - SystemPropertyUtils.PLACEHOLDER_SUFFIX.length();
        placeholder = placeholder.substring(beginIndex, endIndex);

        int separatorIndex = placeholder.indexOf(SystemPropertyUtils.VALUE_SEPARATOR);
        if (separatorIndex != -1) {
            return placeholder.substring(0, separatorIndex);
        }
        return placeholder;
    }

    @Override
    public void onApplicationEvent(ConfigChangeEvent event) {
        Properties properties = event.getSource();
        Set<Map.Entry<String, List<ValueTarget>>> entries = placeholderValueTargetMap.entrySet();
        for (Map.Entry<String, List<ValueTarget>> entry : entries) {
            String key = entry.getKey();
            List<ValueTarget> valueTargets = entry.getValue();
            String newKey = "${" + key + "}";
            String newValue = propertyPlaceholderHelper.replacePlaceholders(newKey, properties);
            if (newKey.equals(newValue)) {
                continue;
            }
            for (ValueTarget valueTarget : valueTargets) {
                if (valueTarget.method != null) {
                    ReflectionUtils.makeAccessible(valueTarget.method);
                    ReflectionUtils.invokeMethod(valueTarget.method, valueTarget.bean, convertIfNecessary(valueTarget.field, newValue));
                } else {
                    try {
                        ReflectionUtils.makeAccessible(valueTarget.field);
                        valueTarget.field.set(valueTarget.bean, convertIfNecessary(valueTarget.field, newValue));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    private Object convertIfNecessary(Field field, Object value) {
        TypeConverter converter = beanFactory.getTypeConverter();
        return converter.convertIfNecessary(value, field.getType(), field);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableBeanFactory) beanFactory;
    }

    private static class ValueTarget {

        private Object bean;

        private String beanName;

        private Method method;

        private Field field;

        ValueTarget(Object bean, String beanName, Method method, Field field) {
            this.bean = bean;

            this.beanName = beanName;

            this.method = method;

            this.field = field;
        }
    }
}
