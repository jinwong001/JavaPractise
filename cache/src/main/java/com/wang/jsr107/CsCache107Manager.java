package com.wang.jsr107;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CsCache107Manager implements CacheManager {
    private final CsCache107Provider cacheProvider;
    private final ClassLoader classLoader;
    private final URI uri;
    private final Properties properties;
    private volatile boolean isClosed;

    private final ConcurrentMap<String, CsCache107<?, ?>> caches = new ConcurrentHashMap<>();


    public CsCache107Manager(CsCache107Provider cache107Provider, Properties properties, ClassLoader classLoader, URI uri) {
        this.cacheProvider = cache107Provider;
        this.properties = properties;
        this.classLoader = classLoader;
        this.uri = uri;
    }

    @Override
    public CachingProvider getCachingProvider() {
        return cacheProvider;
    }

    @Override
    public URI getURI() {
        return uri;
    }

    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public Properties getProperties() {
        return properties;
    }

    @Override
    public <K, V, C extends Configuration<K, V>> Cache<K, V> createCache(String cacheName, C configuration) throws IllegalArgumentException {
        if (isClosed()) {
            throw new IllegalStateException();
        }
        checkNotNull(cacheName, "cacheName");
        checkNotNull(configuration, "configuration");

        CsCache107<?, ?> cache = caches.get(cacheName);
        if (cache == null) {
            cache = new CsCache107<K, V>(this, configuration, cacheName);
            caches.put(cache.getName(), cache);
            return (Cache<K, V>) cache;
        } else {
            throw new CacheException("A cache named " + cacheName + " already exists.");
        }
    }

    @Override
    public <K, V> Cache<K, V> getCache(String cacheName, Class<K> keyClazz, Class<V> valueClazz) {
        if (isClosed()) {
            throw new IllegalStateException();
        }
        checkNotNull(keyClazz, "keyType");
        checkNotNull(valueClazz, "valueType");
        CsCache107<K, V> cache = (CsCache107<K, V>) caches.get(cacheName);
        if (cache == null) {
            return null;
        }

        Configuration<?, ?> configuration = cache.getConfiguration(Configuration.class);
        if (configuration.getKeyType() == null || !configuration.getKeyType().equals(keyClazz)) {
            throw new ClassCastException("Incompatible cache key types specified, expected "
                    + configuration.getKeyType() + " but " + keyClazz + " was specified");
        }

        if (configuration.getValueType() == null || !configuration.getValueType().equals(valueClazz)) {
            throw new ClassCastException("Incompatible cache value types specified, expected "
                    + configuration.getValueType() + " but " + valueClazz + " was specified");
        }

        return cache;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String cacheName) {
        return (Cache<K, V>) getCache(cacheName, Object.class, Object.class);
    }

    @Override
    public Iterable<String> getCacheNames() {
        if (isClosed()) {
            throw new IllegalStateException();
        }
        return Collections.unmodifiableCollection(new ArrayList<String>(caches.keySet()));
    }

    @Override
    public void destroyCache(String cacheName) {
        if (isClosed()) {
            throw new IllegalStateException();
        }

        checkNotNull(cacheName, "cacheName");

        Cache<?, ?> cache = caches.get(cacheName);
        if (cache != null) {
            cache.close();
        }
    }

    @Override
    public void enableManagement(String s, boolean b) {

    }

    @Override
    public void enableStatistics(String s, boolean b) {

    }

    @Override
    public void close() {
        if (!isClosed()) {
            cacheProvider.releaseCacheManager(getURI(), getClassLoader());
            isClosed = true;
            ArrayList<Cache<?, ?>> cacheList = new ArrayList<Cache<?, ?>>(caches.values());
            caches.clear();
            for (Cache<?, ?> cache : cacheList) {
                try {
                    cache.close();
                } catch (Exception e) {

                }
            }
        }
    }

    @Override
    public boolean isClosed() {
        return isClosed;
    }

    @Override
    public <T> T unwrap(Class<T> clazz) {
        if (clazz.isAssignableFrom(getClass())) {
            return clazz.cast(this);
        }

        throw new IllegalArgumentException("Unwapping to " + clazz + " is not a supported by this implementation");
    }

    private void checkNotNull(Object oject, String name) {
        if (oject == null) {
            throw new NullPointerException(name + " can not be null");
        }
    }

    synchronized public void releaseCache(String cacheName) {
        if (cacheName == null) {
            throw new NullPointerException();
        }
        caches.remove(cacheName);
    }
}
