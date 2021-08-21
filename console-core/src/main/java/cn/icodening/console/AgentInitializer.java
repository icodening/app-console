package cn.icodening.console;

import java.lang.instrument.Instrumentation;

/**
 * @author icodening
 * @date 2021.05.20
 */
public interface AgentInitializer {

    /**
     * 初始化
     *
     * @param agentArgs 启动时的premain args
     */
    void initialize(String agentArgs, Instrumentation instrumentation);
}
