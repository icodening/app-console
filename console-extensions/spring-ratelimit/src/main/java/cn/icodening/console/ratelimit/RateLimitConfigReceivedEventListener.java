package cn.icodening.console.ratelimit;

import cn.icodening.console.common.entity.RateLimitEntity;
import cn.icodening.console.common.event.ServerMessageReceivedEvent;
import cn.icodening.console.common.model.ServerMessage;
import cn.icodening.console.event.ConsoleEventListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

/**
 * @author icodening
 * @date 2021.06.08
 */
public class RateLimitConfigReceivedEventListener implements ConsoleEventListener<ServerMessageReceivedEvent> {

    private static final String RECEIVE_TYPE = RateLimitEntity.class.getName();

    private static Long lastSendTimestamp;

    @Autowired
    private List<RateLimiter> rateLimiters = Collections.emptyList();

    @Override
    public void onEvent(ServerMessageReceivedEvent event) {
        ServerMessage source = event.getSource();
        //简单幂等校验 上次数据包发送时间戳 >= 本次收到的数据包时间戳 则认为数据包过期丢弃
        if (lastSendTimestamp == null) {
            lastSendTimestamp = event.getTimestamp();
        }
        if (lastSendTimestamp > event.getTimestamp()) {
            return;
        }
        lastSendTimestamp = event.getTimestamp();
        if (RECEIVE_TYPE.equalsIgnoreCase(source.getType())) {
            for (RateLimiter rateLimiter : rateLimiters) {
                rateLimiter.refresh();
            }
        }
    }
}
