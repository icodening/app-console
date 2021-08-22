package cn.icodening.console.boot.spring;

import cn.icodening.console.AgentInitializer;
import cn.icodening.console.util.ClassUtil;

import java.lang.instrument.Instrumentation;

/**
 * @author icodening
 * @date 2021.08.22
 */
public class SpringBootDelayAgentInitializer implements AgentInitializer {

    private static final String DELAY_START_KEY = "delay.start.agent";

    private static final String LAUNCHER_CLASS_NAME = "org.springframework.boot.loader.Launcher";

    @Override
    public void initialize(String agentArgs, Instrumentation instrumentation) {
        if (ClassUtil.exists(LAUNCHER_CLASS_NAME)) {
            System.setProperty(DELAY_START_KEY, "true");
        }
    }
}
