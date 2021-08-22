package cn.icodening.console;

import java.lang.instrument.Instrumentation;

/**
 * @author icodening
 * @date 2021.05.20
 */
public interface AgentInitializer extends Sortable {

    /**
     * 初始化
     *
     * @param agentArgs       启动时的premain args
     * @param instrumentation 桩
     */
    void initialize(String agentArgs, Instrumentation instrumentation);
}
