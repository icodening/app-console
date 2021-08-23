package cn.icodening.console.monitor.sql.config;

import cn.icodening.console.common.constants.URLConstants;
import cn.icodening.console.monitor.sql.BeanProxyProcessor;
import cn.icodening.console.monitor.sql.DataSourceProxyAdvisor;
import cn.icodening.console.monitor.sql.SQLMonitorServlet;
import cn.icodening.console.monitor.sql.filter.ProxyFilterManager;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * @author icodening
 * @date 2021.07.28
 */
@ConditionalOnClass(name = "javax.servlet.Servlet")
public class MonitorSQLAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(type = {"org.springframework.aop.framework.AopInfrastructureBean"})
    public BeanPostProcessor proxyDataSourceBean() {
        return BeanProxyProcessor.of(DataSource.class)
                .proxyBean(dataSource -> {
                    return ProxyFilterManager.getFilterChain(DataSource.class)
                            .filter(dataSource);
                })
                .buildProcessor();
    }

    @Bean
    @ConditionalOnBean(type = {"org.springframework.aop.framework.AopInfrastructureBean"})
    public DataSourceProxyAdvisor dataSourceProxyAdvisor() {
        return new DataSourceProxyAdvisor();
    }

    @Bean
    public ServletRegistrationBean<SQLMonitorServlet> sqlMonitorServletRegistrationBean(SQLMonitorServlet sqlMonitorServlet) {
        ServletRegistrationBean<SQLMonitorServlet> registrationBean = new ServletRegistrationBean<>(sqlMonitorServlet);
        registrationBean.addUrlMappings(URLConstants.INSTANCE_SQLMONITOR_URL);
        return registrationBean;
    }

    @Bean
    public SQLMonitorServlet sqlMonitorServlet() {
        return new SQLMonitorServlet();
    }
}
