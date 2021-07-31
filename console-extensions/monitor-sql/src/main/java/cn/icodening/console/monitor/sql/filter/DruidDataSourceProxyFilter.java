package cn.icodening.console.monitor.sql.filter;

import cn.icodening.console.monitor.sql.DataSourceProxy;
import cn.icodening.console.monitor.sql.SQLFilterChain;
import cn.icodening.console.monitor.sql.define.DataSourceInfo;
import org.springframework.util.ReflectionUtils;

import javax.sql.DataSource;
import java.lang.reflect.Method;

/**
 * @author icodening
 * @date 2021.07.31
 */
public class DruidDataSourceProxyFilter extends AbstractProxyFilter<DataSource> {

    private static final String DRUID_DATASOURCE_CLASS = "com.alibaba.druid.pool.DruidDataSource";

    @Override
    public DataSource filter(SQLFilterChain<DataSource> chain, DataSource wrapper) {
        if (wrapper instanceof DataSourceProxy) {
            DataSource dataSource = ((DataSourceProxy) wrapper).getDataSource();
            Class<? extends DataSource> clz = dataSource.getClass();
            if (DRUID_DATASOURCE_CLASS.equals(clz.getName())) {
                try {
                    Method getJdbcUrl = clz.getMethod("getRawJdbcUrl");
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
