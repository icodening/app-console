package cn.icodening.console.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author icodening
 * @date 2021.05.24
 */
@Entity
@Table(name = "ratelimit")
public class RateLimitEntity extends ConfigurableScopeEntity {

    private static final String CONFIG_TYPE = "RATE_LIMIT";

    @Column(name = "endpoint", nullable = false, length = 50)
    private String endpoint;

    @Column(name = "frequency", nullable = false, length = 10)
    private Integer frequency;

    @Column(name = "dimension", nullable = false, length = 10)
    private String dimension;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    @Override
    public String getConfigType() {
        return CONFIG_TYPE;
    }
}
