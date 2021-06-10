package cn.icodening.console.ratelimit.supoort;

import cn.icodening.console.common.entity.RateLimitEntity;
import cn.icodening.console.common.model.PushData;
import cn.icodening.console.ratelimit.RateLimiter;
import cn.icodening.console.ratelimit.cache.RateLimitCache;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * 实例限流器
 *
 * @author icodening
 * @date 2021.06.08
 */
public class LocalRateLimiter implements RateLimiter {

    private final Map<String, RateLimitCache> rateLimitCacheMap = new ConcurrentHashMap<>();

    @Override
    public synchronized void refresh(PushData<List<RateLimitEntity>> pushData) {
        System.out.println(LocalRateLimiter.class.getName() + ": refresh");
        List<RateLimitEntity> rateLimitEntityList = pushData.getData();
        for (RateLimitEntity rateLimitEntity : rateLimitEntityList) {
            Integer frequency = rateLimitEntity.getFrequency();
            Boolean enable = rateLimitEntity.getEnable();
            String endpoint = rateLimitEntity.getEndpoint();
            String dimension = rateLimitEntity.getDimension();
            RateLimitCache rateLimitCache = new RateLimitCache();
            Semaphore semaphore = new Semaphore(frequency);
            rateLimitCache.setDimension(dimension);
            rateLimitCache.setEndpoint(endpoint);
            rateLimitCache.setEnable(enable);
            rateLimitCache.setFrequency(frequency);
            rateLimitCache.setSemaphore(semaphore);
            long now = System.currentTimeMillis();
            long endTime = 0;
            rateLimitCache.setStartTime(now);
            final Calendar cal = Calendar.getInstance();
            switch (dimension) {
                case "YEAR": {
                    cal.add(Calendar.YEAR, 1);
                    endTime = cal.getTimeInMillis();
                    break;
                }
                case "MONTH": {
                    cal.add(Calendar.MONTH, 1);
                    endTime = cal.getTimeInMillis();
                    break;
                }
                case "DAY": {
                    cal.add(Calendar.DATE, 1);
                    endTime = cal.getTimeInMillis();
                    break;
                }
                case "HOUR": {
                    cal.add(Calendar.HOUR, 1);
                    endTime = cal.getTimeInMillis();
                    break;
                }
                case "MINUTE": {
                    cal.add(Calendar.MINUTE, 1);
                    endTime = cal.getTimeInMillis();
                    break;
                }
                case "SECOND": {
                    cal.add(Calendar.SECOND, 1);
                    endTime = cal.getTimeInMillis();
                    break;
                }
            }
            rateLimitCache.setAddTime(endTime - now);
            rateLimitCache.setEndTime(endTime);
            rateLimitCacheMap.put(rateLimitCache.getEndpoint(), rateLimitCache);
        }
//        Integer frequency = (Integer) data.get("frequency");
//        Boolean enable = (Boolean) data.get("enable");
//        String endpoint = (String) data.get("endpoint");
//        String dimension = (String) data.get("dimension");
//
//        System.out.println(JVMRateLimiter.class.getName() + " map is :" + rateLimitCacheMap);
    }

    @Override
    public boolean isAllow(HttpServletRequest request) {
        String bestPath = ((String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE));
        System.out.println(LocalRateLimiter.class.getName() + ": best path is " + bestPath);
        final RateLimitCache rateLimitCache = rateLimitCacheMap.get(bestPath);
        System.out.println("RateLimitCache is : " + rateLimitCache);
        if (rateLimitCache == null) {
            return true;
        }
        long now = System.currentTimeMillis();
        if (now > rateLimitCache.getEndTime()) {
            rateLimitCache.refresh();
        }
        return now <= rateLimitCache.getEndTime() &&
                rateLimitCache.getSemaphore().tryAcquire();
    }
}
