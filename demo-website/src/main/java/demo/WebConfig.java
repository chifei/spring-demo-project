package demo;

import core.framework.web.DefaultSiteConfig;
import core.framework.web.site.SiteSettings;
import core.framework.web.site.layout.ModelBuilderInterceptor;
import demo.web.interceptor.LoginRequiredInterceptor;
import demo.web.interceptor.PermissionRequiredInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import java.util.Locale;

/**
 * @author neo
 */
@Configuration
@ComponentScan(basePackageClasses = WebConfig.class)
@EnableTransactionManagement(proxyTargetClass = true)
public class WebConfig extends DefaultSiteConfig {
    static {
        Locale.setDefault(Locale.US);
    }

    @Override
    public SiteSettings siteSettings() {
        SiteSettings settings = new SiteSettings();
        settings.setCSSDir("/static/css");
        settings.setErrorPage("/error");
        settings.setResourceNotFoundPage("/error");
        return settings;
    }

    @Bean
    public SpringTemplateEngine thymeleafTemplateEngine() {
        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
        resolver.setPrefix("/WEB-INF/templates");
        resolver.setTemplateMode("HTML5");
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(resolver);
        return engine;
    }

    @Bean
    public ThymeleafViewResolver thymeleafViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(thymeleafTemplateEngine());
        resolver.setOrder(2);
        resolver.setViewNames(new String[]{"*.html"});
        resolver.setCharacterEncoding("UTF-8");
        resolver.setContentType("text/html;charset=UTF-8");
        return resolver;
    }

    @Bean
    public ModelBuilderInterceptor modelBuilderInterceptor() {
        return new ModelBuilderInterceptor();
    }

    @Bean
    public LoginRequiredInterceptor loginRequiredInterceptor() {
        return new LoginRequiredInterceptor();
    }

    @Bean
    public PermissionRequiredInterceptor permissionRequiredInterceptor() {
        return new PermissionRequiredInterceptor();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(exceptionInterceptor());
        registry.addInterceptor(requestContextInterceptor());
        registry.addInterceptor(httpSchemeEnforceInterceptor());
        registry.addInterceptor(cookieInterceptor());
        registry.addInterceptor(sessionInterceptor());
        registry.addInterceptor(modelBuilderInterceptor());
        registry.addInterceptor(loginRequiredInterceptor());
        registry.addInterceptor(permissionRequiredInterceptor());
    }

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize(1000 * 1000); // 1M
        return resolver;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/home");
    }
}
