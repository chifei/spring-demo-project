package core.framework.cache;

/**
 * @author neo
 */
public class CacheSettings {
    private CacheProvider cacheProvider = CacheProvider.LOCAL;
    private String remoteCacheServer;

    public CacheProvider cacheProvider() {
        return cacheProvider;
    }

    public void setCacheProvider(CacheProvider cacheProvider) {
        this.cacheProvider = cacheProvider;
    }

    public String remoteCacheServer() {
        return remoteCacheServer;
    }

    public void setRemoteCacheServer(String remoteCacheServer) {
        this.remoteCacheServer = remoteCacheServer;
    }
}
