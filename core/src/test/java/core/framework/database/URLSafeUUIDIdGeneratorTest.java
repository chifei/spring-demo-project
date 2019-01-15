package core.framework.database;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;

/**
 * @author neo
 */
public class URLSafeUUIDIdGeneratorTest {
    URLSafeUUIDIdGenerator generator;

    @Before
    public void createURLSafeUUIDIdGenerator() {
        generator = new URLSafeUUIDIdGenerator();
    }

    @Test
    public void generate() {
        Serializable key = generator.generate(null, null);
        Assert.assertEquals(String.class, key.getClass());
        Assert.assertEquals(URLSafeUUIDIdGenerator.KEY_LENGTH, ((String) key).length());
    }
}
