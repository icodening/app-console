package cn.icodening.console.monitor.sql.filter;

import cn.icodening.console.Sortable;
import cn.icodening.console.monitor.sql.DataSourceManager;
import cn.icodening.console.monitor.sql.DataSourceProxy;
import cn.icodening.console.monitor.sql.SQLFilterChain;

import javax.sql.DataSource;

/**
 * @author icodening
 * @date 2021.07.31
 */
public class DataSourceProxyFilter extends AbstractProxyFilter<DataSource> {

    @Override
    public DataSource filter(SQLFilterChain<DataSource> chain, DataSource wrapper) {
        DataSourceProxy dataSourceProxy = new DataSourceProxy(wrapper);
        DataSourceManager.addDataSource(dataSourceProxy);
        return chain.filter(dataSourceProxy);
    }

    @Override
    public int getPriority() {
        return Sortable.MAX_PRIORITY;
    }
}
