package cn.icodening.console.server.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author icodening
 * @date 2021.06.08
 */
public class ConfigUpdateEvent extends ApplicationEvent {

    private final String configType;

    private final String scope;

    private final String affectTarget;

    private final String action;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public ConfigUpdateEvent(String action, String configType, String scope, String affectTarget, Object source) {
        super(source);
        this.action = action;
        this.configType = configType;
        this.scope = scope;
        this.affectTarget = affectTarget;
    }

    public String getConfigType() {
        return configType;
    }

    public String getScope() {
        return scope;
    }

    public String getAffectTarget() {
        return affectTarget;
    }

    public String getAction() {
        return action;
    }
}
