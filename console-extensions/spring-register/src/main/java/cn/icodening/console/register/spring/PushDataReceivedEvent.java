package cn.icodening.console.register.spring;

import cn.icodening.console.model.PushData;
import org.springframework.context.ApplicationEvent;

/**
 * @author icodening
 * @date 2021.06.08
 */
public class PushDataReceivedEvent extends ApplicationEvent {

    private final PushData pushData;

    public PushDataReceivedEvent(PushData source) {
        super(source);
        this.pushData = source;
    }

    public PushData getPushData() {
        return pushData;
    }
}
