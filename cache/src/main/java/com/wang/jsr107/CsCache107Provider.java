package com.wang.jsr107;

import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.OptionalFeature;
import javax.cache.spi.CachingProvider;
import javax.xml.bind.annotation.XmlType;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CsCache107Provider implements CachingProvider {
    private static final String DEFAULT_URI_STRING = "urn:X-cscache:jsr107-default-config";

    private static final URI URI_DEFAULT;

    static {
        try {
            URI_DEFAULT = new URI(DEFAULT_URI_STRING);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new CacheException(e);
        }
    }


    private final Map<ClassLoader, ConcurrentMap<URI, CacheManager>> cacheManages = new WeakHashMap<ClassLoader, ConcurrentMap<URI, CacheManager>>();

    @Override
    public CacheManager getCacheManager(URI uri, ClassLoader classLoader, Properties properties) {
        uri = uri == null ? getDefaultURI() : uri;
        classLoader = classLoader == null ? getDefaultClassLoader() : classLoader;
        properties = properties == null ? new Properties() : cloneProperties(properties);

        ConcurrentMap<URI, CacheManager> cacheManagersByURI = cacheManages.get(classLoader);
        if (cacheManagersByURI == null) {
            cacheManagersByURI = new ConcurrentHashMap<URI, CacheManager>();
        }

        CacheManager cacheManager = cacheManagersByURI.get(uri);
        if (cacheManager == null) {
            cacheManager = new CsCache107Manager(this, properties, classLoader, uri);
            cacheManagersByURI.put(uri, cacheManager);
        }
        if (!cacheManages.containsKey(classLoader)) {
            cacheManages.put(classLoader, cacheManagersByURI);
        }
        return cacheManager;
    }

    @Override
    public ClassLoader getDefaultClassLoader() {
        return getClass().getClassLoader();
    }

    @Override
    public URI getDefaultURI() {
        return URI_DEFAULT;
    }

    @Override
    public Properties getDefaultProperties() {
        return new Properties();
    }

    @Override
    public CacheManager getCacheManager(URI uri, ClassLoader classLoader) {
        return getCacheManager(uri, classLoader, getDefaultProperties());
    }

    @Override
    public CacheManager getCacheManager() {
        return getCacheManager(getDefaultURI(), getDefaultClassLoader());
    }

    @Override
    public void close() {

    }

    @Override
    public void close(ClassLoader classLoader) {

    }

    @Override
    public void close(URI uri, ClassLoader classLoader) {

    }

    @Override
    public boolean isSupported(OptionalFeature optionalFeature) {
        return false;
    }

    private static Properties cloneProperties(Properties properties) {
        Properties clone = new Properties();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            clone.put(entry.getKey(), entry.getValue());
        }
        return clone;
    }

    public void releaseCacheManager(URI uri, ClassLoader classLoader) {
        if (uri == null || classLoader == null) {
            throw new NullPointerException("uri or classLoader should not be null");
        }

        ConcurrentMap<URI, CacheManager> cacheManagerConcurrentMap = cacheManages.get(classLoader);
        if (cacheManagerConcurrentMap != null) {
            cacheManagerConcurrentMap.remove(uri);
            if (cacheManagerConcurrentMap.size() == 0) {
                cacheManages.remove(classLoader);
            }
        }
    }
}
