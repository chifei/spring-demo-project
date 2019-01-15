package core.framework.web.site.layout;

import core.framework.internal.SpringObjectFactory;
import core.framework.util.AssertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * @author neo
 */
public class ModelBuilderInterceptor extends HandlerInterceptorAdapter {
    private static final String ATTRIBUTE_CONTEXT_INITIALIZED = ModelBuilderInterceptor.class.getName() + ".CONTEXT_INITIALIZED";

    private final Logger logger = LoggerFactory.getLogger(ModelBuilderInterceptor.class);
    private final Map<Class<? extends Annotation>, ModelBuilder> modelBuilders = new HashMap<>();
    private Map<Class<? extends Annotation>, Class<? extends ModelBuilder>> modelBuilderClasses = new HashMap<>();
    private ModelContext modelContext;
    private SpringObjectFactory springObjectFactory;

    @PostConstruct
    public void initialize() {
        for (Map.Entry<Class<? extends Annotation>, Class<? extends ModelBuilder>> entry : modelBuilderClasses.entrySet()) {
            Class<? extends Annotation> annotationClass = entry.getKey();
            Class<? extends ModelBuilder> modelBuilderClass = entry.getValue();
            ModelBuilder modelBuilder = springObjectFactory.create(modelBuilderClass);
            ModelBuilder previousModelBuilder = modelBuilders.put(annotationClass, modelBuilder);
            AssertUtils.assertNull(previousModelBuilder, "duplicated modelBuilder found, annotation={}, modelBuilderClass={}", annotationClass, modelBuilderClass);
        }
        modelBuilderClasses = null; // remove unneeded registry
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) return true;

        if (!initialized(request)) {
            logger.debug("initialize modelContext");

            Map<String, Object> model = modelContext.getModel();
            Class<?> controllerClass = ((HandlerMethod) handler).getBeanType();
            for (Map.Entry<Class<? extends Annotation>, ModelBuilder> entry : modelBuilders.entrySet()) {
                Class<? extends Annotation> annotationClass = entry.getKey();
                if (isAnnotationPresentInClassHierarchy(controllerClass, annotationClass)) {
                    ModelBuilder builder = entry.getValue();
                    logger.debug("prepare model by model builder, annotation={}, builder={}", annotationClass.getName(), builder.getClass().getName());
                    builder.build(model);
                }
            }

            request.setAttribute(ATTRIBUTE_CONTEXT_INITIALIZED, Boolean.TRUE);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (!isTemplateView(modelAndView, handler)) return;

        logger.debug("merge modelContext to result modelAndView");
        Map<String, Object> model = modelAndView.getModel();
        modelContext.mergeToModel(model);
    }

    private boolean initialized(HttpServletRequest request) {
        Boolean initialized = (Boolean) request.getAttribute(ATTRIBUTE_CONTEXT_INITIALIZED);
        return Boolean.TRUE.equals(initialized);
    }

    private boolean isTemplateView(ModelAndView modelAndView, Object handler) {
        if (modelAndView == null) return false; // for @ResponseBody
        String viewName = modelAndView.getViewName();
        if (viewName == null) return false;
        if (viewName.startsWith(UrlBasedViewResolver.FORWARD_URL_PREFIX)
            || viewName.startsWith(UrlBasedViewResolver.REDIRECT_URL_PREFIX)) return false; // forward and redirect
        if (!(handler instanceof HandlerMethod) || modelAndView.getModel() == null)
            return false;   // no model is injected to Controller
        return true;
    }

    private boolean isAnnotationPresentInClassHierarchy(Class<?> controllerClass, Class<? extends Annotation> annotationClass) {
        Class<?> targetClass = controllerClass;
        while (true) {
            if (targetClass.isAnnotationPresent(annotationClass)) return true;
            targetClass = targetClass.getSuperclass();
            if (Object.class.equals(targetClass)) return false;
        }
    }

    public void registerModelBuilder(Class<? extends Annotation> annotationClass, Class<? extends ModelBuilder> modelBuilderClass) {
        modelBuilderClasses.put(annotationClass, modelBuilderClass);
    }

    @Inject
    public void setModelContext(ModelContext modelContext) {
        this.modelContext = modelContext;
    }

    @Inject
    public void setSpringObjectFactory(SpringObjectFactory springObjectFactory) {
        this.springObjectFactory = springObjectFactory;
    }
}