package cn.icodening.console.server.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author icodening
 * @date 2021.06.07
 */
@MappedSuperclass
public abstract class ConfigurableScopeEntity extends AbstractEntity {

    /**
     * 作用范围: group、application、instance
     */
    @Column(name = "scope", nullable = false, length = 50)
    private String scope;

    /**
     * 影响目标, 分组名、应用名、实例id
     */
    @Column(name = "affect_target", nullable = false, length = 50)
    private String affectTarget;

    /**
     * 功能开关，启用、禁用
     */
    @Column(name = "enable", nullable = false)
    private Boolean enable;

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAffectTarget() {
        return affectTarget;
    }

    public void setAffectTarget(String affectTarget) {
        this.affectTarget = affectTarget;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
