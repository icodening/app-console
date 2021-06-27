package cn.icodening.console.register.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;

/**
 * @author icodening
 * @date 2021.06.25
 */
public class AppConsoleSpringContext extends AbstractRefreshableConfigApplicationContext {

    private DefaultListableBeanFactory beanFactory;

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException {
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);
        this.beanFactory = beanFactory;
    }

    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        beanFactory.registerBeanDefinition(name, beanDefinition);
    }
}
