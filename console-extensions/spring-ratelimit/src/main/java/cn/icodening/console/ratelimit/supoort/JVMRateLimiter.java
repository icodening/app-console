package cn.icodening.console.ratelimit.supoort;

import cn.icodening.console.ratelimit.RateLimiter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author icodening
 * @date 2021.06.08
 */
@Component
public class JVMRateLimiter implements RateLimiter {

    @Override
    public boolean isAllow(HttpServletRequest request) {
        return false;
    }
}
