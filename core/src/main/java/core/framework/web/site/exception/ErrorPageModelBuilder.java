package core.framework.web.site.exception;


import core.framework.web.request.RequestContext;
import core.framework.web.runtime.RuntimeEnvironment;
import core.framework.web.runtime.RuntimeSettings;
import core.framework.web.site.layout.ModelContext;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Single place to build model needed to render error page for web site only
 *
 * @author neo
 */
public class ErrorPageModelBuilder {
    static final String ERROR_MESSAGE_NULL_POINTER_EXCEPTION = "null pointer exception";
    @Inject
    RuntimeSettings runtimeSettings;
    @Inject
    ModelContext modelContext;
    @Inject
    RequestContext requestContext;

    public Map<String, Object> buildErrorPageModel(Throwable exception) {
        return buildErrorPageModel(getErrorMessage(exception), exception);
    }

    String getErrorMessage(Throwable exception) {
        if (exception instanceof NullPointerException) return ERROR_MESSAGE_NULL_POINTER_EXCEPTION;
        return exception.getMessage();
    }

    public Map<String, Object> buildErrorPageModel(String errorMessage, Throwable exception) {
        Map<String, Object> model = new HashMap<>();

        if (RuntimeEnvironment.DEV.equals(runtimeSettings.environment())) {
            model.put("exception", new ExceptionInfo(errorMessage, exception));
        }

        model.put("requestContext", requestContext);
        modelContext.mergeToModel(model);
        return model;
    }
}