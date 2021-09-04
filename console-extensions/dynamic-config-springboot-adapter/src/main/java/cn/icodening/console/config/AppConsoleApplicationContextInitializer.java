package cn.icodening.console.config;

import cn.icodening.console.common.entity.ConfigEntity;
import cn.icodening.console.http.Request;
import cn.icodening.console.http.Response;
import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerFactory;
import cn.icodening.console.util.HttpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author icodening
 * @date 2021.07.11
 */
public class AppConsoleApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppConsoleApplicationContextInitializer.class);

    private static final String APPLICATION_NAME_KEY = "spring.application.name";

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        String serverAddress = ConfigurationManager.INSTANCE.get("serverAddress");
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        String applicationName = applicationContext.getEnvironment().getProperty(APPLICATION_NAME_KEY);
        Properties properties = new Properties();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Request get = Request.of(serverAddress + "/config", "GET");
            get.getQueryParams().put("scope", "APPLICATION");
            get.getQueryParams().put("affectTarget", applicationName);
            get.getQueryParams().put("enable", Boolean.TRUE.toString());
            Response applicationConfig = HttpUtil.exchange(get);
            JsonNode jsonNode = objectMapper.readTree(applicationConfig.getData());
            JsonNode data = jsonNode.get("data");
            JsonNode list = data.get("list");
            for (int i = 0; list.has(i); i++) {
                JsonNode objectNode = list.get(i);
                ConfigEntity target = objectMapper.readValue(objectNode.toString(), ConfigEntity.class);
                properties.load(new StringReader(target.getContent()));
            }
        } catch (Throwable e) {
            LOGGER.warn("initialize dynamic config fail !!! because:" + e.getMessage());
        }

        Set<String> set = properties.stringPropertyNames();
        Map<String, Object> propertyMap = new HashMap<>(set.size());
        for (String key : set) {
            propertyMap.put(key, properties.get(key));
        }
        CompositePropertySource compositePropertySource = new CompositePropertySource("AppConsolePropertySource");
        MapPropertySource mapPropertySource = new MapPropertySource("AppConsoleMapSource", propertyMap);
        compositePropertySource.addPropertySource(mapPropertySource);
        propertySources.addFirst(compositePropertySource);
    }
}
