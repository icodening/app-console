package cn.icodening.console.register.spring;

import org.springframework.context.annotation.Bean;

/**
 * @author icodening
 * @date 2021.06.08
 */
public class RegisterAutoConfiguration {

//    @Bean
//    public ServletRegistrationBean<ReceiveConfigServlet> receiveConfigServletServletRegistrationBean(ReceiveConfigServlet receiveConfigServlet) {
//        final ServletRegistrationBean<ReceiveConfigServlet> registrationBean = new ServletRegistrationBean<>(receiveConfigServlet);
//        registrationBean.addUrlMappings("/config_receiver");
//        return registrationBean;
//    }
//
//    @Bean
//    public ReceiveConfigServlet receiveConfigServlet() {
//        return new ReceiveConfigServlet();
//    }

    @Bean
    public ReceiverController receiverController() {
        System.out.println("配置 ReceiverController");
        return new ReceiverController();
    }
}
