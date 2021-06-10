package cn.icodening.console.ratelimit;

import cn.icodening.console.common.entity.RateLimitEntity;
import cn.icodening.console.common.model.PushData;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author icodening
 * @date 2021.06.08
 */
public interface RateLimiter {

    void refresh(PushData<List<RateLimitEntity>> pushData);

    boolean isAllow(HttpServletRequest request);
}
