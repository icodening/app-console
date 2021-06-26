package cn.icodening.console.register.spring;

import cn.icodening.console.event.AppConsoleEvent;
import org.springframework.context.ApplicationContext;

/**
 * 用户程序 的Spring上下文初始化完成后事件
 *
 * @author icodening
 * @date 2021.06.26
 */
public class SpringContextRefreshedEvent extends AppConsoleEvent {

    public SpringContextRefreshedEvent(ApplicationContext source) {
        super(source);
    }

    @Override
    public ApplicationContext getSource() {
        return (ApplicationContext) super.getSource();
    }
}
