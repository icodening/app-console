package cn.icodening.console.register.spring;

import cn.icodening.console.AgentStartEventProvider;
import cn.icodening.console.event.AgentStartEvent;

/**
 * @author icodening
 * @date 2021.06.27
 */
public class SpringStartAgentEventProvider implements AgentStartEventProvider {

    private static AgentStartEvent springStartAgentEvent;

    @Override
    public AgentStartEvent get() {
        return getSpringStartAgentEvent();
    }

    @Override
    public int getPriority() {
        return NORMAL_PRIORITY - 1;
    }

    public static AgentStartEvent getSpringStartAgentEvent() {
        if (springStartAgentEvent == null) {
            springStartAgentEvent = new SpringStartAgentEvent();
        }
        return springStartAgentEvent;
    }

    private static class SpringStartAgentEvent extends AgentStartEvent {

        public SpringStartAgentEvent() {
            super(new Object());
        }
    }
}
