package cn.icodening.console.ratelimit;

import cn.icodening.console.injector.ModuleRegistry;
import cn.icodening.console.injector.ModuleRegistryConfigurer;
import cn.icodening.console.util.ClassUtil;

/**
 * @author icodening
 * @date 2021.06.07
 */
public class SpringRateLimitConfigurer implements ModuleRegistryConfigurer {

    private static final String SPRING_INTERCEPTOR_CLASS = "org.springframework.web.servlet.HandlerInterceptor";

    @Override
    public void configureRegistry(ModuleRegistry moduleRegistry) {
        boolean existsSpringHandlerInterceptor = ClassUtil.exists(SPRING_INTERCEPTOR_CLASS);
        if (existsSpringHandlerInterceptor) {
            moduleRegistry.registerCurrentModule();
        }
    }
}
