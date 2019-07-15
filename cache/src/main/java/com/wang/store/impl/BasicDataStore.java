package com.wang.store.impl;

import com.wang.store.DataStore;
import com.wang.store.StoreAccessException;
import com.wang.store.ValueHolder;

import java.util.concurrent.ConcurrentHashMap;

public class BasicDataStore<K, V> implements DataStore<K, V> {
    ConcurrentHashMap<K, ValueHolder<V>> map = new ConcurrentHashMap<>();

    @Override
    public ValueHolder<V> get(K key) throws StoreAccessException {
        return map.get(key);
    }

    @Override
    public PutStatus put(K key, V value) throws StoreAccessException {
        ValueHolder<V> v = new BasicValueHolder<>(value);
        map.put(key, v);
        return PutStatus.PUT;
    }

    @Override
    public ValueHolder<V> remove(K key) throws StoreAccessException {
        return map.remove(key);
    }

    @Override
    public void clear() throws StoreAccessException {
        map.clear();
    }
}
