import core.framework.web.EnvironmentInitializer;
import core.framework.web.filter.PlatformFilter;
import demo.WebConfig;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletRegistration;
import java.io.IOException;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        new Main().startJetty(PORT);
    }

    private void startJetty(int port) throws Exception {
        LOGGER.debug("Starting server at port {}", port);
        Server server = new Server(port);

        server.setHandler(getServletContextHandler());

        addRuntimeShutdownHook(server);

        server.start();
        LOGGER.info("Server started at port {}", port);
        server.join();
    }

    private static ServletContextHandler getServletContextHandler() throws IOException {
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS); // SESSIONS requerido para JSP 
        contextHandler.setErrorHandler(null);

        contextHandler.setResourceBase(new FileSystemResource("D:\\Workspace\\spring-demo-project\\demo-website\\src\\main\\resources\\webapp").getURI().toString());
        contextHandler.setContextPath("/");

        contextHandler.setClassLoader(Thread.currentThread().getContextClassLoader()); // Necesario para cargar JspServlet

        AnnotationConfigWebApplicationContext webAppContext = getWebApplicationContext();
        contextHandler.addEventListener(new ContextLoaderListener(webAppContext) {
            @Override
            public void contextInitialized(ServletContextEvent event) {
                super.contextInitialized(event);
                new EnvironmentInitializer().initialize(webAppContext);
                ServletContext servletContext = event.getServletContext();
                ServletRegistration.Dynamic dispatcher = servletContext.addServlet(AbstractDispatcherServletInitializer.DEFAULT_SERVLET_NAME, new DispatcherServlet(webAppContext));
                dispatcher.addMapping("/");
                dispatcher.setLoadOnStartup(1);
                servletContext.addFilter("platformFilter", new PlatformFilter()).addMappingForUrlPatterns(null, false, "/*");
            }
        });

        return contextHandler;
    }

    private static AnnotationConfigWebApplicationContext getWebApplicationContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(WebConfig.class);
        return context;
    }

    private static void addRuntimeShutdownHook(final Server server) {
        Runtime.getRuntime().addShutdownHook(
            new Thread(() -> {
                if (server.isStarted()) {
                    server.setStopAtShutdown(true);
                    try {
                        server.stop();
                    } catch (Exception e) {
                        LOGGER.error("Error while stopping jetty server: " + e.getMessage(), e);
                    }
                }
            }));
    }

}