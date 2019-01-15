package core.framework.web.site.cdn;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author neo
 */
public class DefaultCDNSettingsTest {
    CDNSettings settings;

    @Before
    public void createDefaultCDNSettings() {
        settings = new CDNSettings();
    }

    @Test
    public void setCDNHostsWithCommaDelimitedValueWithEmpty() {
        settings.setCDNHostsWithCommaDelimitedValue(null);
        Assert.assertNull(settings.cdnHosts());

        settings.setCDNHostsWithCommaDelimitedValue("");
        Assert.assertNull(settings.cdnHosts());
    }

    @Test
    public void setCDNHostsWithCommaDelimitedValue() {
        settings.setCDNHostsWithCommaDelimitedValue("c1.test.com,c2.test.com");
        Assert.assertEquals(2, settings.cdnHosts().length);
        Assert.assertEquals("c1.test.com", settings.cdnHosts()[0]);
        Assert.assertEquals("c2.test.com", settings.cdnHosts()[1]);
    }
}
