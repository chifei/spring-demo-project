package core.framework.web.site.session.provider;

import core.framework.database.RedisAccess;
import core.framework.util.TimeLength;
import core.framework.web.site.SiteSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.util.Map;

/**
 * @author neo
 */
public class RedisSessionProvider implements SessionProvider {
    private static final TimeLength REDIS_TIME_OUT = TimeLength.seconds(5);
    private final Logger logger = LoggerFactory.getLogger(RedisSessionProvider.class);
    @Inject
    SiteSettings siteSettings;
    JedisPool redisPool;
    RedisAccess redisAccess;

    @PostConstruct
    public void initialize() throws Exception {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxWaitMillis(REDIS_TIME_OUT.toMilliseconds());
        redisAccess = new RedisAccess();
        redisPool = new JedisPool(config, siteSettings.remoteSessionServer());
        redisAccess.setRedisPool(redisPool);
    }

    @PreDestroy
    public void shutdown() {
        logger.info("shutdown redis pool");
        redisPool.destroy();
    }

    @Override
    public Map<String, String> getAndRefresh(String sessionId) {
        String key = sessionKey(sessionId);
        redisAccess.expire(key, siteSettings.sessionTimeOut());
        return redisAccess.hgetAll(key);
    }

    @Override
    public void save(String sessionId, Map<String, String> sessionData) {
        String key = sessionKey(sessionId);
        redisAccess.expire(key, siteSettings.sessionTimeOut());
        redisAccess.hmset(key, sessionData);
    }

    @Override
    public void clear(String sessionId) {
        String key = sessionKey(sessionId);
        redisAccess.del(key);
    }

    private String sessionKey(String sessionId) {
        return "session:" + sessionId;
    }
}
