package core.framework.web.site;

import core.framework.util.AssertUtils;
import core.framework.util.TimeLength;
import core.framework.web.site.session.SessionProviderType;
import core.framework.web.site.tag.CustomTagSupport;

import java.lang.reflect.Modifier;

/**
 * @author neo
 */
public class SiteSettings {
    private String errorPage;
    private String resourceNotFoundPage;
    private String sessionTimeOutPage;

    private TimeLength sessionTimeOut = TimeLength.minutes(15);
    private SessionProviderType sessionProviderType = SessionProviderType.LOCAL;
    private String remoteSessionServer;
    private String jsDir;
    private String cssDir;

    private Class<? extends CustomTagSupport> customTagSupport;

    public String errorPage() {
        return errorPage;
    }

    public void setErrorPage(String errorPage) {
        this.errorPage = errorPage;
    }

    public String resourceNotFoundPage() {
        return resourceNotFoundPage;
    }

    public void setResourceNotFoundPage(String resourceNotFoundPage) {
        this.resourceNotFoundPage = resourceNotFoundPage;
    }

    public TimeLength sessionTimeOut() {
        return sessionTimeOut;
    }

    public void setSessionTimeOut(TimeLength sessionTimeOut) {
        this.sessionTimeOut = sessionTimeOut;
    }

    public SessionProviderType sessionProviderType() {
        return sessionProviderType;
    }

    public void setSessionProviderType(SessionProviderType sessionProviderType) {
        this.sessionProviderType = sessionProviderType;
    }

    public String remoteSessionServer() {
        return remoteSessionServer;
    }

    public void setRemoteSessionServer(String remoteSessionServer) {
        this.remoteSessionServer = remoteSessionServer;
    }

    public String sessionTimeOutPage() {
        return sessionTimeOutPage;
    }

    public void setSessionTimeOutPage(String sessionTimeOutPage) {
        this.sessionTimeOutPage = sessionTimeOutPage;
    }

    public String jsDir() {
        return jsDir;
    }

    public void setJSDir(String jsDir) {
        this.jsDir = jsDir;
    }

    public String cssDir() {
        return cssDir;
    }

    public void setCSSDir(String cssDir) {
        this.cssDir = cssDir;
    }

    public Class<? extends CustomTagSupport> customTagSupport() {
        return customTagSupport;
    }

    public void setCustomTagSupport(Class<? extends CustomTagSupport> customTagSupport) {
        AssertUtils.assertFalse(Modifier.isAbstract(customTagSupport.getModifiers()) || Modifier.isInterface(customTagSupport.getModifiers()), "customTagSupport must be concrete class");
        this.customTagSupport = customTagSupport;
    }
}
