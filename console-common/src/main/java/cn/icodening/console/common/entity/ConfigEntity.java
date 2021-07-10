package cn.icodening.console.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author icodening
 * @date 2021.07.10
 */
@Entity
@Table(name = "config")
public class ConfigEntity extends ConfigurableScopeEntity {

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
