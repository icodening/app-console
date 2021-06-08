package cn.icodening.console.register.spring;

import cn.icodening.console.injector.ClasspathInjector;

/**
 * @author icodening
 * @date 2021.06.06
 */
public class SpringRegisterClassPathConfigurer implements ClasspathInjector {

    private static final String DISPATCHER_SERVLET_CLASS = "org.springframework.web.servlet.DispatcherServlet";

    @Override
    public boolean shouldInject() {
        try {
            Class.forName(DISPATCHER_SERVLET_CLASS);
            return true;
        } catch (ClassNotFoundException e) {
            System.out.println(SpringRegisterClassPathConfigurer.class.getName() + ":" + e.getMessage());
            return false;
        }
    }
}
