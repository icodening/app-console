package cn.icodening.console.ratelimit;

import cn.icodening.console.common.Refreshable;
import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;

/**
 * @author icodening
 * @date 2021.06.08
 */
public interface RateLimiter extends Refreshable, Ordered {

    boolean isAvailable();

    boolean isAllow(HttpServletRequest request);
}
