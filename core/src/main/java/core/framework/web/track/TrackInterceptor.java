package core.framework.web.track;

import core.framework.util.AssertUtils;
import core.framework.web.request.RequestContext;
import core.framework.web.request.RequestContextInterceptor;
import core.framework.web.site.ControllerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author neo
 */
public class TrackInterceptor extends HandlerInterceptorAdapter {
    private final Logger logger = LoggerFactory.getLogger(TrackInterceptor.class);
    @Inject
    RequestContext requestContext;
    @Inject
    RequestContextInterceptor requestContextInterceptor;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AssertUtils.assertTrue(requestContextInterceptor.initialized(request), "trackInterceptor depends on requestContextInterceptor, please check WebConfig");

        Track track = ControllerHelper.findMethodOrClassLevelAnnotation(handler, Track.class);
        if (track != null) {
            trackProcess(track);
        }
    }

    private void trackProcess(Track track) {
        Date startTime = requestContext.requestDate();

        long elapsedTime = System.currentTimeMillis() - startTime.getTime();
        if (warningEnabled(track) && elapsedTime > track.warningThresholdInMs()) {
            logger.warn("process took longer than threshold, elapsedTime(ms)={}", elapsedTime);
        }
    }

    private boolean warningEnabled(Track track) {
        return track.warningThresholdInMs() > 0;
    }
}
