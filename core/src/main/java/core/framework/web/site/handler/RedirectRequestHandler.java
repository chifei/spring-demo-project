package core.framework.web.site.handler;

import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author neo
 */
public class RedirectRequestHandler implements HttpRequestHandler {
    private final String redirectURL;

    public RedirectRequestHandler(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", redirectURL);
    }
}
