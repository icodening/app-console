package cn.icodening.console.monitor.sql.filter;

import cn.icodening.console.monitor.sql.DataSourceProxy;
import cn.icodening.console.monitor.sql.SQLFilterChain;
import cn.icodening.console.monitor.sql.define.DataSourceInfo;
import org.springframework.util.ReflectionUtils;

import javax.sql.DataSource;
import java.lang.reflect.Method;

/**
 * FIXME duplicate code
 *
 * @author icodening
 * @date 2021.07.31
 */
public class HikariDataSourceProxyFilter extends AbstractProxyFilter<DataSource> {

    private static final String HIKARI_DATASOURCE_CLASS = "com.zaxxer.hikari.HikariDataSource";

    @Override
    public DataSource filter(SQLFilterChain<DataSource> chain, DataSource wrapper) {
        if (wrapper instanceof DataSourceProxy) {
            DataSource dataSource = ((DataSourceProxy) wrapper).getDataSource();
            Class<? extends DataSource> clz = dataSource.getClass();
            if (HIKARI_DATASOURCE_CLASS.equals(clz.getName())) {
                try {
                    Method getJdbcUrl = clz.getMethod("getJdbcUrl");
                    ReflectionUtils.makeAccessible(getJdbcUrl);
                    String jdbcUrl = (String) ReflectionUtils.invokeMethod(getJdbcUrl, dataSource);
                    DataSourceInfo dynamicField = ((DataSourceProxy) wrapper).getDynamicField();
                    if (dynamicField == null) {
                        dynamicField = new DataSourceInfo();
                        ((DataSourceProxy) wrapper).setDynamicField(dynamicField);
                    }
                    dynamicField.setJdbcUrl(jdbcUrl);
                } catch (Throwable ignore) {
                }
            }
        }
        return chain.filter(wrapper);
    }
}
