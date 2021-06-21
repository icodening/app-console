package cn.icodening.console.common;

import java.util.Date;

/**
 * @author icodening
 * @date 2021.05.24
 */
public interface Modifiable {

    /**
     * 设置修改时间
     *
     * @param date 需要修改的时间
     */
    void setModifyTime(Date date);

    /**
     * 设置当前时间为默认修改时间
     */
    default void setModifyTime() {
        setModifyTime(new Date());
    }
}
