package cn.icodening.console.common.convert;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * @author icodening
 * @date 2021.06.12
 */
public class DateClassCaster implements ClassCaster {

    @Override
    public Object convert(Object object) {
        if (object == null) {
            return null;
        }
        Class<?> objClazz = object.getClass();
        Object ret = object;
        if (Long.class.isAssignableFrom(objClazz)) {
            ret = new Date((Long) object);
        }
        if (String.class.isAssignableFrom(objClazz)) {
            try {
                DateFormat dateFormat = DateFormat.getDateInstance();
                ret = dateFormat.parse(String.valueOf(object));
            } catch (ParseException exception) {
                ret = Long.parseLong((String) object);
            }
        }
        return ret;
    }
}
