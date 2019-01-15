package core.framework.web.site.session.provider;


import core.framework.util.DateUtils;
import core.framework.web.site.SiteSettings;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author neo
 */
public class LocalSessionProvider implements SessionProvider {
    private final Map<String, SessionValue> values = new ConcurrentHashMap<>();
    @Inject
    SiteSettings siteSettings;

    @Override
    public Map<String, String> getAndRefresh(String sessionId) {
        SessionValue sessionValue = values.get(sessionId);
        if (sessionValue == null) return null;

        if (new Date().after(sessionValue.expiredDate())) {
            values.remove(sessionId);
            return null;
        }

        Map<String, String> data = sessionValue.data();
        values.put(sessionId, new SessionValue(expirationTime(), data));
        return data;
    }

    @Override
    public void save(String sessionId, Map<String, String> sessionData) {
        values.put(sessionId, new SessionValue(expirationTime(), sessionData));
    }

    @Override
    public void clear(String sessionId) {
        values.remove(sessionId);
    }

    private Date expirationTime() {
        return DateUtils.add(new Date(), Calendar.SECOND, (int) siteSettings.sessionTimeOut().toSeconds());
    }

    public static class SessionValue {
        private final Date expiredDate;
        private final Map<String, String> data;

        public SessionValue(Date expiredDate, Map<String, String> data) {
            this.expiredDate = expiredDate;
            this.data = data;
        }

        public Date expiredDate() {
            return expiredDate;
        }

        public Map<String, String> data() {
            return data;
        }
    }
}
