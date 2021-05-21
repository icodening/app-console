package cn.icodening.console.boot;

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
     */
    void start();

    /**
     * 销毁
     */
    void destroy();
}
