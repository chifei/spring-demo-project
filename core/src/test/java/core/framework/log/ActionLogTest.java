package core.framework.log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ActionLogTest {
    ActionLog actionLog;

    @Before
    public void createActionLog() {
        actionLog = new ActionLog();
    }

    @Test
    public void filterLineSeparator() {
        String value = actionLog.filterLineSeparator("first_line\nsecond_line");
        Assert.assertEquals("first_line second_line", value);
    }

    @Test
    public void timestamp() {
        String timestamp = actionLog.timestamp();
        Assert.assertTrue(timestamp.matches("\\d{4}\\-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}[+-]\\d{4}"));
    }
}