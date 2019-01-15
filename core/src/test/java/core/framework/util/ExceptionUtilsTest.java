package core.framework.util;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;

/**
 * @author neo
 */
public class ExceptionUtilsTest {
    @Test
    public void getStackTrace() {
        RuntimeException exception = new RuntimeException();
        String trace = ExceptionUtils.stackTrace(exception);

        Assert.assertThat(trace, containsString(RuntimeException.class.getName()));
    }
}
