package core.framework.cache;

import core.framework.database.RedisAccess;
import core.framework.util.TimeLength;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;
import java.util.TreeSet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RedisCacheTest {
    RedisCache redisCache;
    RedisAccess redisAccess;

    @Before
    public void createRedisCache() {
        redisAccess = mock(RedisAccess.class);

        redisCache = new RedisCache("cache", Object.class, TimeLength.minutes(30), redisAccess);
    }

    @Test
    public void deleteInBatch() {
        Set<String> keys = new TreeSet<>();
        for (int i = 0; i < 10; i++) {
            keys.add(String.valueOf(i));
        }
        redisCache.deleteInBatch(keys, 5);

        verify(redisAccess).del("0", "1", "2", "3", "4");
        verify(redisAccess).del("5", "6", "7", "8", "9");
    }

    @Test
    public void deleteInBatchLessThanOneBatch() {
        Set<String> keys = new TreeSet<>();
        for (int i = 0; i < 3; i++) {
            keys.add(String.valueOf(i));
        }
        redisCache.deleteInBatch(keys, 5);

        verify(redisAccess).del("0", "1", "2");
    }

    @Test
    public void deleteInBatchWithLeft() {
        Set<String> keys = new TreeSet<>();
        for (int i = 0; i < 12; i++) {
            keys.add(String.format("%02d", i));
        }
        redisCache.deleteInBatch(keys, 5);

        verify(redisAccess).del("00", "01", "02", "03", "04");
        verify(redisAccess).del("05", "06", "07", "08", "09");
        verify(redisAccess).del("10", "11");
    }
}