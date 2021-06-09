package cn.icodening.console.common;

import java.util.Date;

/**
 * @author icodening
 * @date 2021.05.24
 */
public interface Modifiable {

    void setModifyTime(Date date);

    default void setModifyTime() {
        setModifyTime(new Date());
    }
}
