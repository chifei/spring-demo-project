package core.framework.log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;

/**
 * @author neo
 */
public class LogMessageFilterTest {
    LogMessageFilter logMessageFilter;

    @Before
    public void createLogMessageFilter() {
        logMessageFilter = new LogMessageFilter() {
            @Override
            public String filter(String loggerName, String message) {
                return message;
            }
        };
    }

    @Test
    public void maskWithSingleGroup() {
        String filteredMessage = logMessageFilter.mask("[param] password=passwordValue", "password=(.*)");
        Assert.assertEquals("[param] password=--masked--", filteredMessage);
    }

    @Test
    public void maskWithTwoGroups() {
        String filteredMessage = logMessageFilter.mask("[prefix] field1=field1Value, field2=field2Value", "field1=(.*), field2=(.*)");
        Assert.assertEquals("[prefix] field1=--masked--, field2=--masked--", filteredMessage);
    }

    @Test
    public void maskMultipleTimesWithSingleGroup() {
        String filteredMessage = logMessageFilter.mask("<request><password>p1</password><password>p2</password></request>", "<password>(.*?)</password>");
        Assert.assertEquals("<request><password>--masked--</password><password>--masked--</password></request>", filteredMessage);
    }

    @Test
    public void maskWithoutGroup() {
        String filteredMessage = logMessageFilter.mask("[param] password=passwordValue", "password=.*");
        Assert.assertThat(filteredMessage, containsString("entire message masked"));
    }

    @Test
    public void maskWithInvalidPattern() {
        String filteredMessage = logMessageFilter.mask("[param] field=fieldValue", "invalidPattern[");
        Assert.assertThat(filteredMessage, containsString("entire message masked"));
    }
}
