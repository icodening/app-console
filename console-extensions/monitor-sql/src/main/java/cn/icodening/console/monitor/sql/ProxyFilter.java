package cn.icodening.console.monitor.sql;

import cn.icodening.console.Sortable;
import cn.icodening.console.extension.Extensible;

/**
 * @author icodening
 * @date 2021.07.28
 */
@Extensible
public interface ProxyFilter<T> extends Sortable {

    /**
     * 过滤 java sql 对象
     *
     * @param wrapper 需要被过滤的java sql对象
     * @return 过滤后的java sql对象
     */
    T filter(SQLFilterChain<T> chain, T wrapper);


}
