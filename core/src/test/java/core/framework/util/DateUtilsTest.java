package core.framework.util;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static core.framework.util.DateUtils.date;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

/**
 * @author neo
 */
public class DateUtilsTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void newDateShouldBeExact() {
        Date date = date(2008, Calendar.JANUARY, 2, 3, 4, 5);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        assertEquals(2008, calendar.get(Calendar.YEAR));
        assertEquals(Calendar.JANUARY, calendar.get(Calendar.MONTH));
        assertEquals(2, calendar.get(Calendar.DATE));
        assertEquals(3, calendar.get(Calendar.HOUR));
        assertEquals(4, calendar.get(Calendar.MINUTE));
        assertEquals(5, calendar.get(Calendar.SECOND));
        assertEquals(0, calendar.get(Calendar.MILLISECOND));
    }

    @Test
    public void createDateWithoutTime() {
        Date date = DateUtils.date(2009, Calendar.JANUARY, 15);

        Calendar calendar = DateUtils.calendar(date);

        Assert.assertEquals(0, calendar.get(Calendar.HOUR));
        Assert.assertEquals(0, calendar.get(Calendar.MINUTE));
        Assert.assertEquals(0, calendar.get(Calendar.SECOND));
        Assert.assertEquals(0, calendar.get(Calendar.MILLISECOND));
    }

    @Test
    public void withHour() {
        Date date = date(2008, Calendar.JANUARY, 2, 3, 4, 5);

        Date newDate = DateUtils.withHour(date, 23);

        Calendar calendar = DateUtils.calendar(newDate);
        assertEquals(23, calendar.get(Calendar.HOUR_OF_DAY));
    }

    @Test
    public void withHourReturnsNewDateInstance() {
        Date date = date(2008, Calendar.JANUARY, 2, 3, 4, 5);
        Date newDate = DateUtils.withHour(date, 23);

        assertNotSame(newDate, date);
    }

    @Test
    public void getHour() {
        Date date = date(2008, Calendar.JANUARY, 2, 3, 4, 5);

        int hour = DateUtils.getHour(date);
        assertEquals(3, hour);
    }

    @Test
    public void convertDateToCurrentTimeZone() {
        Date date = date(2008, Calendar.AUGUST, 2, 0, 0, 0);

        TimeZone timeZone = Calendar.getInstance().getTimeZone();
        Date result = DateUtils.toCurrentTimeZone(date, timeZone);
        assertEquals("should not change if it was from current timezone", date.getTime(), result.getTime());

        Date result1 = DateUtils.toCurrentTimeZone(date, TimeZone.getTimeZone("GMT-8:00"));
        Date result2 = DateUtils.toCurrentTimeZone(date, TimeZone.getTimeZone("GMT-7:00"));
        assertEquals(60, DateRangeUtils.minutesBetween(result2, result1), 0);
    }

    @Test
    public void isWeekDay() {
        Date oneFriday = date(2008, Calendar.SEPTEMBER, 5, 0, 0, 0);
        Date oneSaturday = date(2008, Calendar.SEPTEMBER, 6, 0, 0, 0);
        Date oneSunday = date(2008, Calendar.SEPTEMBER, 7, 0, 0, 0);
        Date oneMonday = date(2008, Calendar.SEPTEMBER, 8, 0, 0, 0);

        assertTrue(DateUtils.isWeekDay(oneFriday));
        assertFalse(DateUtils.isWeekDay(oneSaturday));
        assertFalse(DateUtils.isWeekDay(oneSunday));
        assertTrue(DateUtils.isWeekDay(oneMonday));
    }

    @Test
    public void addReturnsNewDateInstance() {
        Date date = date(2008, Calendar.JANUARY, 2, 3, 4, 5);

        Date newDate = DateUtils.add(date, Calendar.HOUR, 1);
        assertNotSame(newDate, date);
    }

    @Test
    public void dateNotAllowLenient() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("DAY_OF_MONTH");

        date(2010, Calendar.JANUARY, 60);
    }

    @Test
    public void getYearMonthDay() {
        Date date = date(2008, Calendar.JANUARY, 2);

        assertEquals(2008, DateUtils.getYear(date));
        assertEquals(Calendar.JANUARY, DateUtils.getMonth(date));
        assertEquals(2, DateUtils.getDay(date));
    }

    @Test
    public void isDateValid() {
        assertTrue(DateUtils.isDateValid(2000, Calendar.FEBRUARY, 28));
        assertFalse(DateUtils.isDateValid(2000, Calendar.FEBRUARY, 31));
    }

    @Test
    public void truncateTime() {
        Date dateTime = DateUtils.date(2012, Calendar.MAY, 25, 13, 40, 0);

        Date date = DateUtils.truncateTime(dateTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Assert.assertEquals(0, calendar.get(Calendar.HOUR));
        Assert.assertEquals(0, calendar.get(Calendar.MINUTE));
        Assert.assertEquals(0, calendar.get(Calendar.SECOND));
        Assert.assertEquals(0, calendar.get(Calendar.MILLISECOND));
    }
}

