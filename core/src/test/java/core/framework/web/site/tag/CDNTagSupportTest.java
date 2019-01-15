package core.framework.web.site.tag;

import core.framework.web.runtime.RuntimeSettings;
import core.framework.web.site.cdn.CDNSettings;
import freemarker.template.SimpleScalar;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertTrue;

/**
 * @author neo
 */
public class CDNTagSupportTest {
    CDNTagSupport cdnTagSupport;
    MockHttpServletRequest request;
    RuntimeSettings runtimeSettings;
    CDNSettings cdnSettings;

    @Before
    public void createCDNTagSupport() {
        request = new MockHttpServletRequest();
        cdnSettings = new CDNSettings();
        runtimeSettings = new RuntimeSettings();
        cdnTagSupport = new CDNTagSupport(request, runtimeSettings, cdnSettings);
    }

    @Test
    public void constructCDNURL() {
        cdnSettings.setCDNHostsWithCommaDelimitedValue("cdn.test.com");
        runtimeSettings.setVersion("1.0");
        request.setScheme("http");
        String url = cdnTagSupport.constructCDNURL("/static/css/default.css");

        Assert.assertEquals("http://cdn.test.com/static/css/default.css?version=1.0", url);
    }

    @Test
    public void constructCDNURLWithParam() {
        cdnSettings.setCDNHostsWithCommaDelimitedValue("cdn.test.com");
        runtimeSettings.setVersion("1.0");
        request.setScheme("http");
        String url = cdnTagSupport.constructCDNURL("/static/css/default.css?param=1");

        Assert.assertEquals("http://cdn.test.com/static/css/default.css?param=1&version=1.0", url);
    }

    @Test
    public void determineCDNHostWithOneCDNHost() {
        cdnSettings.setCDNHostsWithCommaDelimitedValue("cdn.test.com");
        String host = cdnTagSupport.determineCDNHost("/a");

        Assert.assertEquals("cdn.test.com", host);
    }

    @Test
    public void determineCDNHost() {
        cdnSettings.setCDNHostsWithCommaDelimitedValue("c1.test.com,c2.test.com");
        Assert.assertEquals("c1.test.com", cdnTagSupport.determineCDNHost("/a")); // hash = 1554
        Assert.assertEquals("c2.test.com", cdnTagSupport.determineCDNHost("/b")); // hash = 1555

        // from prod bug
        cdnSettings.setCDNHostsWithCommaDelimitedValue("c1.test.com,c2.test.com,c3.test.com,c4.test.com");
        Assert.assertEquals("c3.test.com", cdnTagSupport.determineCDNHost("/mstatic/images/nvhp_rvhp_banner.gif")); // hash = -2026818022
    }

    @Test
    public void buildExtAttributes() {
        Map<String, Object> params = new TreeMap<>(); // use tree map to preserve order of keys for test
        params.put("key1", new SimpleScalar("value1"));
        params.put("key2", new SimpleScalar("value2"));
        params.put("key3", new SimpleScalar("value3"));
        String attributeText = cdnTagSupport.buildExtAttributes(params, "key1");
        Assert.assertEquals(" key2=\"value2\" key3=\"value3\"", attributeText);
    }

    @Test
    public void supportCDN() {
        cdnSettings.setCDNHostsWithCommaDelimitedValue("cdn.test.com");
        request.setSecure(true);

        assertTrue(cdnTagSupport.supportCDN());
    }

    @Test
    public void constructLocalURLShouldAppendVersion() {
        runtimeSettings.setVersion("1.0");
        String url = cdnTagSupport.constructLocalURL("/static/css/default.css");

        Assert.assertEquals("/static/css/default.css?version=1.0", url);
    }

    @Test
    public void constructCDNURLWithFullURL() {
        Assert.assertEquals("http://www.test.com/static/css/default.css?version=1.0", cdnTagSupport.constructCDNURL("http://www.test.com/static/css/default.css?version=1.0"));
        Assert.assertEquals("https://www.test.com/static/css/default.css?version=1.0", cdnTagSupport.constructCDNURL("https://www.test.com/static/css/default.css?version=1.0"));
    }
}
