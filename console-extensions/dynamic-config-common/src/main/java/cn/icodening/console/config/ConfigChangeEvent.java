package cn.icodening.console.config;

import org.springframework.context.ApplicationEvent;

import java.util.Properties;

/**
 * @author icodening
 * @date 2021.07.10
 */
public class ConfigChangeEvent extends ApplicationEvent {

    public ConfigChangeEvent(Properties source) {
        super(source);
    }

    @Override
    public Properties getSource() {
        return (Properties) super.getSource();
    }
}
