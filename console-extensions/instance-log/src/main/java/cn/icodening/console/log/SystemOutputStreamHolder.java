package cn.icodening.console.log;

/**
 * @author icodening
 * @date 2021.07.12
 */
public class SystemOutputStreamHolder {

    private static volatile SystemPrintStreamDecorator systemPrintStreamDecorator;

    private SystemOutputStreamHolder() {
    }

    public static SystemPrintStreamDecorator getSystemPrintStreamDecorator() {
        return systemPrintStreamDecorator;
    }

    public static void setSystemPrintStreamDecorator(SystemPrintStreamDecorator systemPrintStreamDecorator) {
        SystemOutputStreamHolder.systemPrintStreamDecorator = systemPrintStreamDecorator;
    }
}
