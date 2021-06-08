package cn.icodening.console.ratelimit;

import javax.servlet.http.HttpServletRequest;

/**
 * @author icodening
 * @date 2021.06.08
 */
public interface RateLimiter {

    boolean isAllow(HttpServletRequest request);
}
