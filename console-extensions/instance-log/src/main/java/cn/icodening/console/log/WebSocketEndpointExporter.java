package cn.icodening.console.log;

import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import java.util.Map;

/**
 * reference {@link org.springframework.web.socket.server.standard.ServerEndpointExporter}
 * 用于免去应依赖 spring-websocket
 *
 * @author icodening
 * @date 2021.07.13
 */
public class WebSocketEndpointExporter
        implements SmartInitializingSingleton, ServletContextAware, ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketEndpointExporter.class);

    private ServerContainer serverContainer;

    private ApplicationContext applicationContext;

    protected ServerContainer getServerContainer() {
        return this.serverContainer;
    }

    @Override
    public void afterSingletonsInstantiated() {
        if (getServerContainer() == null) {
            LOGGER.warn("javax.websocket.server.ServerContainer not available");
            return;
        }
        registerEndpoints();
    }

    protected void registerEndpoints() {
        if (applicationContext == null
                || serverContainer == null) {
            return;
        }
        String[] endpointBeanNames = applicationContext.getBeanNamesForAnnotation(ServerEndpoint.class);
        for (String beanName : endpointBeanNames) {
            registerEndpoint(applicationContext.getType(beanName));
        }

        Map<String, ServerEndpointConfig> endpointConfigMap = applicationContext.getBeansOfType(ServerEndpointConfig.class);
        for (ServerEndpointConfig endpointConfig : endpointConfigMap.values()) {
            registerEndpoint(endpointConfig);
        }
    }

    private void registerEndpoint(Class<?> endpointClass) {
        try {
            serverContainer.addEndpoint(endpointClass);
        } catch (DeploymentException ex) {
            throw new IllegalStateException("Failed to register @ServerEndpoint class: " + endpointClass, ex);
        }
    }

    private void registerEndpoint(ServerEndpointConfig endpointConfig) {
        try {
            serverContainer.addEndpoint(endpointConfig);
        } catch (DeploymentException ex) {
            throw new IllegalStateException("Failed to register ServerEndpointConfig: " + endpointConfig, ex);
        }
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        if (this.serverContainer == null) {
            this.serverContainer =
                    (ServerContainer) servletContext.getAttribute("javax.websocket.server.ServerContainer");
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
