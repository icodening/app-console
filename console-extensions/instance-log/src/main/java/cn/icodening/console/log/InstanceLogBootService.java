package cn.icodening.console.log;

import cn.icodening.console.boot.BaseBootService;
import cn.icodening.console.config.ConfigurationManager;
import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerFactory;

import java.io.*;
import java.lang.instrument.Instrumentation;

/**
 * @author icodening
 * @date 2021.07.12
 */
public class InstanceLogBootService extends BaseBootService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstanceLogBootService.class);

    public static final String TEMP_LOG_PATH_KEY = "temp_log";

    @Override
    public void initialize(String agentArgs, Instrumentation instrumentation) {
        try {
            File temp = File.createTempFile("instance-console", ".log");
            temp.deleteOnExit();
            LOGGER.info("temp log path is:" + temp.getAbsolutePath());
            ConfigurationManager.INSTANCE.set(TEMP_LOG_PATH_KEY, temp.getAbsolutePath());
            PrintStream systemOut = System.out;
            BufferedOutputStream fbos = new BufferedOutputStream(new FileOutputStream(temp));
            SystemPrintStreamDecorator systemPrintStreamDecorator = new SystemPrintStreamDecorator(systemOut, fbos);
            SystemOutputStreamHolder.setSystemPrintStreamDecorator(systemPrintStreamDecorator);
            System.setOut(systemPrintStreamDecorator);
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }
}
