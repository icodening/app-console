package cn.icodening.console.log;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

/**
 * @author icodening
 * @date 2021.07.12
 */
public class InstanceLogBeanConfiguration {

    @Bean
    public ApplicationLogWebSocketEndpoint applicationLogWebSocketEndpoint() {
        return new ApplicationLogWebSocketEndpoint();
    }

    @Bean
    @ConditionalOnMissingClass("org.springframework.web.socket.server.standard.ServerEndpointExporter")
    public WebSocketEndpointExporter webSocketEndpointExporter() {
        return new WebSocketEndpointExporter();
    }

    @Bean
    public ServletListenerRegistrationBean<ContextLoaderListener> contextLoaderListenerBean(WebApplicationContext applicationContext) {
        ServletContext servletContext = applicationContext.getServletContext();
        if (servletContext != null) {
            servletContext.removeAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        }
        ServletListenerRegistrationBean<ContextLoaderListener> servletListenerRegistrationBean = new ServletListenerRegistrationBean<>();
        ContextLoaderListener contextLoaderListener = new ContextLoaderListener(applicationContext);
        servletListenerRegistrationBean.setListener(contextLoaderListener);
        return servletListenerRegistrationBean;
    }
}
