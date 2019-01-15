package core.test.rest.database;

import core.framework.database.JDBCAccess;
import core.test.rest.SpringTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

/**
 * @author neo
 */
public class JDBCAccessTest extends SpringTest {
    @Inject
    JDBCAccess jdbcAccess;

    @Before
    public void createTestTable() {
        jdbcAccess.execute("create table db_sql_test (id int, value varchar(20))");
        jdbcAccess.execute("insert into db_sql_test (id, value) values (?, ?)", 1, "value");
    }

    @After
    public void dropTestTable() {
        jdbcAccess.execute("drop table db_sql_test");
    }

    @Test
    public void findStringBySQL() {
        String value = jdbcAccess.findString("select value from db_sql_test");

        Assert.assertEquals("value", value);
    }

    @Test
    public void findIntegerBySQL() {
        Integer count = jdbcAccess.findInteger("select count(*) from db_sql_test");

        Assert.assertEquals(1, (int) count);
    }

    @Test
    public void findStringBySQLReturnsNull() {
        String value = jdbcAccess.findString("select value from db_sql_test where id > 1");

        Assert.assertNull(value);
    }
}
