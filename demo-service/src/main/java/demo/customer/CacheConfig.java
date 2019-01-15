package demo.customer;

import core.framework.cache.CacheRegistry;
import core.framework.cache.DefaultCacheConfig;
import core.framework.util.TimeLength;
import demo.customer.domain.Customer;
import org.springframework.context.annotation.Configuration;

/**
 * @author neo
 */
@Configuration
public class CacheConfig extends DefaultCacheConfig {
    @Override
    protected void addCaches(CacheRegistry registry) {
        registry.add("customers", Customer.class, TimeLength.minutes(5));
    }
}
