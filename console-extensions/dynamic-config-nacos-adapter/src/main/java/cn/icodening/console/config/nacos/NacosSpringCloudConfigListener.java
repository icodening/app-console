package cn.icodening.console.config.nacos;

import cn.icodening.console.config.ConfigChangeEvent;
import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerFactory;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.context.ApplicationListener;
import org.springframework.util.StringUtils;

import java.io.StringReader;
import java.util.Properties;
import java.util.Set;

/**
 * @author icodening
 * @date 2021.08.03
 */
public class NacosSpringCloudConfigListener implements ApplicationListener<ConfigChangeEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NacosSpringCloudConfigListener.class);

    private final NacosConfigProperties nacosConfigProperties;

    private final NacosConfigManager nacosConfigManager;

    public NacosSpringCloudConfigListener(NacosConfigProperties nacosConfigProperties, NacosConfigManager nacosConfigManager) {
        this.nacosConfigProperties = nacosConfigProperties;
        this.nacosConfigManager = nacosConfigManager;
    }

    @Override
    public void onApplicationEvent(ConfigChangeEvent event) {
        Properties source = event.getSource();
        String name = nacosConfigProperties.getName();
        String dataId = nacosConfigProperties.getPrefix();
        if (StringUtils.isEmpty(dataId)) {
            dataId = name;
        }

        if (StringUtils.isEmpty(dataId)) {
            dataId = nacosConfigProperties.getEnvironment().getProperty("spring.application.name");
        }
        String group = nacosConfigProperties.getGroup();
        ConfigService configService = nacosConfigManager.getConfigService();
        try {
            String config = configService.getConfig(dataId, group, Long.getLong("nacos.default.timeout", 5000L));
            Properties properties = new Properties();
            properties.load(new StringReader(config));
            properties.putAll(source);
            StringBuilder sb = new StringBuilder();
            Set<String> propertyNames = properties.stringPropertyNames();
            for (String key : propertyNames) {
                String property = properties.getProperty(key);
                sb.append(key).append("=").append(property).append('\n');
            }
            configService.publishConfig(dataId, group, sb.toString());
        } catch (Exception e) {
            //TODO ignore exception and print log
            LOGGER.warn("refresh nacos config fail!!!!!", e);
        }
    }
}
