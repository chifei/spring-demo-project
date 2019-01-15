package core.framework.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author neo
 */
public class ClasspathResourceTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldThrowExceptionIfResourceNotFound() {
        exception.expect(IllegalArgumentException.class);
        new ClasspathResource("not-existed-resource");
    }
}
