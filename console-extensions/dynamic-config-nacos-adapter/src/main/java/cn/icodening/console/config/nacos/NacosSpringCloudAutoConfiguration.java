package cn.icodening.console.config.nacos;

import com.alibaba.cloud.nacos.NacosConfigBootstrapConfiguration;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * 适配spring cloud版本的nacos配置中心自动配置类
 *
 * @author icodening
 * @date 2021.08.03
 */
@ConditionalOnClass({NacosConfigBootstrapConfiguration.class})
@AutoConfigureAfter(NacosConfigBootstrapConfiguration.class)
public class NacosSpringCloudAutoConfiguration {

    @Bean
    public NacosSpringCloudConfigListener nacosSpringBootConfigListener(NacosConfigProperties nacosConfigProperties, NacosConfigManager nacosConfigManager) {
        return new NacosSpringCloudConfigListener(nacosConfigProperties, nacosConfigManager);
    }

}
