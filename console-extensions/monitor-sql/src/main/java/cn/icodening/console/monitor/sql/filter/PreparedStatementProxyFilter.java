package cn.icodening.console.monitor.sql.filter;

import cn.icodening.console.Sortable;
import cn.icodening.console.monitor.sql.PreparedStatementProxy;
import cn.icodening.console.monitor.sql.SQLFilterChain;

import java.sql.PreparedStatement;

/**
 * @author icodening
 * @date 2021.07.29
 */
public class PreparedStatementProxyFilter extends AbstractPreparedStatementFilter {

    @Override
    public PreparedStatement filter(SQLFilterChain<PreparedStatement> chain, PreparedStatement preparedStatement) {
        return chain.filter(new PreparedStatementProxy(preparedStatement));
    }

    @Override
    public int getPriority() {
        return Sortable.MAX_PRIORITY;
    }
}
