package cn.icodening.console.monitor.sql;

import cn.icodening.console.monitor.sql.define.DataSourceInfo;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * @author icodening
 * @date 2021.07.28
 */
public abstract class DataSourceProxyAdapter extends AbstractWrapperProxy implements DataSource, ProxyInstance<DataSourceInfo> {

    private DataSourceInfo dataSourceInfo = new DataSourceInfo();

    @Override
    public void setDynamicField(DataSourceInfo dataSourceInfo) {
        this.dataSourceInfo = dataSourceInfo;
    }

    @Override
    public DataSourceInfo getDynamicField() {
        return dataSourceInfo;
    }

    public DataSourceProxyAdapter(DataSource dataSource) {
        super(dataSource);
    }

    public DataSource getDataSource() {
        return (DataSource) super.getRaw();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getDataSource().getConnection(username, password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return getDataSource().getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        getDataSource().setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        getDataSource().setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return getDataSource().getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return getDataSource().getParentLogger();
    }
}
