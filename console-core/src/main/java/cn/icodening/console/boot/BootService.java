package cn.icodening.console.boot;

import cn.icodening.console.AppConsoleException;
import cn.icodening.console.Initializer;
import cn.icodening.console.Sortable;
import cn.icodening.console.extension.Extensible;

/**
 * @author icodening
 * @date 2021.05.20
 */
@Extensible
public interface BootService extends Initializer, Sortable {


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
