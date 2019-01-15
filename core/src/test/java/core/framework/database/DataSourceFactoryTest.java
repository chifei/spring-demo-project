package core.framework.database;

import core.framework.util.AssertUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author neo
 */
public class DataSourceFactoryTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    DataSourceFactory dataSourceFactory;

    @Before
    public void createDataSourceFactory() {
        dataSourceFactory = new DataSourceFactory();
    }

    @Test
    public void checkURL() throws Exception {
        exception.expect(AssertUtils.AssertionException.class);
        exception.expectMessage("url");

        dataSourceFactory.getObject();
    }

    @Test
    public void checkUser() throws Exception {
        exception.expect(AssertUtils.AssertionException.class);
        exception.expectMessage("user");

        dataSourceFactory.url("jdbc://");
        dataSourceFactory.getObject();
    }
}