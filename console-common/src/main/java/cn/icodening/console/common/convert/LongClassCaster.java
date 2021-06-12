package cn.icodening.console.common.convert;

/**
 * @author icodening
 * @date 2021.06.12
 */
public class LongClassCaster implements ClassCaster {

    @Override
    public Object convert(Object object) {
        if (object == null) {
            return null;
        }
        String str = String.valueOf(object);
        return Long.parseLong(str);
    }
}
