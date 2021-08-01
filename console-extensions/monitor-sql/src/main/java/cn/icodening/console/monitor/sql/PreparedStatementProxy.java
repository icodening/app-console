package cn.icodening.console.monitor.sql;

import cn.icodening.console.common.vo.SQLMonitorData;

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
        long begin = System.currentTimeMillis();
        int execute = super.executeUpdate();
        long end = System.currentTimeMillis();
        postExecuteSQL(end - begin);
        return execute;
    }

    @Override
    public boolean execute() throws SQLException {
        long begin = System.currentTimeMillis();
        boolean execute = super.execute();
        long end = System.currentTimeMillis();
        postExecuteSQL(end - begin);
        return execute;
    }

    @Override
    public long executeLargeUpdate() throws SQLException {
        long begin = System.currentTimeMillis();
        long execute = super.executeLargeUpdate();
        long end = System.currentTimeMillis();
        postExecuteSQL(end - begin);
        return execute;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        long begin = System.currentTimeMillis();
        ResultSet resultSet = super.executeQuery();
        long end = System.currentTimeMillis();
        postExecuteSQL(end - begin);
        return resultSet;
    }

    private void postExecuteSQL(long costTime) {
        String sql = getDynamicField().getSql();
        List<Object> parameters = getDynamicField().getParameters();
        String jdbcUrl = getConnectionProxy().getDataSourceProxy().getDynamicField().getJdbcUrl();
        SQLMonitorData sqlMonitorData = new SQLMonitorData();
        sqlMonitorData.setSql(sql);
        sqlMonitorData.setCostTime(costTime);
        sqlMonitorData.setJdbcUrl(jdbcUrl);
        sqlMonitorData.setParameters(parameters);
        SQLMonitorDataRepository.addMonitorData(sqlMonitorData);
    }
}
