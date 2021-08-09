package cn.icodening.console.cloud.router.common;

import cn.icodening.console.extension.Extensible;

/**
 * 表达式比较器，精确、正则...
 *
 * @author icodening
 * @date 2021.07.24
 */
@Extensible
public interface ExpressionMatcher {

    /**
     * 将传入表达式与值做比较
     *
     * @param expression 表达式，可以是精确值、正则表达式...
     * @param value      待比较的值
     * @return true表示匹配成功 false表示失败
     */
    boolean match(String expression, String value);
}
