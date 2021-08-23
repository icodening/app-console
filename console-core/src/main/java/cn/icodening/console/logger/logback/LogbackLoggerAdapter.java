package cn.icodening.console.logger.logback;

import ch.qos.logback.classic.LoggerContext;
import cn.icodening.console.logger.Level;
import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerAdapter;

import java.io.File;

/**
 * @author icodening
 * @date 2021.08.24
 */
public class LogbackLoggerAdapter implements LoggerAdapter {

    private final LoggerContext loggerContext = new LoggerContext();

    @Override
    public Logger getLogger(Class<?> key) {
        return new LogbackLogger(loggerContext.getLogger(key));
    }

    @Override
    public Logger getLogger(String key) {
        return new LogbackLogger(loggerContext.getLogger(key));
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
