package cn.icodening.console.register.spring;

import cn.icodening.console.common.event.ApplicationInstanceStartedEvent;
import cn.icodening.console.common.model.ApplicationInstance;
import cn.icodening.console.event.EventDispatcher;
import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;

import java.lang.management.ManagementFactory;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author icodening
 * @date 2021.06.06
 */
public class AppConsoleListener implements ApplicationListener<ApplicationStartedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppConsoleListener.class);

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        String localhost = getLocalhost();
        if (localhost == null) {
            LOGGER.info("localhost is null, dont't register");
            return;
        }
        ApplicationContext applicationContext = event.getApplicationContext();
        String portString = applicationContext.getEnvironment().getProperty("server.port");
        if (portString == null) {
            portString = "8080";
        }
        LOGGER.info("post is " + portString);
        ApplicationInstance.Builder builder = ApplicationInstance.newBuilder();
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.substring(0, name.indexOf("@"));
        String applicationName = applicationContext.getEnvironment().getProperty("spring.application.name");
        if (applicationName == null) {
            applicationName = localhost + ":" + portString + "@pid=" + pid;
        }
        ApplicationInstance instance = builder.ip(localhost)
                .port(Integer.parseInt(portString))
                .pid(pid)
                .applicationName(applicationName)
                .identity(localhost + ":" + portString)
                .build();
        //使用Spring Context接管所有Extension，享有Spring DI特性
        AppConsoleSpringContext appConsoleSpringContext = SpringScope.getContext();
        appConsoleSpringContext.setParent(event.getApplicationContext());
        EventDispatcher.dispatch(new SpringContextRefreshedEvent(event.getApplicationContext()));
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
