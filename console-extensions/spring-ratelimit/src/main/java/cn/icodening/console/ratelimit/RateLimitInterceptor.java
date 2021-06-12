package cn.icodening.console.ratelimit;

import cn.icodening.console.AppConsoleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

/**
 * @author icodening
 * @date 2021.06.07
 */
public class RateLimitInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private List<RateLimiter> rateLimiters = Collections.emptyList();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //FIXME LOG and foreach
        for (RateLimiter rateLimiter : rateLimiters) {
            if (rateLimiter.isAvailable()) {
                boolean allow = rateLimiter.isAllow(request);
                if (!allow) {
                    throw new AppConsoleException("当前请求已被限流! 请稍后再试");
                }
            }
        }
        return true;
    }
}
