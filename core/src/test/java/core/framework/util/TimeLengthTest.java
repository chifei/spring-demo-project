package core.framework.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author neo
 */
public class TimeLengthTest {
    @Test
    public void toMilliseconds() {
        assertEquals(1000, TimeLength.seconds(1).toMilliseconds());

        assertEquals(60000, TimeLength.minutes(1).toMilliseconds());

        assertEquals(0, TimeLength.ZERO.toMilliseconds());
    }

    @Test
    public void oneMinuteEqualsSixtySeconds() {
        assertEquals(TimeLength.minutes(1), TimeLength.seconds(60));
    }

    @Test
    public void oneHourEqualsSixtyMinutes() {
        assertEquals(TimeLength.hours(1), TimeLength.minutes(60));
    }

    @Test
    public void toSeconds() {
        assertEquals(60, TimeLength.minutes(1).toSeconds());
    }
}
