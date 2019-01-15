package core.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author neo
 */
public final class Convert {
    private Convert() {
    }

    public static Integer toInt(String text, Integer defaultValue) {
        if (!StringUtils.hasText(text))
            return defaultValue;
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Long toLong(String text, Long defaultValue) {
        if (!StringUtils.hasText(text))
            return defaultValue;
        try {
            return Long.parseLong(text);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Double toDouble(String text, Double defaultValue) {
        if (!StringUtils.hasText(text))
            return defaultValue;
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Date toDate(String date, String formatPattern, Date defaultValue) {
        if (!StringUtils.hasText(date)) {
            return defaultValue;
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatPattern);
            return format.parse(date);
        } catch (ParseException e) {
            return defaultValue;
        }
    }

    public static Date toDate(String date, String formatPattern) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatPattern);
            return format.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String toString(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static String toString(Date date, String format, TimeZone timeZone) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(timeZone);
        return dateFormat.format(date);
    }

    public static <T extends Enum<T>> T toEnum(String value, Class<T> enumClass, T defaultValue) {
        if (!StringUtils.hasText(value))
            return defaultValue;
        try {
            return Enum.valueOf(enumClass, value);
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }
}
