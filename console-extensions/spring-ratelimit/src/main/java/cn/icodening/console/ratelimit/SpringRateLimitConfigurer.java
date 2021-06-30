package cn.icodening.console.ratelimit;

import cn.icodening.console.injector.ModuleRegistry;
import cn.icodening.console.injector.ModuleRegistryConfigurer;
import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerFactory;

/**
 * @author icodening
 * @date 2021.06.07
 */
public class SpringRateLimitConfigurer implements ModuleRegistryConfigurer {

    private static final String SPRING_INTERCEPTOR_CLASS = "org.springframework.web.servlet.HandlerInterceptor";

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringRateLimitConfigurer.class);

    @Override
    public void configureRegistry(ModuleRegistry moduleRegistry) {
        try {
            Class.forName(SPRING_INTERCEPTOR_CLASS);
            moduleRegistry.registerCurrentModule();
        } catch (ClassNotFoundException e) {
            LOGGER.warn(e.getMessage());
        }
    }
}
