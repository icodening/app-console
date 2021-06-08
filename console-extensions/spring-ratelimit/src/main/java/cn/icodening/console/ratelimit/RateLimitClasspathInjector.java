package cn.icodening.console.ratelimit;

import cn.icodening.console.injector.ClasspathInjector;

/**
 * @author icodening
 * @date 2021.06.07
 */
public class RateLimitClasspathInjector implements ClasspathInjector {

    private static final String SPRING_INTERCEPTOR_CLASS = "org.springframework.web.servlet.HandlerInterceptor";

    @Override
    public boolean shouldInject() {
        try {
            Class.forName(SPRING_INTERCEPTOR_CLASS);
            return true;
        } catch (ClassNotFoundException e) {
            System.out.println(RateLimitClasspathInjector.class.getName() + ":" + e.getMessage());
            return false;
        }
    }
}
