package cn.icodening.console.logger.jdk;

import cn.icodening.console.logger.Level;
import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerAdapter;

import java.io.File;

/**
 * @author icodening
 * @date 2021.07.05
 */
public class JDKLoggerAdapter implements LoggerAdapter {
    @Override
    public Logger getLogger(Class<?> key) {
        return new JDKLogger(java.util.logging.Logger.getLogger(key.getName()));
    }

    @Override
    public Logger getLogger(String key) {
        return new JDKLogger(java.util.logging.Logger.getLogger(key));
    }

    @Override
    public Level getLevel() {
        return null;
    }

    @Override
    public void setLevel(Level level) {

    }

    @Override
    public File getFile() {
        return null;
    }

    @Override
    public void setFile(File file) {

    }
}
