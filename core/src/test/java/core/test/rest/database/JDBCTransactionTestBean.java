package core.test.rest.database;

import core.framework.database.JDBCAccess;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * @author neo
 */
@Service
public class JDBCTransactionTestBean {
    @Inject
    JDBCAccess jdbcAccess;

    public String readValue() {
        return jdbcAccess.findString("select value from db_transaction_test");
    }

    @Transactional
    public void updateValue(String value) {
        jdbcAccess.execute("update db_transaction_test set value = ?", value);
    }

    @Transactional
    public void updateValueWithException(String value) {
        jdbcAccess.execute("insert into db_transaction_test (value) values (?)", value);
        throw new IllegalStateException("test exception");
    }
}
