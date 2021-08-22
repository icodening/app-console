package cn.icodening.console;

import java.lang.instrument.Instrumentation;

/**
 * ClassLoader 使用 {@link cn.icodening.console.extension.ExtensionClassLoader} 加载扩展点
 * 扩展点的classpath下通常没有过多第三方依赖，扩展点仅能做简单的初始化动作
 *
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
