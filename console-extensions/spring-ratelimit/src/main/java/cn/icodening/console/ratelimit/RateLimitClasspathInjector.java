package cn.icodening.console.ratelimit;

import cn.icodening.console.injector.ClasspathInjector;
import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerFactory;
import cn.icodening.console.register.spring.SpringRegisterClassPathConfigurer;

/**
 * @author icodening
 * @date 2021.06.07
 */
public class RateLimitClasspathInjector implements ClasspathInjector {

    private static final String SPRING_INTERCEPTOR_CLASS = "org.springframework.web.servlet.HandlerInterceptor";

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringRegisterClassPathConfigurer.class);

    @Override
    public boolean shouldInject() {
        try {
            Class.forName(SPRING_INTERCEPTOR_CLASS);
            return true;
        } catch (ClassNotFoundException e) {
            LOGGER.warn(e.getMessage());
            return false;
        }
    }
}
