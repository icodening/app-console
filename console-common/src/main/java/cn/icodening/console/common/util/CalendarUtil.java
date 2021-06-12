package cn.icodening.console.common.util;

import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author icodening
 * @date 2021.06.12
 */
public class CalendarUtil {

    private static final Map<String, Integer> calendarFieldMap = new ConcurrentHashMap<>();

    static {
        calendarFieldMap.putIfAbsent("YEAR", Calendar.YEAR);
        calendarFieldMap.putIfAbsent("MONTH", Calendar.MONTH);
        calendarFieldMap.putIfAbsent("DATE", Calendar.DATE);
        calendarFieldMap.putIfAbsent("DAY", Calendar.DATE);
        calendarFieldMap.putIfAbsent("HOUR", Calendar.HOUR);
        calendarFieldMap.putIfAbsent("MINUTE", Calendar.MINUTE);
        calendarFieldMap.putIfAbsent("SECOND", Calendar.SECOND);
    }

    private CalendarUtil() {
    }

    public static long addTime(long time, int field, int num) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.add(field, num);
        return cal.getTimeInMillis();
    }

    public static long addTime(int field, int num) {
        return addTime(System.currentTimeMillis(), field, num);
    }

    public static Integer getField(String string) {
        return calendarFieldMap.get(string);
    }
}
