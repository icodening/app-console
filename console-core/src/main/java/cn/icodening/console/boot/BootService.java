package cn.icodening.console.boot;

import cn.icodening.console.AgentInitializer;
import cn.icodening.console.AppConsoleException;
import cn.icodening.console.Sortable;

/**
 * @author icodening
 * @date 2021.05.20
 */
public interface BootService extends AgentInitializer, Sortable {


    /**
     * 启动
     *
     * @throws AppConsoleException 所有异常
     */
    void start() throws AppConsoleException;

    /**
     * 销毁
     *
     * @throws AppConsoleException 所有异常
     */
    void destroy() throws AppConsoleException;
}
