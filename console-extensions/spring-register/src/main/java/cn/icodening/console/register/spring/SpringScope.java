package cn.icodening.console.register.spring;

import cn.icodening.console.ObjectFactory;
import cn.icodening.console.extension.Extensible;
import cn.icodening.console.extension.Scope;
import cn.icodening.console.util.ReflectUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 扩展Extension作用域对接Spring，此后所有Extension将享有Spring的所有特性
 *
 * @author icodening
 * @date 2021.06.25
 */
public class SpringScope implements Scope {

    private static final AppConsoleSpringContext APP_CONSOLE_SPRING_CONTEXT = new AppConsoleSpringContext();

    private static final List<Object> SPRING_BEANS = new CopyOnWriteArrayList<>();

    @Override
    public Object getObject(Class<?> clz, ObjectFactory<?> objectFactory) {
        Object ret = null;
        try {
            ret = APP_CONSOLE_SPRING_CONTEXT.getBean(clz);
        } catch (BeansException e) {
            String name = clz.getName();
            Class<?>[] interfaces = ReflectUtil.getAllInterfaces(clz);
            for (Class<?> in : interfaces) {
                if (in.isAnnotationPresent(Extensible.class)) {
                    BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(clz);
                    APP_CONSOLE_SPRING_CONTEXT.registerBeanDefinition(name, builder.getBeanDefinition());
                    break;
                }
            }
            ret = APP_CONSOLE_SPRING_CONTEXT.getBean(name);
        }
        SPRING_BEANS.add(ret);
        return ret;
    }

    @Override
    public String toString() {
        return "spring";
    }

    @Override
    public int getPriority() {
        return MAX_PRIORITY;
    }

    public static List<Object> getSpringBeans() {
        return SPRING_BEANS;
    }

    public static AppConsoleSpringContext getContext() {
        return APP_CONSOLE_SPRING_CONTEXT;
    }
}
