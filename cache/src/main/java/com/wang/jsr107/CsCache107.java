package com.wang.jsr107;

import com.wang.CsCache;
import com.wang.store.impl.BasicDataStore;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.configuration.Configuration;
import javax.cache.integration.CompletionListener;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.EntryProcessorResult;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CsCache107<K, V> implements Cache<K, V> {
    private CsCache<K, V> csCache = new CsCache<K, V>(new BasicDataStore<K, V>());
    private volatile boolean isClosed;
    private CsCache107Manager cacheManager;
    private Configuration<K, V> configuration;
    private String cacheName;


    public CsCache107(CsCache107Manager cacheManager, Configuration<K, V> configuration, String cacheName) {
        this.cacheManager = cacheManager;
        this.configuration = configuration;
        this.cacheName = cacheName;
    }

    @Override
    public V get(K k) {
        return csCache.get(k);
    }

    @Override
    public Map<K, V> getAll(Set<? extends K> set) {
        return null;
    }

    @Override
    public boolean containsKey(K k) {
        return false;
    }

    @Override
    public void loadAll(Set<? extends K> set, boolean b, CompletionListener completionListener) {

    }

    @Override
    public void put(K k, V v) {
        csCache.put(k, v);
    }

    @Override
    public V getAndPut(K k, V v) {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {

    }

    @Override
    public boolean putIfAbsent(K k, V v) {
        return false;
    }

    @Override
    public boolean remove(K k) {
        csCache.remove(k);
        return true;
    }

    @Override
    public boolean remove(K k, V v) {
        return false;
    }

    @Override
    public V getAndRemove(K k) {
        return null;
    }

    @Override
    public boolean replace(K k, V v, V v1) {
        return false;
    }

    @Override
    public boolean replace(K k, V v) {
        return false;
    }

    @Override
    public V getAndReplace(K k, V v) {
        return null;
    }

    @Override
    public void removeAll(Set<? extends K> set) {

    }

    @Override
    public void removeAll() {

    }

    @Override
    public void clear() {
        csCache.clear();
    }

    @Override
    public <C extends Configuration<K, V>> C getConfiguration(Class<C> aClass) {
        return (C) configuration;
    }

    @Override
    public <T> T invoke(K k, EntryProcessor<K, V, T> entryProcessor, Object... objects) throws EntryProcessorException {
        return null;
    }

    @Override
    public <T> Map<K, EntryProcessorResult<T>> invokeAll(Set<? extends K> set, EntryProcessor<K, V, T> entryProcessor, Object... objects) {
        return null;
    }

    @Override
    public String getName() {
        return cacheName;
    }

    @Override
    public CacheManager getCacheManager() {
        return cacheManager;
    }

    @Override
    public void close() {
        if (!isClosed) {
            isClosed = true;
            if (cacheManager != null) {
                cacheManager.releaseCache(cacheName);
            }
            csCache.clear();
        }
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> clazz) {
        if (clazz.isAssignableFrom(csCache.getClass())) {
            return (T) this.csCache;
        }

        throw new IllegalArgumentException("Unwrapping to " + clazz + " is not " + "supported by this implementation");
    }

    @Override
    public void registerCacheEntryListener(CacheEntryListenerConfiguration<K, V> cacheEntryListenerConfiguration) {

    }

    @Override
    public void deregisterCacheEntryListener(CacheEntryListenerConfiguration<K, V> cacheEntryListenerConfiguration) {

    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return null;
    }
}
