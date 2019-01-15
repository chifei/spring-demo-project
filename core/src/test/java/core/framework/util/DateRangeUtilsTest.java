package core.framework.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static core.framework.util.DateUtils.date;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author neo
 */
public class DateRangeUtilsTest {
    @Test
    public void minutesBetween() {
        Date before = date(2008, Calendar.JANUARY, 1, 1, 0, 0);
        Date after = date(2008, Calendar.JANUARY, 1, 1, 30, 0);

        assertEquals(30, DateRangeUtils.minutesBetween(before, after), 0);
        assertEquals("between method handles the 2 dates in any order", 30, DateRangeUtils.minutesBetween(after, before), 0);
    }

    @Test
    public void hoursBetween() {
        Date before = date(2008, Calendar.JANUARY, 1, 1, 0, 0);
        Date after = date(2008, Calendar.JANUARY, 1, 2, 0, 0);

        assertEquals(1, DateRangeUtils.hoursBetween(before, after), 0);

        before = date(2008, Calendar.JANUARY, 1, 1, 0, 0);
        after = date(2008, Calendar.JANUARY, 1, 2, 5, 0);

        double hoursBetween = DateRangeUtils.hoursBetween(before, after);
        assertTrue("the gap should be more than 1 hour", hoursBetween > 1);
        assertTrue("the gap should be more than 1 hour", hoursBetween < 2);
    }

    @Test
    public void daysBetween() {
        Date before = date(2008, Calendar.JANUARY, 1, 1, 0, 0);
        Date after = date(2008, Calendar.JANUARY, 2, 1, 0, 0);

        assertEquals(1, DateRangeUtils.daysBetween(before, after), 0);

        before = date(2008, Calendar.JANUARY, 1, 1, 0, 0);
        after = date(2008, Calendar.JANUARY, 2, 2, 0, 0);

        double daysBetween = DateRangeUtils.daysBetween(before, after);
        assertTrue("the gap should be more than 1 day", daysBetween > 1);
        assertTrue("the gap should be more than 1 day", daysBetween < 2);
    }

    @Test
    public void monthsBetweenWithExactDay() {
        Date before = DateUtils.date(2008, Calendar.JANUARY, 15);
        Date after = DateUtils.date(2009, Calendar.JANUARY, 15);

        double monthsBetween = DateRangeUtils.monthsBetween(before, after);

        Assert.assertEquals(12, monthsBetween, 0);
    }

    @Test
    public void monthsBetweenWithDaysDiffLessThanOneMonth() {
        Date before = DateUtils.date(2008, Calendar.JANUARY, 15);
        Date after = DateUtils.date(2009, Calendar.JANUARY, 14);

        double monthsBetween = DateRangeUtils.monthsBetween(before, after);

        Assert.assertEquals(11, Math.floor(monthsBetween), 0);
        Assert.assertEquals(12, Math.ceil(monthsBetween), 0);
    }

    @Test
    public void monthsBetweenWithDaysDiffMoreThanOneMonth() {
        Date before = DateUtils.date(2009, Calendar.JANUARY, 15);
        Date after = DateUtils.date(2009, Calendar.FEBRUARY, 16);

        double monthsBetween = DateRangeUtils.monthsBetween(before, after);

        Assert.assertEquals(1, Math.floor(monthsBetween), 0);
        Assert.assertEquals(2, Math.ceil(monthsBetween), 0);
    }

    @Test
    public void yearsBetweenWithOneDayLessFromAnotherYear() {
        Date before = DateUtils.date(1878, 11, 2);
        Date after = DateUtils.date(2009, 11, 1);
        int yearsBetween = (int) DateRangeUtils.yearsBetween(before, after);
        assertEquals(130, yearsBetween);
    }

    @Test
    public void yearsBetweenHandlesDatesInAnyOrder() {
        Date before = DateUtils.date(2008, 11, 1);
        Date after = DateUtils.date(2009, 11, 1);

        Assert.assertEquals(1, DateRangeUtils.yearsBetween(before, after), 0);
        Assert.assertEquals(1, DateRangeUtils.yearsBetween(after, before), 0);
    }
}
