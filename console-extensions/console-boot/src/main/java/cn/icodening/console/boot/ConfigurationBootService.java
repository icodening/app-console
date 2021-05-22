package cn.icodening.console.boot;

import cn.icodening.console.AppConsoleException;
import cn.icodening.console.Sortable;
import cn.icodening.console.config.ConfigurationManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

/**
 * @author icodening
 * @date 2021.05.22
 */
public class ConfigurationBootService extends BaseBootService {

    @Override
    public void start() {
        // load local config
        System.out.println("[" + ConfigurationBootService.class.getName() + "] load " + ConfigurationManager.DEFAULT_CONFIG_PATH);
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(ConfigurationManager.DEFAULT_CONFIG_PATH));
            Set<Object> keys = properties.keySet();
            for (Object key : keys) {
                if (key instanceof String) {
                    ConfigurationManager.INSTANCE.set((String) key, properties.getProperty((String) key));
                }
            }
        } catch (IOException e) {
            //FIXME LOG
            throw AppConsoleException.wrapperException(e);
        }
    }

    @Override
    public int getPriority() {
        return Sortable.MAX_PRIORITY;
    }
}
