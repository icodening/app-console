package cn.icodening.console.config.nacos;

import com.alibaba.boot.nacos.config.properties.NacosConfigProperties;
import com.alibaba.nacos.spring.context.annotation.config.NacosValueAnnotationBeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * 适配springboot版本的nacos配置中心自动配置类
 *
 * @author icodening
 * @date 2021.08.02
 */
@ConditionalOnClass({NacosValueAnnotationBeanPostProcessor.class})
public class NacosSpringBootAutoConfiguration {

    @Bean
    public NacosSpringBootConfigListener nacosSpringBootConfigListener(NacosConfigProperties nacosConfigProperties) {
        return new NacosSpringBootConfigListener(nacosConfigProperties);
    }

}
