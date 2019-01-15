package core.test.site;

import com.google.common.base.Charsets;
import core.framework.web.DefaultSiteConfig;
import core.framework.web.site.view.DefaultFreeMarkerConfig;
import core.framework.web.site.view.DefaultFreemarkerView;
import core.framework.web.site.view.DefaultFreemarkerViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.Properties;

/**
 * @author neo
 */
@Configuration
@ComponentScan(basePackageClasses = WebSiteConfig.class)
@EnableTransactionManagement(proxyTargetClass = true)
public class WebSiteConfig extends DefaultSiteConfig {
    @Bean
    DefaultFreeMarkerConfig freeMarkerConfig() {
        DefaultFreeMarkerConfig config = new DefaultFreeMarkerConfig();
        config.setTemplateLoaderPath("classpath:/templates/");
        Properties settings = new Properties();
        settings.setProperty(freemarker.template.Configuration.DEFAULT_ENCODING_KEY, Charsets.UTF_8.name());
        settings.setProperty(freemarker.template.Configuration.URL_ESCAPING_CHARSET_KEY, Charsets.UTF_8.name());
        settings.setProperty(freemarker.template.Configuration.NUMBER_FORMAT_KEY, "0.##");
        settings.setProperty(freemarker.template.Configuration.TEMPLATE_EXCEPTION_HANDLER_KEY, "rethrow");
        config.setFreemarkerSettings(settings);
        return config;
    }

    @Bean
    DefaultFreemarkerViewResolver freemarkerViewResolver() {
        DefaultFreemarkerViewResolver resolver = new DefaultFreemarkerViewResolver();
        resolver.setPrefix("/");
        resolver.setSuffix(".ftl");
        resolver.setContentType("text/html; charset=UTF-8");
        resolver.setViewClass(DefaultFreemarkerView.class);
        resolver.setExposeSpringMacroHelpers(false);
        resolver.setExposeRequestAttributes(true);
        resolver.setAllowRequestOverride(true);
        return resolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestContextInterceptor());
    }
}
