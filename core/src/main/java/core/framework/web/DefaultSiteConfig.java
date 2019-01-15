package core.framework.web;

import com.google.common.base.Charsets;
import core.framework.web.site.SiteErrorControllerAdvice;
import core.framework.web.site.SiteSettings;
import core.framework.web.site.cdn.CDNSettings;
import core.framework.web.site.cookie.CookieContext;
import core.framework.web.site.cookie.CookieInterceptor;
import core.framework.web.site.exception.ErrorPageModelBuilder;
import core.framework.web.site.exception.ExceptionInterceptor;
import core.framework.web.site.form.FormArgumentResolver;
import core.framework.web.site.layout.ModelContext;
import core.framework.web.site.scheme.HTTPSchemeEnforceInterceptor;
import core.framework.web.site.session.SecureSessionContext;
import core.framework.web.site.session.SessionContext;
import core.framework.web.site.session.SessionInterceptor;
import core.framework.web.site.tag.DefaultTagSupport;
import core.framework.web.site.view.DefaultFreeMarkerConfig;
import core.framework.web.site.view.DefaultFreemarkerView;
import core.framework.web.site.view.DefaultFreemarkerViewResolver;
import freemarker.template.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import java.util.List;
import java.util.Properties;

/**
 * @author neo
 */
public abstract class DefaultSiteConfig extends AbstractWebConfig {
    @Bean
    public CDNSettings cdnSettings() {
        return new CDNSettings();
    }

    @Bean
    public SiteSettings siteSettings() {
        return new SiteSettings();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new FormArgumentResolver());
    }

    @Bean
    DefaultTagSupport defaultTagSupport() {
        return new DefaultTagSupport();
    }

    @Bean
    DefaultFreeMarkerConfig freeMarkerConfig() {
        DefaultFreeMarkerConfig config = new DefaultFreeMarkerConfig();
        config.setTemplateLoaderPath("/");
        Properties settings = new Properties();
        settings.setProperty(Configuration.DEFAULT_ENCODING_KEY, Charsets.UTF_8.name());
        settings.setProperty(Configuration.URL_ESCAPING_CHARSET_KEY, Charsets.UTF_8.name());
        settings.setProperty(Configuration.NUMBER_FORMAT_KEY, "0.##");
        settings.setProperty(Configuration.TEMPLATE_EXCEPTION_HANDLER_KEY, "rethrow");
        config.setFreemarkerSettings(settings);
        return config;
    }

    @Bean
    DefaultFreemarkerViewResolver freemarkerViewResolver() {
        DefaultFreemarkerViewResolver resolver = new DefaultFreemarkerViewResolver();
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setSuffix(".ftl");
        resolver.setContentType("text/html; charset=UTF-8");
        resolver.setViewClass(DefaultFreemarkerView.class);
        resolver.setExposeSpringMacroHelpers(false);
        resolver.setExposeRequestAttributes(true);
        resolver.setAllowRequestOverride(true);
        resolver.setRedirectHttp10Compatible(false);
        return resolver;
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    CookieContext cookieContext() {
        return new CookieContext();
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    SessionContext sessionContext() {
        return new SessionContext();
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    SecureSessionContext secureSessionContext() {
        return new SecureSessionContext();
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    ModelContext modelContextContext() {
        return new ModelContext();
    }

    @Bean
    ErrorPageModelBuilder errorPageModelBuilder() {
        return new ErrorPageModelBuilder();
    }

    @Bean
    SiteErrorControllerAdvice siteErrorControllerAdvice() {
        return new SiteErrorControllerAdvice();
    }

    @Bean
    public CookieInterceptor cookieInterceptor() {
        return new CookieInterceptor();
    }

    @Bean
    public SessionInterceptor sessionInterceptor() {
        return new SessionInterceptor();
    }

    @Bean
    public HTTPSchemeEnforceInterceptor httpSchemeEnforceInterceptor() {
        return new HTTPSchemeEnforceInterceptor();
    }

    @Bean
    public ExceptionInterceptor exceptionInterceptor() {
        return new ExceptionInterceptor();
    }
}
