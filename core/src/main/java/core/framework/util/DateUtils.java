package core.framework.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Date should be considered as immutable object (e.g. String), all methods in
 * this util return new instance
 *
 * @author neo
 */
public final class DateUtils {
    private DateUtils() {
    }

    public static Date date(int year, int month, int day) {
        return date(year, month, day, 0, 0, 0);
    }

    @SuppressWarnings("checkstyle:parameternumber")
    public static Date date(int year, int month, int day, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.set(year, month, day, hour, minute, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Calendar calendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Date add(Date date, int field, int value) {
        Calendar calendar = calendar(date);
        calendar.add(field, value);
        return calendar.getTime();
    }

    public static int get(Date date, int field) {
        Calendar calendar = calendar(date);
        return calendar.get(field);
    }

    public static Date withField(Date date, int field, int value) {
        Calendar calendar = calendar(date);
        calendar.set(field, value);
        return calendar.getTime();
    }

    public static int getYear(Date date) {
        return get(date, Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        return get(date, Calendar.MONTH);
    }

    public static int getDay(Date date) {
        return get(date, Calendar.DATE);
    }

    public static int getHour(Date date) {
        return get(date, Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(Date date) {
        return get(date, Calendar.MINUTE);
    }

    public static Date withHour(Date date, int value) {
        return withField(date, Calendar.HOUR_OF_DAY, value);
    }

    public static Date withMinute(Date date, int value) {
        return withField(date, Calendar.MINUTE, value);
    }

    public static Date toCurrentTimeZone(Date targetDate, TimeZone targetTimeZone) {
        Calendar target = calendar(targetDate);

        Calendar result = Calendar.getInstance(targetTimeZone);
        result.set(Calendar.YEAR, target.get(Calendar.YEAR));
        result.set(Calendar.MONTH, target.get(Calendar.MONTH));
        result.set(Calendar.DATE, target.get(Calendar.DATE));
        result.set(Calendar.HOUR_OF_DAY, target.get(Calendar.HOUR_OF_DAY));
        result.set(Calendar.MINUTE, target.get(Calendar.MINUTE));
        result.set(Calendar.SECOND, target.get(Calendar.SECOND));
        result.set(Calendar.MILLISECOND, target.get(Calendar.MILLISECOND));

        return result.getTime();
    }

    public static boolean isWeekDay(Date targetDate) {
        Calendar calendar = calendar(targetDate);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY;
    }

    public static boolean isDateValid(int year, int month, int day) {
        try {
            date(year, month, day);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Date truncateTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
