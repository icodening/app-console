package cn.icodening.console.register.spring;

import cn.icodening.console.common.event.ApplicationInstanceStartedEvent;
import cn.icodening.console.common.model.ApplicationInstance;
import cn.icodening.console.event.EventDispatcher;
import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.management.ManagementFactory;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author icodening
 * @date 2021.06.29
 */
public class AgentStartInitialization implements InitializingBean, ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgentStartInitialization.class);

    private static final String SERVER_PORT_KEY = "server.port";
    private static final String APPLICATION_NAME_KEY = "spring.application.name";

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //1.启动agent
        SpringScope.getContext().setParent(applicationContext);
        SpringScope.getContext().refresh();
        EventDispatcher.dispatch(SpringStartAgentEventProvider.getSpringStartAgentEvent());

        //2.将当前应用注册到application console
        registerInstance();
    }


    private void registerInstance() {
        String localhost = getLocalhost();
        if (localhost == null) {
            LOGGER.info("localhost is null, dont't register");
            return;
        }
        String portString = applicationContext.getEnvironment().getProperty(SERVER_PORT_KEY);
        if (portString == null) {
            portString = "8080";
        }
        LOGGER.info("post is " + portString);
        ApplicationInstance.Builder builder = ApplicationInstance.newBuilder();
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.substring(0, name.indexOf("@"));
        String applicationName = applicationContext.getEnvironment().getProperty(APPLICATION_NAME_KEY);
        if (applicationName == null) {
            applicationName = localhost + ":" + portString + "@pid=" + pid;
        }
        ApplicationInstance instance = builder.ip(localhost)
                .port(Integer.parseInt(portString))
                .pid(pid)
                .applicationName(applicationName)
                .identity(localhost + ":" + portString)
                .build();
        EventDispatcher.dispatch(new ApplicationInstanceStartedEvent(instance));
    }

    private String getLocalhost() {
        Enumeration<NetworkInterface> networkInterfaces;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
            if (networkInterfaces == null) {
                return null;
            }
            while (networkInterfaces.hasMoreElements()) {
                final NetworkInterface networkInterface = networkInterfaces.nextElement();
                final Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    final InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ignore) {

        }
        return null;
    }
}
