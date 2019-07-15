package com.wang;

import com.wang.store.DataStore;
import com.wang.store.StoreAccessException;
import com.wang.store.ValueHolder;

public class CsCache<K, V> {

    private final DataStore<K, V> store;

    public CsCache(DataStore<K, V> dataStore) {
        store = dataStore;
    }

    public V get(K key) {
        try {
            ValueHolder<V> value = store.get(key);
            if (value == null) {
                return null;
            }
            return value.value();
        } catch (StoreAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void put(K key, V value) {
        try {
            store.put(key, value);
        } catch (StoreAccessException e) {
            e.printStackTrace();
        }
    }

    public V remove(K key) {
        try {
            ValueHolder<V> v = store.remove(key);
            if (v == null) {
                return null;
            }
            return v.value();
        } catch (StoreAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void clear() {
        try {
            store.clear();
        } catch (StoreAccessException e) {
            e.printStackTrace();
        }
    }
}
