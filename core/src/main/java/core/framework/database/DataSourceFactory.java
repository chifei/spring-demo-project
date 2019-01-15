package core.framework.database;

import com.mchange.v2.c3p0.AbstractConnectionCustomizer;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.management.NullManagementCoordinator;
import core.framework.crypto.EncryptionUtils;
import core.framework.util.AssertUtils;
import core.framework.util.ClasspathResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.transaction.annotation.Isolation;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;

/**
 * @author neo
 */
public final class DataSourceFactory implements FactoryBean<DataSource>, DisposableBean {
    static {
        // disable c3p0 jmx bean, avoid conflict with multiple webapps use c3p0 in same tomcat
        System.setProperty("com.mchange.v2.c3p0.management.ManagementCoordinator", NullManagementCoordinator.class.getName());
    }

    private final Logger logger = LoggerFactory.getLogger(DataSourceFactory.class);
    private final ComboPooledDataSource dataSource;

    public DataSourceFactory() {
        dataSource = new ComboPooledDataSource();
        dataSource.setTestConnectionOnCheckout(true);
    }

    public DataSourceFactory database(Database database) {
        switch (database) {
            case MYSQL:
                try {
                    dataSource.setDriverClass("com.mysql.jdbc.Driver");
                    dataSource.setPreferredTestQuery("select 1");
                } catch (PropertyVetoException e) {
                    throw new IllegalStateException(e); // dataSource.setDriverClass won't throw exception
                }
                break;
            default:
                throw new IllegalArgumentException("not supported database, please update framework, database=" + database);
        }
        return this;
    }

    public DataSourceFactory url(String url) {
        dataSource.setJdbcUrl(url);
        return this;
    }

    public DataSourceFactory user(String user) {
        dataSource.setUser(user);
        return this;
    }

    public DataSourceFactory password(String password) {
        dataSource.setPassword(password);
        return this;
    }

    public DataSourceFactory encryptedPassword(String encryptedPassword, ClasspathResource privateKey) {
        String password = EncryptionUtils.decrypt(encryptedPassword, privateKey);
        dataSource.setPassword(password);
        return this;
    }

    public DataSourceFactory poolSize(int minSize, int maxSize) {
        dataSource.setMinPoolSize(minSize);
        dataSource.setMaxPoolSize(maxSize);
        return this;
    }

    public DataSourceFactory defaultIsolationLevel(Isolation isolationLevel) {
        if (isolationLevel == Isolation.READ_COMMITTED)
            dataSource.setConnectionCustomizerClassName(ReadCommittedConnectionCustomizer.class.getName());
        else if (isolationLevel == Isolation.READ_UNCOMMITTED)
            dataSource.setConnectionCustomizerClassName(ReadUncommittedConnectionCustomizer.class.getName());
        else
            throw new IllegalArgumentException("not supported isolation level, please update framework, level=" + isolationLevel);

        return this;
    }

    @Override
    public void destroy() throws Exception {
        logger.info("close c3p0 connection pool, url={}", dataSource.getJdbcUrl());
        dataSource.close();
    }

    @Override
    public DataSource getObject() throws Exception {
        AssertUtils.assertHasText(dataSource.getJdbcUrl(), "url should be set");
        AssertUtils.assertHasText(dataSource.getUser(), "user should be set");
        AssertUtils.assertHasText(dataSource.getDriverClass(), "database should be set");
        return dataSource;
    }

    @Override
    public Class<?> getObjectType() {
        return DataSource.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public static class ReadUncommittedConnectionCustomizer extends AbstractConnectionCustomizer {
        @Override
        public void onAcquire(Connection connection, String parentDataSourceIdentityToken) throws Exception {
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        }
    }

    public static class ReadCommittedConnectionCustomizer extends AbstractConnectionCustomizer {
        @Override
        public void onAcquire(Connection connection, String parentDataSourceIdentityToken) throws Exception {
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        }
    }
}
