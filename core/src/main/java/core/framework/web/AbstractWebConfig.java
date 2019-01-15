package core.framework.web;

import core.framework.log.LogSettings;
import core.framework.managment.ManagementControllerConfig;
import core.framework.monitor.MonitorControllerConfig;
import core.framework.monitor.ServerStatus;
import core.framework.util.JSONBinder;
import core.framework.util.RuntimeIOException;
import core.framework.web.config.DefaultConfig;
import core.framework.web.filter.PlatformFilter;
import core.framework.web.i18n.Messages;
import core.framework.web.request.RequestContextInterceptor;
import core.framework.web.runtime.RuntimeSettings;
import core.framework.web.track.TrackInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author neo
 */
@EnableWebMvc
@Import({DefaultConfig.class, MonitorControllerConfig.class, ManagementControllerConfig.class})
public abstract class AbstractWebConfig extends WebMvcConfigurerAdapter implements WebApplicationInitializer {
    @Bean
    static PropertySourcesPlaceholderConfigurer propertyLoader() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(getClass());
        new EnvironmentInitializer().initialize(context);
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet(AbstractDispatcherServletInitializer.DEFAULT_SERVLET_NAME, new DispatcherServlet(context));
        dispatcher.addMapping("/");
        dispatcher.setLoadOnStartup(1);
        servletContext.addFilter("platformFilter", new PlatformFilter()).addMappingForUrlPatterns(null, false, "/*");
    }

    @Bean
    Messages messages() {
        try {
            Resource[] messageResources = new PathMatchingResourcePatternResolver().getResources("classpath*:messages/*.properties");
            Messages messages = new Messages();
            String[] baseNames = new String[messageResources.length];
            for (int i = 0, messageResourcesLength = messageResources.length; i < messageResourcesLength; i++) {
                Resource messageResource = messageResources[i];
                String filename = messageResource.getFilename();
                baseNames[i] = "messages/" + filename.substring(0, filename.indexOf('_'));
            }
            messages.setBasenames(baseNames);
            return messages;
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messages());
        validator.afterPropertiesSet();
        return validator;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new ByteArrayHttpMessageConverter());
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
        stringConverter.setWriteAcceptCharset(false);
        ArrayList<MediaType> textTypes = new ArrayList<>();
        textTypes.add(MediaType.TEXT_PLAIN);
        textTypes.add(MediaType.TEXT_HTML);
        textTypes.add(MediaType.TEXT_XML);
        textTypes.add(MediaType.APPLICATION_XML);
        textTypes.add(MediaType.APPLICATION_JSON);
        stringConverter.setSupportedMediaTypes(textTypes);
        converters.add(stringConverter);
        converters.add(new Jaxb2RootElementHttpMessageConverter());
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setObjectMapper(JSONBinder.objectMapper());
        converters.add(jsonConverter);
    }

    @Bean
    public RuntimeSettings runtimeSettings() {
        return new RuntimeSettings();
    }

    @Bean
    public LogSettings logSettings() {
        return LogSettings.get();
    }

    @Bean
    public RequestContextInterceptor requestContextInterceptor() {
        return new RequestContextInterceptor();
    }

    @Bean
    public TrackInterceptor trackInterceptor() {
        return new TrackInterceptor();
    }

    @Bean
    public ServerStatus serverStatus() {
        return new ServerStatus();
    }
}
