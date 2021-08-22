package cn.icodening.console.boot;

import cn.icodening.console.AgentInitializer;
import cn.icodening.console.AppConsoleException;
import cn.icodening.console.Sortable;
import cn.icodening.console.config.ConfigurationManager;
import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.Properties;
import java.util.Set;

/**
 * @author icodening
 * @date 2021.05.22
 */
public class ConfigurationAgentInitializer implements AgentInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationAgentInitializer.class);

    @Override
    public void initialize(String agentArgs, Instrumentation instrumentation) {
        // load local config
        LOGGER.info("load " + ConfigurationManager.DEFAULT_CONFIG_PATH);
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(ConfigurationManager.DEFAULT_CONFIG_PATH));
            Set<Object> keys = properties.keySet();
            for (Object key : keys) {
                if (key instanceof String) {
                    ConfigurationManager.INSTANCE.set((String) key, properties.getProperty((String) key));
                }
            }
            LOGGER.info("load success:  " + properties);
        } catch (IOException e) {
            throw AppConsoleException.wrapperException(e);
        }
    }

    @Override
    public int getPriority() {
        return Sortable.MAX_PRIORITY;
    }
}
