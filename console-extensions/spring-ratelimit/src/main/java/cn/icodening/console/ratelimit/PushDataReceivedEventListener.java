package cn.icodening.console.ratelimit;

import cn.icodening.console.model.PushData;
import cn.icodening.console.register.spring.PushDataReceivedEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author icodening
 * @date 2021.06.08
 */
public class PushDataReceivedEventListener implements ApplicationListener<PushDataReceivedEvent> {

    @Override
    public void onApplicationEvent(PushDataReceivedEvent event) {
        final PushData pushData = event.getPushData();
        System.out.println("push data is : " + pushData);
        String type = pushData.getType();
        if ("ratelimit".equalsIgnoreCase(type)) {
            System.out.println(PushDataReceivedEventListener.class.getName() + ": 接收到 ratelimit 配置");
        }
    }
}
