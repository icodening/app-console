package cn.icodening.console.ratelimit;

import cn.icodening.console.common.model.PushData;
import cn.icodening.console.register.spring.PushDataReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import java.util.List;

/**
 * @author icodening
 * @date 2021.06.08
 */
public class RateLimitConfigReceivedEventListener implements ApplicationListener<PushDataReceivedEvent> {

    private static final String RECEIVE_TYPE = "RATE_LIMIT";

    @Autowired
    private List<RateLimiter> rateLimiters;

    @Override
    public void onApplicationEvent(PushDataReceivedEvent event) {
        final PushData pushData = event.getPushData();
        String type = pushData.getType();
        if (RECEIVE_TYPE.equalsIgnoreCase(type)) {
            for (RateLimiter rateLimiter : rateLimiters) {
                rateLimiter.refresh(pushData);
            }
            System.out.println(RateLimitConfigReceivedEventListener.class.getName() + ": 接收到 ratelimit 配置");
        }
    }
}
