package cn.icodening.console.monitor.sql;

import java.util.List;

/**
 * @author icodening
 * @date 2021.07.28
 */
public class SQLFilterChain<T> {

    private final List<ProxyFilter> chain;

    private int position = -1;

    public SQLFilterChain(List<ProxyFilter> chain) {
        this.chain = chain;
    }

    public T filter(T wrapper) {
        if (++position < chain.size()) {
            return (T) chain.get(position).filter(this, wrapper);
        }
        return wrapper;
    }
}
