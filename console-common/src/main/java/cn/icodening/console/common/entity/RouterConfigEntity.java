package cn.icodening.console.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * @author icodening
 * @date 2021.07.17
 */
@Entity
@Table(name = "router_config")
public class RouterConfigEntity extends ConfigurableScopeEntity {

    /**
     * 原始调用服务名
     */
    @Column(name = "origin_service", nullable = false, length = 20)
    private String originService;

    /**
     * 目标调用服务名
     */
    @Column(name = "target_service", nullable = false, length = 20)
    private String targetService;

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

    public String getOriginService() {
        return originService;
    }

    public void setOriginService(String originService) {
        this.originService = originService;
    }

    public String getTargetService() {
        return targetService;
    }

    public void setTargetService(String targetService) {
        this.targetService = targetService;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouterConfigEntity that = (RouterConfigEntity) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
