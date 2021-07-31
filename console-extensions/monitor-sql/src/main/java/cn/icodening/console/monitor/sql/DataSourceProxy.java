package cn.icodening.console.monitor.sql;

import cn.icodening.console.monitor.sql.filter.ProxyFilterManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author icodening
 * @date 2021.07.28
 */
public class DataSourceProxy extends DataSourceProxyAdapter {

    public DataSourceProxy(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Connection getConnection() throws SQLException {
        SQLFilterChain<Connection> filterChain = ProxyFilterManager.getFilterChain(Connection.class);
        Connection targetConnection = filterChain.filter(super.getConnection());
        if (targetConnection instanceof ConnectionProxy) {
            ((ConnectionProxy) targetConnection).setDataSourceProxy(this);
        }
        return targetConnection;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        SQLFilterChain<Connection> filterChain = ProxyFilterManager.getFilterChain(Connection.class);
        Connection targetConnection = filterChain.filter(super.getConnection(username, password));
        if (targetConnection instanceof ConnectionProxy) {
            ((ConnectionProxy) targetConnection).setDataSourceProxy(this);
        }
        return targetConnection;
    }
}
