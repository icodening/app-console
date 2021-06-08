package cn.icodening.console.ratelimit;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author icodening
 * @date 2021.06.07
 */
public class RateLimitInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //FIXME LOG
        System.out.println(RateLimitInterceptor.class.getName() + ": rate limit interceptor enable!!!");
        //TODO get config by current uri
        return true;
    }
}
