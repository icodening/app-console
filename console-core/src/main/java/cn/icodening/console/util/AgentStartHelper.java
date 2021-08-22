package cn.icodening.console.util;

/**
 * @author icodening
 * @date 2021.08.22
 */
public class AgentStartHelper {

    private static volatile Runnable runnable = () -> {
    };

    public static void setStart(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        AgentStartHelper.runnable = runnable;
    }

    public static void start() {
        AgentStartHelper.runnable.run();
    }
}
