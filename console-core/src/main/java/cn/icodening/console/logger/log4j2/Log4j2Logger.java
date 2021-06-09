/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.icodening.console.logger.log4j2;


import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerHolder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.spi.AbstractLogger;

public class Log4j2Logger implements Logger {

    private final org.apache.logging.log4j.Logger logger;

    private static final String FQCN = LoggerHolder.class.getName();

    private AbstractLogger abstractLogger = null;

    public Log4j2Logger(org.apache.logging.log4j.Logger logger) {
        if (logger instanceof AbstractLogger) {
            this.abstractLogger = (AbstractLogger) logger;
        }
        this.logger = logger;
    }

    @Override
    public void trace(String msg) {
        logIfEnabled(FQCN, Level.TRACE, null, msg, null);
    }

    @Override
    public void trace(Throwable e) {
        logIfEnabled(FQCN, Level.TRACE, null, e == null ? null : e.getMessage(), e);
    }

    @Override
    public void trace(String msg, Throwable e) {
        logIfEnabled(FQCN, Level.TRACE, null, e == null ? null : msg, e);
    }

    @Override
    public void debug(String msg) {
        logIfEnabled(FQCN, Level.DEBUG, null, msg, null);
    }

    @Override
    public void debug(Throwable e) {
        logIfEnabled(FQCN, Level.DEBUG, null, e == null ? null : e.getMessage(), e);
    }

    @Override
    public void debug(String msg, Throwable e) {
        logIfEnabled(FQCN, Level.DEBUG, null, msg, e);
    }

    @Override
    public void info(String msg) {
        logIfEnabled(FQCN, Level.INFO, null, msg, null);
    }

    @Override
    public void info(Throwable e) {
        logIfEnabled(FQCN, Level.INFO, null, e == null ? null : e.getMessage(), e);
    }

    @Override
    public void info(String msg, Throwable e) {
        logIfEnabled(FQCN, Level.INFO, null, msg, e);
    }

    @Override
    public void warn(String msg) {
        logIfEnabled(FQCN, Level.WARN, null, msg, null);
    }

    @Override
    public void warn(Throwable e) {
        logIfEnabled(FQCN, Level.WARN, null, e == null ? null : e.getMessage(), e);
    }

    @Override
    public void warn(String msg, Throwable e) {
        logIfEnabled(FQCN, Level.WARN, null, msg, e);
    }

    @Override
    public void error(String msg) {
        logIfEnabled(FQCN, Level.ERROR, null, msg, null);
    }

    @Override
    public void error(Throwable e) {
        logIfEnabled(FQCN, Level.ERROR, null, e == null ? null : e.getMessage(), e);
    }

    @Override
    public void error(String msg, Throwable e) {
        logIfEnabled(FQCN, Level.ERROR, null, msg, e);
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    private void logIfEnabled(final String fqcn, final Level level, final Marker marker, final String msg,
                              final Throwable t) {
        if (abstractLogger != null) {
            abstractLogger.logIfEnabled(fqcn, level, null, msg, t);
        } else {
            logger.log(level, msg, t);
        }

    }

}
