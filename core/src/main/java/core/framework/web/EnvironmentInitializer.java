package core.framework.web;

import core.framework.util.RuntimeIOException;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.Properties;

/**
 * @author neo
 */
public class EnvironmentInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        PropertyLoader propertyLoader = new PropertyLoader();
        try {
            propertyLoader.setLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:*.properties"));
            environment.setIgnoreUnresolvableNestedPlaceholders(true);
            environment.getPropertySources().addLast(new PropertiesPropertySource("properties", propertyLoader.loadProperties()));
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    static class PropertyLoader extends PropertySourcesPlaceholderConfigurer {
        Properties loadProperties() throws IOException {
            return mergeProperties();
        }
    }
}
