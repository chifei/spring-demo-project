package core.framework.database;

import core.framework.util.TimeLength;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.Set;

/**
 * @author neo
 */
public class RedisAccess {
    private JedisPool redisPool;

    public String get(String key) {
        Jedis redis = redisPool.getResource();
        try {
            String value = redis.get(key);
            redisPool.returnResource(redis);
            return value;
        } catch (Throwable e) {
            redisPool.returnBrokenResource(redis);
            throw e;
        }
    }

    public void set(String key, String value) {
        Jedis redis = redisPool.getResource();
        try {
            redis.set(key, value);
            redisPool.returnResource(redis);
        } catch (Throwable e) {
            redisPool.returnBrokenResource(redis);
            throw e;
        }
    }

    public void expire(String key, TimeLength expirationTime) {
        Jedis redis = redisPool.getResource();
        try {
            redis.expire(key, (int) expirationTime.toSeconds());
            redisPool.returnResource(redis);
        } catch (Throwable e) {
            redisPool.returnBrokenResource(redis);
            throw e;
        }
    }

    public void setExpire(String key, String value, TimeLength expirationTime) {
        Jedis redis = redisPool.getResource();
        try {
            redis.setex(key, (int) expirationTime.toSeconds(), value);
            redisPool.returnResource(redis);
        } catch (Throwable e) {
            redisPool.returnBrokenResource(redis);
            throw e;
        }
    }

    public void del(String... keys) {
        Jedis redis = redisPool.getResource();
        try {
            redis.del(keys);
            redisPool.returnResource(redis);
        } catch (Throwable e) {
            redisPool.returnBrokenResource(redis);
            throw e;
        }
    }

    public Map<String, String> hgetAll(String key) {
        Jedis redis = redisPool.getResource();
        try {
            Map<String, String> value = redis.hgetAll(key);
            redisPool.returnResource(redis);
            return value;
        } catch (Throwable e) {
            redisPool.returnBrokenResource(redis);
            throw e;
        }
    }

    public void hmset(String key, Map<String, String> value) {
        Jedis redis = redisPool.getResource();
        try {
            redis.hmset(key, value);
            redisPool.returnResource(redis);
        } catch (Throwable e) {
            redisPool.returnBrokenResource(redis);
            throw e;
        }
    }

    public Set<String> keys(String pattern) {
        Jedis redis = redisPool.getResource();
        try {
            Set<String> keys = redis.keys(pattern);
            redisPool.returnResource(redis);
            return keys;
        } catch (Throwable e) {
            redisPool.returnBrokenResource(redis);
            throw e;
        }
    }

    public void setRedisPool(JedisPool redisPool) {
        this.redisPool = redisPool;
    }
}
