package cn.icodening.console.register.spring;

import cn.icodening.console.common.event.ApplicationInstanceStartedEvent;
import cn.icodening.console.common.model.ApplicationInstance;
import cn.icodening.console.event.EventDispatcher;
import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerFactory;
import cn.icodening.console.util.NetUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigUtils;

import java.lang.management.ManagementFactory;

/**
 * @author icodening
 * @date 2021.06.29
 */
public class AgentStartInitialization implements InitializingBean, ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgentStartInitialization.class);

    private static final String SERVER_PORT_KEY = "server.port";
    private static final String APPLICATION_NAME_KEY = "spring.application.name";

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //1.刷新ExtensionLoader并回调依赖注入
        AppConsoleSpringContext appConsoleSpringContext = SpringScope.getContext();
        appConsoleSpringContext.registerCustomizeDefaultListableBeanFactory(AnnotationConfigUtils::registerAnnotationConfigProcessors);
        appConsoleSpringContext.setParent(applicationContext);
        appConsoleSpringContext.refresh();
        AutowireCapableBeanFactory autowireCapableBeanFactory = appConsoleSpringContext.getParent().getAutowireCapableBeanFactory();
        for (Object bean : SpringScope.getSpringBeans()) {
            autowireCapableBeanFactory.autowireBean(bean);
        }

        //2.将当前应用注册到application console
        registerInstance();
    }


    private void registerInstance() {
        String localhost = NetUtil.getLocalhost();
        if (localhost == null) {
            LOGGER.info("localhost is null, dont't register");
            return;
        }
        String portString = applicationContext.getEnvironment().getProperty(SERVER_PORT_KEY);
        if (portString == null) {
            portString = "8080";
        }
        LOGGER.info("post is " + portString);
        ApplicationInstance.Builder builder = ApplicationInstance.newBuilder();
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.substring(0, name.indexOf("@"));
        String applicationName = applicationContext.getEnvironment().getProperty(APPLICATION_NAME_KEY);
        if (applicationName == null) {
            applicationName = localhost + ":" + portString + "@pid=" + pid;
        }
        ApplicationInstance instance = builder.ip(localhost)
                .port(Integer.parseInt(portString))
                .pid(pid)
                .applicationName(applicationName)
                .identity(localhost + ":" + portString)
                .build();
        EventDispatcher.dispatch(new ApplicationInstanceStartedEvent(instance));
    }
}
