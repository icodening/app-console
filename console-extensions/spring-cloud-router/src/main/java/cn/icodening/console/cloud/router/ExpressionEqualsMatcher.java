package cn.icodening.console.cloud.router;

/**
 * @author icodening
 * @date 2021.07.24
 */
public class ExpressionEqualsMatcher implements ExpressionMatcher {

    @Override
    public boolean match(String expression, String value) {
        if (expression == null && value == null) {
            return true;
        }
        if (expression != null) {
            return expression.equals(value);
        }
        return false;
    }
}
