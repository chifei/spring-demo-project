package core.framework.web.request;

import core.framework.log.TraceLogger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.web.method.HandlerMethod;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author neo
 */
public class RequestContextInterceptorTest {
    RequestContextInterceptor requestContextInterceptor;
    TraceLogger traceLogger;

    @Before
    public void createRequestContextInterceptor() {
        traceLogger = Mockito.mock(TraceLogger.class);

        requestContextInterceptor = new RequestContextInterceptor();
        requestContextInterceptor.traceLogger = traceLogger;
    }

    @Test
    public void assignActionWithFirstHandlerMethod() throws NoSuchMethodException {
        String expectedAction = "RequestContextInterceptorTest$TestController-execute";

        when(traceLogger.action()).thenReturn(null);
        requestContextInterceptor.assignAction(new HandlerMethod(new TestController(), TestController.class.getMethod("execute")));

        verify(traceLogger).action(expectedAction);
    }

    @Test
    public void assignActionWhenAssignedAlready() throws NoSuchMethodException {
        when(traceLogger.action()).thenReturn("assigned-action");
        requestContextInterceptor.assignAction(new HandlerMethod(new TestController(), TestController.class.getMethod("execute")));

        verify(traceLogger, never()).action(Matchers.<String>any());
    }

    public static class TestController {
        public String execute() {
            return "view";
        }
    }
}

