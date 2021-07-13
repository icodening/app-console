package cn.icodening.console.server.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.servlet.ServletContext;
import java.time.Duration;
import java.util.List;

/**
 * @author icodening
 * @date 2021.05.30
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.removeIf(next -> next instanceof StringHttpMessageConverter);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(200))
                .setReadTimeout(Duration.ofMillis(5000))
                .build();
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
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
