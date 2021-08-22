package cn.icodening.console.config.nacos;

import cn.icodening.console.config.ConfigChangeEvent;
import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerFactory;
import com.alibaba.boot.nacos.config.properties.NacosConfigProperties;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.spring.util.NacosBeanUtils;
import com.alibaba.nacos.spring.util.NacosUtils;
import org.springframework.context.ApplicationListener;

import java.io.StringReader;
import java.util.*;

/**
 * @author icodening
 * @date 2021.08.03
 */
public class NacosSpringBootConfigListener implements ApplicationListener<ConfigChangeEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NacosSpringBootConfigListener.class);

    private final NacosConfigProperties nacosConfigProperties;

    public NacosSpringBootConfigListener(NacosConfigProperties nacosConfigProperties) {
        this.nacosConfigProperties = nacosConfigProperties;
    }

    @Override
    public void onApplicationEvent(ConfigChangeEvent event) {
        Properties source = event.getSource();
        Collection<ConfigService> configServices = NacosBeanUtils.getNacosServiceFactoryBean().getConfigServices();
        List<ConfigService> nacosConfigServices = new ArrayList<>(configServices);
        if (nacosConfigServices.isEmpty()) {
            return;
        }
        ConfigService configService = nacosConfigServices.get(0);
        String dataId = nacosConfigProperties.getDataId();
        String group = nacosConfigProperties.getGroup();
        try {
            String config = configService.getConfig(dataId, group, NacosUtils.DEFAULT_TIMEOUT);
            if (config == null) {
                LOGGER.warn("refresh nacos config fail! because nacos config is null");
                return;
            }
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
