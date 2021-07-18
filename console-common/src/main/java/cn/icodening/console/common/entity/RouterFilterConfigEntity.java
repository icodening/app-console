package cn.icodening.console.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * 路由实例过滤
 *
 * @author icodening
 * @date 2021.07.17
 */
@Entity
@Table(name = "router_filter_config")
public class RouterFilterConfigEntity extends ConfigurableScopeEntity {

    /**
     * 原始调用服务名
     */
    @Column(name = "service_id", nullable = false, length = 20)
    private String serviceId;

    /**
     * key来源，header、query
     */
    @Column(name = "key_source", nullable = false, length = 10)
    private String keySource;

    /**
     * 需要寻找的key
     */
    @Column(name = "key_name", nullable = false, length = 20)
    private String keyName;

    /**
     * 精确匹配、正则匹配
     */
    @Column(name = "match_type", nullable = false, length = 15)
    private String matchType;

    /**
     * 需要精确匹配的值，或正则匹配的值
     */
    @Column(name = "expression", nullable = false, length = 20)
    private String expression;

    /**
     * 需要过滤的类型 metadata、host or other
     */
    @Column(name = "filter_type", nullable = false, length = 15)
    private String filterType;

    /**
     * server特征Key
     */
    @Column(name = "signature_key", nullable = true, length = 20)
    private String signatureKey;

    /**
     * 需要选中的server特征
     */
    @Column(name = "server_instance_signature", nullable = false, length = 20)
    private String serverInstanceSignature;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getKeySource() {
        return keySource;
    }

    public void setKeySource(String keySource) {
        this.keySource = keySource;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public String getSignatureKey() {
        return signatureKey;
    }

    public void setSignatureKey(String signatureKey) {
        this.signatureKey = signatureKey;
    }

    public String getServerInstanceSignature() {
        return serverInstanceSignature;
    }

    public void setServerInstanceSignature(String serverInstanceSignature) {
        this.serverInstanceSignature = serverInstanceSignature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouterFilterConfigEntity that = (RouterFilterConfigEntity) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
