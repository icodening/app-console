package cn.icodening.console.register.spring;

import cn.icodening.console.injector.ClasspathInjector;
import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerFactory;
import org.springframework.boot.SpringApplication;

/**
 * @author icodening
 * @date 2021.06.06
 */
public class SpringRegisterClassPathConfigurer implements ClasspathInjector {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringRegisterClassPathConfigurer.class);

    @Override
    public boolean shouldInject() {
        try {
            Class.forName(SpringApplication.DEFAULT_SERVLET_WEB_CONTEXT_CLASS);
            return true;
        } catch (ClassNotFoundException e) {
            LOGGER.warn(e.getMessage());
            return false;
        }
    }
}
