package demo.product;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import core.framework.database.JDBCAccess;
import core.framework.database.JPAAccess;
import core.framework.event.EventPublisher;
import core.framework.event.SQSEventPublisher;
import core.framework.task.TaskExecutor;
import demo.product.event.ProductCreatedEvent;
import demo.product.event.ProductUpdatedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.inject.Inject;
import javax.sql.DataSource;

/**
 * @author neo
 */
@Configuration
public class AppConfig {
    @Inject
    Environment env;

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL).build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan(AppConfig.class.getPackage().getName());
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.HSQL);
        vendorAdapter.setShowSql(true);
        vendorAdapter.setGenerateDdl(true);
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

    @Bean
    public JPAAccess jpaAccess() {
        return new JPAAccess();
    }

    @Bean
    public JDBCAccess jdbcAccess() {
        JDBCAccess jdbcAccess = new JDBCAccess();
        jdbcAccess.setDataSource(dataSource());
        return jdbcAccess;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return TaskExecutor.unlimitedExecutor();
    }

    @Bean
    public AmazonSNS amazonSNS() {
        return new AmazonSNSClient(new ClasspathPropertiesFileCredentialsProvider("aws.properties"));
    }

    @Bean
    public AmazonSQS amazonSQS() {
        return new AmazonSQSClient(new ClasspathPropertiesFileCredentialsProvider("aws.properties"));
    }

    @Bean
    public EventPublisher eventPublisher() {
        String queueURL = env.getRequiredProperty("event.queueURL");

        return new SQSEventPublisher(amazonSQS())
                .eventSender("demo-scheduler")
                .registerEventQueue(ProductCreatedEvent.class, queueURL)
                .registerEventQueue(ProductUpdatedEvent.class, queueURL);
    }

//    @Bean
//    public EventPublisher eventPublisher() {
//        String topicARN = env.getRequiredProperty("event.topicARN");
//
//        return new SNSEventPublisher(amazonSNS())
//                .eventSender("demo-scheduler")
//                .registerEventTopic(ProductCreatedEvent.class, topicARN)
//                .registerEventTopic(ProductUpdatedEvent.class, topicARN);
//    }
}
