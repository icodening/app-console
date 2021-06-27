package cn.icodening.console.logger;

import java.io.File;

/**
 * 此处参考自Dubbo Logger
 *
 * @author icodening
 * @date 2021.06.09
 */
public interface LoggerAdapter {

    /**
     * Get a logger
     *
     * @param key the returned logger will be named after clazz
     * @return logger
     */
    Logger getLogger(Class<?> key);

    /**
     * Get a logger
     *
     * @param key the returned logger will be named after key
     * @return logger
     */
    Logger getLogger(String key);

    /**
     * Get the current logging level
     *
     * @return current logging level
     */
    Level getLevel();

    /**
     * Set the current logging level
     *
     * @param level logging level
     */
    void setLevel(Level level);

    /**
     * Get the current logging file
     *
     * @return current logging file
     */
    File getFile();

    /**
     * Set the current logging file
     *
     * @param file logging file
     */
    void setFile(File file);
}
