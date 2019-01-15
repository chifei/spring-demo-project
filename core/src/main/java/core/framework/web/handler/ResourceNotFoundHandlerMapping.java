package core.framework.web.handler;

import core.framework.exception.ResourceNotFoundException;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author neo
 */
public class ResourceNotFoundHandlerMapping extends AbstractHandlerMapping {
    public ResourceNotFoundHandlerMapping() {
        setOrder(LOWEST_PRECEDENCE);
    }

    @Override
    protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
        throw new ResourceNotFoundException(request.getServletPath() + " not found");
    }
}
