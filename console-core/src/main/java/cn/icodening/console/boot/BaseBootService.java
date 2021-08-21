package cn.icodening.console.boot;

import cn.icodening.console.AppConsoleException;

import java.lang.instrument.Instrumentation;

/**
 * @author icodening
 * @date 2021.05.22
 */
public abstract class BaseBootService implements BootService {

    @Override
    public void destroy() {

    }

    @Override
    public void initialize(String agentArgs, Instrumentation instrumentation) {

    }

    @Override
    public void start() throws AppConsoleException {

    }
}
