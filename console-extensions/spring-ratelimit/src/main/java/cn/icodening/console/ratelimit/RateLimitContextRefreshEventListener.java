package cn.icodening.console.ratelimit;

import cn.icodening.console.common.event.ServerMessageReceivedEvent;
import cn.icodening.console.event.EventDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author icodening
 * @date 2021.06.12
 */
public class RateLimitContextRefreshEventListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private RateLimitConfigReceivedEventListener receivedEventListener;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        EventDispatcher.register(ServerMessageReceivedEvent.class, receivedEventListener);
    }
}
