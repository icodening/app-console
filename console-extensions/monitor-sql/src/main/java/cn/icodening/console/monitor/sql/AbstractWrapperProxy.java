package cn.icodening.console.monitor.sql;

import java.sql.SQLException;
import java.sql.Wrapper;

/**
 * @author icodening
 * @date 2021.07.28
 */
public abstract class AbstractWrapperProxy implements Wrapper {

    protected final Wrapper wrapper;

    public AbstractWrapperProxy(Wrapper wrapper) {
        this.wrapper = wrapper;
    }

    protected Wrapper getRaw() {
        return wrapper;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface == null
                || iface != wrapper.getClass()) {
            return null;
        }
        if (iface == getClass()) {
            return (T) this;
        }
        return wrapper.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        if (iface == null
                || iface != getClass()) {
            return false;
        }

        return wrapper.isWrapperFor(iface);
    }
}
