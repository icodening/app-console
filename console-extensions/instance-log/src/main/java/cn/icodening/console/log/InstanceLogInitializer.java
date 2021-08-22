package cn.icodening.console.log;

import cn.icodening.console.boot.BaseBootService;
import cn.icodening.console.config.ConfigurationManager;
import cn.icodening.console.logger.Logger;
import cn.icodening.console.logger.LoggerFactory;

import java.io.*;

/**
 * @author icodening
 * @date 2021.07.12
 */
public class InstanceLogInitializer extends BaseBootService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstanceLogInitializer.class);

    public static final String TEMP_LOG_PATH_KEY = "temp_log";

    @Override
    public void start() {
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
