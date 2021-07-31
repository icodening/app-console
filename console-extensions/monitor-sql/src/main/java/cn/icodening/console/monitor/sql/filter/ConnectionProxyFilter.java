package cn.icodening.console.monitor.sql.filter;

import cn.icodening.console.monitor.sql.ConnectionProxy;
import cn.icodening.console.monitor.sql.SQLFilterChain;

import java.sql.Connection;

/**
 * @author icodening
 * @date 2021.07.29
 */
public class ConnectionProxyFilter extends AbstractConnectionFilter {

    @Override
    public Connection filter(SQLFilterChain<Connection> chain, Connection connection) {
        return chain.filter(new ConnectionProxy(connection));
    }
}
