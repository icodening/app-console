package cn.icodening.console.register.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * @author icodening
 * @date 2021.06.25
 */
public class AppConsoleSpringContext extends AbstractRefreshableConfigApplicationContext {

    private DefaultListableBeanFactory beanFactory;

    private final List<Consumer<DefaultListableBeanFactory>> beanFactoryCustomizers = new CopyOnWriteArrayList<>();

    public AppConsoleSpringContext() {
        refresh();
    }

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        beanFactory.registerBeanDefinition(name, beanDefinition);
    }

    public void registerCustomizeDefaultListableBeanFactory(Consumer<DefaultListableBeanFactory> consumer) {
        if (consumer != null) {
            beanFactoryCustomizers.add(consumer);
        }
    }

    @Override
    protected void customizeBeanFactory(DefaultListableBeanFactory beanFactory) {
        super.customizeBeanFactory(beanFactory);
        for (Consumer<DefaultListableBeanFactory> consumer : beanFactoryCustomizers) {
            consumer.accept(beanFactory);
        }
    }
}
