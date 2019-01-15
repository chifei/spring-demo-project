package core.framework.cache;

import core.framework.web.runtime.RuntimeSettings;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author neo
 */
public class DefaultCacheKeyGeneratorTest {
    DefaultCacheKeyGenerator defaultCacheKeyGenerator;

    @Before
    public void createDefaultCacheKeyGenerator() {
        defaultCacheKeyGenerator = new DefaultCacheKeyGenerator();
        defaultCacheKeyGenerator.runtimeSettings = new RuntimeSettings();
    }

    @Test
    public void encodeKeyIfContainsIllegalChar() {
        String encodedKey = defaultCacheKeyGenerator.buildKey(new String[]{"contains space key"});
        Assert.assertEquals("illegal key should be encoded by md5, which is 32 chars long", 32, encodedKey.length());
    }
}
