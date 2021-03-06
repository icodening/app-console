package cn.icodening.console.logger;

import cn.icodening.console.logger.jdk.JDKLoggerAdapter;
import cn.icodening.console.logger.log4j2.Log4j2LoggerAdapter;
import cn.icodening.console.logger.logback.LogbackLoggerAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * FIXME 应用运行时非AppClassLoader类加载器导致仅JDK日志生效，是否需要移除
 *
 * @author icodening
 * @date 2021.06.09
 */
public class LoggerFactory {

    private static final ConcurrentMap<String, LoggerHolder> LOGGERS = new ConcurrentHashMap<>();

    private static volatile LoggerAdapter LOGGER_ADAPTER;

    static {
        String logger = System.getProperty("application.console.logger", "");
        switch (logger) {
            case "log4j2": {
                setLoggerAdapter(new Log4j2LoggerAdapter());
                break;
            }
            case "jdk": {
                setLoggerAdapter(new JDKLoggerAdapter());
                break;
            }
            case "logback": {
                setLoggerAdapter(new LogbackLoggerAdapter());
                break;
            }
            default:
                List<Class<? extends LoggerAdapter>> candidates = Arrays.asList(
                        LogbackLoggerAdapter.class, Log4j2LoggerAdapter.class, JDKLoggerAdapter.class
                );
                for (Class<? extends LoggerAdapter> clazz : candidates) {
                    try {
                        setLoggerAdapter(clazz.newInstance());
                        break;
                    } catch (Throwable ignored) {
                    }
                }
        }
    }

    private LoggerFactory() {
    }

    /**
     * Set logger provider
     *
     * @param loggerAdapter logger provider
     */
    public static void setLoggerAdapter(LoggerAdapter loggerAdapter) {
        if (loggerAdapter != null) {
            Logger logger = loggerAdapter.getLogger(LoggerFactory.class.getName());
            logger = new LoggerHolder(logger);
            logger.info("using logger: " + loggerAdapter.getClass().getName());
            LoggerFactory.LOGGER_ADAPTER = loggerAdapter;
            for (Map.Entry<String, LoggerHolder> entry : LOGGERS.entrySet()) {
                entry.getValue().setLogger(LOGGER_ADAPTER.getLogger(entry.getKey()));
            }
        }
    }

    public static Logger getLogger(Class<?> key) {
        return LOGGERS.computeIfAbsent(key.getName(),
                name -> new LoggerHolder(LOGGER_ADAPTER.getLogger(name)));
    }

    /**
     * Get logger provider
     *
     * @param key the returned logger will be named after key
     * @return logger provider
     */
    public static Logger getLogger(String key) {
        return LOGGERS.computeIfAbsent(key, k -> new LoggerHolder(LOGGER_ADAPTER.getLogger(k)));
    }

}
