package core.framework.web.runtime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author neo
 */
public class RuntimeSettingsTest {
    RuntimeSettings runtimeSettings;

    @Before
    public void createRuntimeSettings() {
        runtimeSettings = new RuntimeSettings();
    }

    @Test
    public void setVersion() {
        runtimeSettings.setVersion("${project.build.time}");
        Assert.assertEquals("current", runtimeSettings.version());
    }
}