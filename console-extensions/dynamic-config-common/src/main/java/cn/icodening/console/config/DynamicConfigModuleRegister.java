package cn.icodening.console.config;

import cn.icodening.console.Sortable;

/**
 * 按优先级排序，选择最优配置中心适配模块
 *
 * @author icodening
 * @date 2021.08.03
 */
public interface DynamicConfigModuleRegister extends Sortable {

    /**
     * 是否应该注册当前
     *
     * @return true表示注册，false不注册
     */
    boolean shouldRegister();
}
