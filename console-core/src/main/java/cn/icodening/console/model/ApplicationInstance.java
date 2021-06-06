package cn.icodening.console.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author icodening
 * @date 2021.06.05
 */
public class ApplicationInstance {

    private int port;

    private String ip;

    private String name;

    private String identity;

    private String pid;

    private Map<String, Object> attributes = new HashMap<>();

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private int port;

        private String ip;

        private String name;

        private String identity;

        private String pid;

        private Map<String, Object> attributes = new HashMap<>();

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder ip(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder identity(String identity) {
            this.identity = identity;
            return this;
        }

        public Builder pid(String pid) {
            this.pid = pid;
            return this;
        }

        public Builder attribute(String key, Object value) {
            this.attributes.put(key, value);
            return this;
        }

        public ApplicationInstance build() {
            ApplicationInstance applicationInstance = new ApplicationInstance();
            applicationInstance.setIp(ip);
            applicationInstance.setName(name);
            applicationInstance.setIdentity(identity);
            applicationInstance.setPort(port);
            applicationInstance.setPid(pid);
            applicationInstance.setAttributes(attributes);
            return applicationInstance;
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }


    @Override
    public String toString() {
        return "ApplicationInstance{" +
                "port=" + port +
                ", ip='" + ip + '\'' +
                ", name='" + name + '\'' +
                ", identity='" + identity + '\'' +
                ", pid='" + pid + '\'' +
                ", attributes=" + attributes +
                '}';
    }
}
