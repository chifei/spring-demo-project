package core.framework.util;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author neo
 */
public class ConvertTest {
    @Test
    public void toIntShouldReturnDefaultValueWithInvalidFormat() {
        assertEquals(-1, (int) Convert.toInt("", -1));
        assertEquals(-1, (int) Convert.toInt("   ", -1));
        assertEquals(-1, (int) Convert.toInt(null, -1));
        assertEquals(-1, (int) Convert.toInt("a", -1));
    }

    @Test
    public void toInt() {
        assertEquals(10, (int) Convert.toInt("10", -1));
    }

    @Test
    public void toDate() {
        Date date = Convert.toDate("01-02-1998", "MM-dd-yyyy", null);

        assertNotNull(date);

        Calendar calendar = DateUtils.calendar(date);

        assertEquals(2, calendar.get(Calendar.DATE));
        assertEquals(Calendar.JANUARY, calendar.get(Calendar.MONTH));
        assertEquals(1998, calendar.get(Calendar.YEAR));
    }

    @Test
    public void convertDateToString() {
        Date date = DateUtils.date(2010, Calendar.JULY, 19);
        assertEquals("2010-07-19", Convert.toString(date, "yyyy-MM-dd"));
        assertEquals("20100719", Convert.toString(date, "yyyyMMdd"));
    }
}
