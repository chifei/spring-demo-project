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
public class JDBCTransactionTest extends SpringTest {
    @Inject
    JDBCTransactionTestBean bean;
    @Inject
    JDBCAccess jdbcAccess;

    @Before
    public void createTestTable() {
        jdbcAccess.execute("create table db_transaction_test (value varchar(20))");
        jdbcAccess.execute("insert into db_transaction_test (value) values (?)", "value1");
    }

    @After
    public void dropTestTable() {
        jdbcAccess.execute("drop table db_transaction_test");
    }

    @Test
    public void commit() {
        bean.updateValue("value2");
        String name = bean.readValue();
        Assert.assertEquals("value2", name);
    }

    @Test
    public void rollback() {
        try {
            bean.updateValueWithException("value2");
            Assert.fail();
        } catch (Exception e) {
            String name = bean.readValue();
            Assert.assertEquals("value1", name);
        }
    }
}