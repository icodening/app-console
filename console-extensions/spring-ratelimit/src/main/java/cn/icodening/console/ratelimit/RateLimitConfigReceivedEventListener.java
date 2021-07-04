package cn.icodening.console.ratelimit;

import cn.icodening.console.common.entity.RateLimitEntity;
import cn.icodening.console.common.event.ServerMessageReceivedEvent;
import cn.icodening.console.common.model.ServerMessage;
import cn.icodening.console.event.ConsoleEventListener;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author icodening
 * @date 2021.06.08
 */
public class RateLimitConfigReceivedEventListener implements ConsoleEventListener<ServerMessageReceivedEvent> {

    private static final String RECEIVE_TYPE = RateLimitEntity.class.getName();

    @Resource
    private List<RateLimiter> rateLimiters = Collections.emptyList();

    @Override
    public void onEvent(ServerMessageReceivedEvent event) {
        ServerMessage source = event.getSource();
        if (RECEIVE_TYPE.equalsIgnoreCase(source.getType())) {
            for (RateLimiter rateLimiter : rateLimiters) {
                rateLimiter.refresh(source);
            }
        }
    }
}
