package core.framework.cache;

import core.framework.util.DigestUtils;
import core.framework.web.runtime.RuntimeSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.KeyGenerator;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author neo
 */
public class DefaultCacheKeyGenerator implements KeyGenerator {
    private final Logger logger = LoggerFactory.getLogger(DefaultCacheKeyGenerator.class);

    @Inject
    RuntimeSettings runtimeSettings;

    @Override
    public String generate(Object target, Method method, Object... params) {
        String key = buildKey(params);
        logger.debug("generate cache key, method={}, params={}, key={}", method, params, key);
        return key;
    }

    public String buildKey(Object[] params) {
        StringBuilder builder = new StringBuilder();

        if (params.length == 0) {
            builder.append("default");
        } else {
            for (Object param : params) {
                String value = getKeyValue(param);
                builder.append(value).append(':');
            }
        }
        builder.append(runtimeSettings.version());

        String key = builder.toString();

        // for long key or illegal key, use md5 to short
        if (key.length() > 128 || containsIllegalKeyChar(key)) {
            return DigestUtils.md5(key);
        }
        return key;
    }

    private String getKeyValue(Object param) {
        if (param instanceof CacheKeyGenerator)
            return ((CacheKeyGenerator) param).buildCacheKey();
        if (param instanceof Enum)
            return ((Enum) param).name();
        if (param instanceof Date)
            return String.valueOf(((Date) param).getTime());
        return String.valueOf(param);
    }

    private boolean containsIllegalKeyChar(String value) {
        return value.contains(" ");
    }
}
