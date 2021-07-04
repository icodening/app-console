package cn.icodening.console.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * @author icodening
 * @date 2021.05.24
 */
@Entity
@Table(name = "instance")
public class InstanceEntity extends AbstractEntity {

    @Column(name = "application_name", nullable = false, length = 32)
    private String applicationName;

    @Column(name = "ip", nullable = false, length = 64)
    private String ip;

    @Column(name = "port", nullable = false, length = 5)
    private Integer port;

    @Column(name = "identity", nullable = true, length = 32)
    private String identity;

    @Column(name = "pid", nullable = true, length = 10)
    private String pid;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        InstanceEntity that = (InstanceEntity) object;
        return identity.equals(that.identity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identity);
    }
}
