package core.framework.web.site.handler;

import org.springframework.web.HttpRequestHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author neo
 */
public class ForwardRequestHandler implements HttpRequestHandler {
    private final String forwardURL;

    public ForwardRequestHandler(String forwardURL) {
        this.forwardURL = forwardURL;
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(forwardURL);
        dispatcher.forward(request, response);
    }
}
