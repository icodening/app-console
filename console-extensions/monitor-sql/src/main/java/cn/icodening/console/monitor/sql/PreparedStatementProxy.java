package cn.icodening.console.monitor.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author icodening
 * @date 2021.07.28
 */
public class PreparedStatementProxy extends PreparedStatementProxyAdapter {

    public PreparedStatementProxy(PreparedStatement preparedStatement) {
        super(preparedStatement);
    }

    @Override
    public int executeUpdate() throws SQLException {
        return super.executeUpdate();
    }

    @Override
    public boolean execute() throws SQLException {
        return super.execute();
    }

    @Override
    public long executeLargeUpdate() throws SQLException {
        return super.executeLargeUpdate();
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        long begin = System.currentTimeMillis();
        ResultSet resultSet = super.executeQuery();
        long end = System.currentTimeMillis();
        String sql = getDynamicField().getSql();
        List<Object> parameters = getDynamicField().getParameters();
        String jdbcUrl = getConnectionProxy().getDataSourceProxy().getDynamicField().getJdbcUrl();
        System.out.println("本次连接为:" + jdbcUrl);
        System.out.println("sql:" + sql);
        System.out.println("参数:" + parameters);
        System.out.println("耗时:" + (end - begin) + " ms");
        return resultSet;
    }
}
