package cn.icodening.console.register.spring;

import cn.icodening.console.ObjectFactory;
import cn.icodening.console.extension.Extensible;
import cn.icodening.console.extension.Scope;
import cn.icodening.console.util.ReflectUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

/**
 * @author icodening
 * @date 2021.06.25
 */
public class SpringScope implements Scope {

    private static final AppConsoleSpringContext APP_CONSOLE_SPRING_CONTEXT = new AppConsoleSpringContext();

    static {
        //需要注意的是防止直接使用系统加载器导致无法读取到自身的SpringFactories
        APP_CONSOLE_SPRING_CONTEXT.refresh();
    }

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

    public static AppConsoleSpringContext getContext() {
        return APP_CONSOLE_SPRING_CONTEXT;
    }
}
