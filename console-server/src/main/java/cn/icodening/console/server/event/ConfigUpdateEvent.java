package cn.icodening.console.server.event;

import cn.icodening.console.common.entity.ConfigurableScopeEntity;
import org.springframework.context.ApplicationEvent;

/**
 * @author icodening
 * @date 2021.06.08
 */
public class ConfigUpdateEvent extends ApplicationEvent {

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public ConfigUpdateEvent(ConfigurableScopeEntity source) {
        super(source);
    }

    public ConfigurableScopeEntity getConfigurableScopeEntity() {
        return (ConfigurableScopeEntity) getSource();
    }
}
