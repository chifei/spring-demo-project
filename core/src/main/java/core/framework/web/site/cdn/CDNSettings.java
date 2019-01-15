package core.framework.web.site.cdn;

import core.framework.util.StringUtils;

/**
 * @author neo
 */
public class CDNSettings {
    private String[] cdnHosts;

    public String[] cdnHosts() {
        return cdnHosts;
    }

    public void setCDNHostsWithCommaDelimitedValue(String cdnHosts) {
        if (StringUtils.hasText(cdnHosts))
            this.cdnHosts = cdnHosts.split(",");
    }
}
