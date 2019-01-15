package demo.customer;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;

/**
 * @author neo
 */
public class ConfigurationTest extends SpringTest {
    @Inject
    ApplicationContext applicationContext;

    @Test
    public void contextShouldBeInitialized() {
        Assert.assertNotNull(applicationContext);
    }

    @Test
    public void verifyBeanConfiguration() {
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            try {
                applicationContext.getBean(beanName);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create bean, name=" + beanName, e);
            }
        }
    }
}
