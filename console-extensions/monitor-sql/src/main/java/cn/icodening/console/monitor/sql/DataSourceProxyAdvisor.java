package cn.icodening.console.monitor.sql;

import cn.icodening.console.monitor.sql.filter.ProxyFilterManager;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractGenericPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author icodening
 * @date 2021.08.23
 */
public class DataSourceProxyAdvisor extends AbstractGenericPointcutAdvisor {

    private static final String METHOD_NAME = "getConnection";

    private Map<DataSource, DataSourceProxy> dsProxyMap = new ConcurrentHashMap<>(2);

    public DataSourceProxyAdvisor() {
        setAdvice(new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                SQLFilterChain<Connection> filterChain = ProxyFilterManager.getFilterChain(Connection.class);
                Connection proceed = (Connection) invocation.proceed();
                Connection targetConnection = filterChain.filter(proceed);
                Object ds = invocation.getThis();
                if (ds instanceof DataSource && targetConnection instanceof ConnectionProxy) {
                    DataSourceProxy dataSourceProxy = dsProxyMap.get(ds);
                    if (dataSourceProxy == null) {
                        DataSource dsProxy = ProxyFilterManager.getFilterChain(DataSource.class)
                                .filter((DataSource) ds);
                        dsProxyMap.putIfAbsent((DataSource) ds, (DataSourceProxy) dsProxy);
                        dataSourceProxy = (DataSourceProxy) dsProxy;
                    }
                    ((ConnectionProxy) targetConnection).setDataSourceProxy(dataSourceProxy);
                }
                return targetConnection;
            }
        });
    }

    @Override
    public Pointcut getPointcut() {
        return new StaticMethodMatcherPointcutAdvisor() {
            @Override
            public boolean matches(Method method, Class<?> targetClass) {
                if (!DataSource.class.isAssignableFrom(targetClass)) {
                    return false;
                }
                return METHOD_NAME.equals(method.getName());
            }
        };
    }
}
