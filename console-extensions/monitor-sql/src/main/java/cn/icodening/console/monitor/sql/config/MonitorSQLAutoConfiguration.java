package cn.icodening.console.monitor.sql.config;

import cn.icodening.console.monitor.sql.BeanProxyProcessor;
import cn.icodening.console.monitor.sql.filter.ProxyFilterManager;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * @author icodening
 * @date 2021.07.28
 */
public class MonitorSQLAutoConfiguration {

    @Bean
    public BeanPostProcessor proxyDataSourceBean() {
        return BeanProxyProcessor.of(DataSource.class)
                .proxyBean(dataSource -> {
                    return ProxyFilterManager.getFilterChain(DataSource.class)
                            .filter(dataSource);
                })
                .buildProcessor();
    }
}
