package cn.icodening.console.cloud.router;

import java.util.regex.Pattern;

/**
 * @author icodening
 * @date 2021.07.24
 */
public class ExpressionRegexMatcher implements ExpressionMatcher {
    @Override
    public boolean match(String expression, String value) {
        return Pattern.matches(expression, value);
    }
}
