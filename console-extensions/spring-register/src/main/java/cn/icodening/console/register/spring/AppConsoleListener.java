package cn.icodening.console.register.spring;

import cn.icodening.console.event.ApplicationInstanceStartedEvent;
import cn.icodening.console.event.EventDispatcher;
import cn.icodening.console.model.ApplicationInstance;
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

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        System.out.println(AppConsoleListener.class.getName() + ": ApplicationStartedEvent");
        ApplicationContext applicationContext = event.getApplicationContext();
        String portString = applicationContext.getEnvironment().getProperty("server.port");
        if (portString == null) {
            //FIXME
            portString = "8080";
            System.out.println(AppConsoleListener.class.getName() + ": post is null, will be use port 8080");
        }
        ApplicationInstance.Builder builder = ApplicationInstance.newBuilder();
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.substring(0, name.indexOf("@"));
        String localhost = getLocalhost();
        String applicationName = applicationContext.getEnvironment().getProperty("spring.application.name");
        if (applicationName == null) {
            applicationName = localhost + ":" + portString + "@pid=" + pid;
        }
        ApplicationInstance instance = builder.ip(localhost)
                .port(Integer.parseInt(portString))
                .pid(pid)
                .name(applicationName)
                .identity(localhost + ":" + portString)
                .build();
        EventDispatcher.dispatch(new ApplicationInstanceStartedEvent(instance));
        System.out.println(this.getClass().getName() + ": register instance");
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
