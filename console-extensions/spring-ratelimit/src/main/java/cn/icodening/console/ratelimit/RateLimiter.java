package cn.icodening.console.ratelimit;

import cn.icodening.console.common.model.PushData;

import javax.servlet.http.HttpServletRequest;

/**
 * @author icodening
 * @date 2021.06.08
 */
public interface RateLimiter {

    void refresh(PushData pushData);

    boolean isAllow(HttpServletRequest request);
}
