package cn.icodening.config;

import org.springframework.context.annotation.Bean;

/**
 * TODO 缺少启动前的环境变量配置
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
