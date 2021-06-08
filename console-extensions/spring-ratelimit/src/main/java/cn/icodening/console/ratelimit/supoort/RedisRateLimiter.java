package cn.icodening.console.ratelimit.supoort;

import cn.icodening.console.ratelimit.RateLimiter;

import javax.servlet.http.HttpServletRequest;

/**
 * @author icodening
 * @date 2021.06.08
 */
public class RedisRateLimiter implements RateLimiter {
    @Override
    public boolean isAllow(HttpServletRequest request) {
        return true;
    }
}
