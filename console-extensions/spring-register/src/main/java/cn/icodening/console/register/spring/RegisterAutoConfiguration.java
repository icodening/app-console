package cn.icodening.console.register.spring;

import cn.icodening.console.common.constants.URLConstants;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * @author icodening
 * @date 2021.06.08
 */
public class RegisterAutoConfiguration {

    @Bean
    public ServletRegistrationBean<ReceiveConfigServlet> receiveConfigServletRegistrationBean(ReceiveConfigServlet receiveConfigServlet) {
        final ServletRegistrationBean<ReceiveConfigServlet> registrationBean = new ServletRegistrationBean<>(receiveConfigServlet);
        registrationBean.addUrlMappings(URLConstants.INSTANCE_RECEIVE_URL);
        return registrationBean;
    }

    @Bean
    public ReceiveConfigServlet receiveConfigServlet() {
        return new ReceiveConfigServlet();
    }

    @Bean
    public AgentStartInitialization agentStartInitialization() {
        return new AgentStartInitialization();
    }

}
