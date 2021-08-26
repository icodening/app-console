package cn.icodening.console.logger.jdk;

import cn.icodening.console.logger.Logger;

import java.util.logging.Level;

/**
 * @author icodening
 * @date 2021.07.05
 */
public class JDKLogger implements Logger {

    private final java.util.logging.Logger logger;

    public JDKLogger(java.util.logging.Logger logger) {
        this.logger = logger;
    }

    @Override
    public void trace(String msg) {
        info(msg);
    }

    @Override
    public void trace(Throwable e) {
        info(e);
    }

    @Override
    public void trace(String msg, Throwable e) {
        info(msg, e);
    }

    @Override
    public void debug(String msg) {
        logger.log(Level.FINE, msg);
    }

    @Override
    public void debug(Throwable e) {
        debug(e.getMessage());
    }

    @Override
    public void debug(String msg, Throwable e) {
        logger.log(Level.FINE, e, e::getMessage);
    }

    @Override
    public void info(String msg) {
        logger.info(msg);
    }

    @Override
    public void info(Throwable e) {
        logger.log(Level.INFO, e, e::getMessage);
    }

    @Override
    public void info(String msg, Throwable e) {
        logger.log(Level.INFO, msg, e);
    }

    @Override
    public void warn(String msg) {
        logger.log(Level.WARNING, msg);
    }

    @Override
    public void warn(Throwable e) {
        logger.log(Level.WARNING, e, e::getMessage);
    }

    @Override
    public void warn(String msg, Throwable e) {
        logger.log(Level.WARNING, msg, e);
    }

    @Override
    public void error(String msg) {
        logger.log(Level.SEVERE, msg);
    }

    @Override
    public void error(Throwable e) {
        logger.log(Level.SEVERE, e, e::getMessage);
    }

    @Override
    public void error(String msg, Throwable e) {
        logger.log(Level.SEVERE, msg, e);
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isLoggable(Level.INFO);
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isLoggable(Level.INFO);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isLoggable(Level.INFO);
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isLoggable(Level.WARNING);
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isLoggable(Level.SEVERE);
    }
}
