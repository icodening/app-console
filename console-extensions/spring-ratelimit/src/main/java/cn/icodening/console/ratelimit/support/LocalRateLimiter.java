package cn.icodening.console.ratelimit.support;

import cn.icodening.console.common.entity.RateLimitEntity;
import cn.icodening.console.common.model.InstanceConfigurationCache;
import cn.icodening.console.common.util.CalendarUtil;
import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerFactory;
import cn.icodening.console.ratelimit.RateLimiter;
import cn.icodening.console.ratelimit.cache.RateLimitCache;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * FIXME 设计简单粗暴，限流粒度大，后续改造
 * 实例限流器
 *
 * @author icodening
 * @date 2021.06.08
 */
public class LocalRateLimiter implements RateLimiter {

    private final Map<String, RateLimitCache> rateLimitCacheMap = new ConcurrentHashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalRateLimiter.class);

    @Override
    public synchronized void refresh() {
        List<RateLimitEntity> configurations = InstanceConfigurationCache.getConfigs(RateLimitEntity.class);
        LOGGER.debug("refresh ratelimit config list is: " + configurations);
        if (configurations == null || configurations.isEmpty()) {
            rateLimitCacheMap.clear();
            return;
        }
        try {
            for (RateLimitEntity rateLimitEntity : configurations) {
                if (!rateLimitEntity.getEnable()) {
                    continue;
                }
                Integer frequency = rateLimitEntity.getFrequency();
                String dimension = rateLimitEntity.getDimension();
                RateLimitCache rateLimitCache = new RateLimitCache(rateLimitEntity);
                Semaphore semaphore = new Semaphore(frequency);
                rateLimitCache.setSemaphore(semaphore);
                long now = System.currentTimeMillis();
                rateLimitCache.setStartTime(now);
                Integer field = CalendarUtil.getField(dimension);
                long endTime = CalendarUtil.addTime(field, 1);
                rateLimitCache.setAddTime(endTime - now);
                rateLimitCache.setEndTime(endTime);
                rateLimitCacheMap.put(rateLimitEntity.getEndpoint(), rateLimitCache);
            }
        } catch (Exception e) {
            LOGGER.warn("refresh rate limiter fail !!!");
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public boolean isAllow(HttpServletRequest request) {
        String bestPath = ((String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE));
        LOGGER.debug(request.getRequestURI() + " matching pattern: " + bestPath);
        final RateLimitCache rateLimitCache = rateLimitCacheMap.get(bestPath);
        if (rateLimitCache == null) {
            return true;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("cache hit by: " + bestPath);
        }
        long now = System.currentTimeMillis();
        if (now > rateLimitCache.getEndTime()) {
            rateLimitCache.refresh();
        }
        return now <= rateLimitCache.getEndTime() &&
                rateLimitCache.getSemaphore().tryAcquire();
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
