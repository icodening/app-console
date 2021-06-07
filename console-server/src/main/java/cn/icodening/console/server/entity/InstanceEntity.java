package cn.icodening.console.server.entity;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * @author icodening
 * @date 2021.05.24
 */
@Entity
@Table(name = "instance")
@DynamicUpdate
public class InstanceEntity extends AbstractEntity {

    @Column(name = "name", nullable = false, length = 32)
    private String name;

    @Column(name = "ip", nullable = false, length = 64)
    private String ip;

    @Column(name = "port", nullable = false, length = 5)
    private Integer port;

    @Column(name = "identity", nullable = true, length = 32)
    @NotNull
    private String identity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
