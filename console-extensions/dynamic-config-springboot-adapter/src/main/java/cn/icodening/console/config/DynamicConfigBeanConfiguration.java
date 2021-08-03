package cn.icodening.console.config;

import org.springframework.context.annotation.Bean;

/**
 *
 * @author icodening
 * @date 2021.07.10
 */
public class DynamicConfigBeanConfiguration {

    @Bean
    public ValueAnnotationBeanPostProcessor valueAnnotationBeanPostProcessor() {
        return new ValueAnnotationBeanPostProcessor();
    }
}
