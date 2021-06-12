package cn.icodening.console.common.entity;

/**
 * @author icodening
 * @date 2021.06.12
 */
public interface ConfigurationType {

    default String configType() {
        return this.getClass().getName();
    }
}
