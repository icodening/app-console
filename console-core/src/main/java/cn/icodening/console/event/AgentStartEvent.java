package cn.icodening.console.event;

import java.lang.instrument.Instrumentation;

/**
 * agent启动事件
 * TODO 按照当前设计，该类应该是不需要了，考虑删除
 *
 * @author icodening
 * @date 2021.06.27
 */
@Deprecated
public abstract class AgentStartEvent extends AppConsoleEvent {

    /**
     * agent启动前执行单元，优先级高于所有BootService, 可用于简单前置初始化
     */
    private AgentPreStartInvocation execution = (args, instrumentation) -> {
    };

    public AgentStartEvent(Object source) {
        super(source);
    }

    public void invoke(String args, Instrumentation instrumentation) {
        execution.invoke(args, instrumentation);
    }

    public void setExecution(AgentPreStartInvocation execution) {
        if (execution == null) {
            return;
        }
        this.execution = execution;
    }

    @FunctionalInterface
    public static interface AgentPreStartInvocation {

        void invoke(String args, Instrumentation instrumentation);
    }

}
