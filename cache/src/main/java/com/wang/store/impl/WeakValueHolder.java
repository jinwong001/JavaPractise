package com.wang.store.impl;

import com.wang.store.ValueHolder;

import java.lang.ref.WeakReference;

public class WeakValueHolder<V> implements ValueHolder<V> {
    private WeakReference<V> v;

    public WeakValueHolder(V value) {
        super();
        if (value == null) {
            return;
        }
        this.v = new WeakReference<>(value);
    }

    @Override
    public V value() {
        return v.get();
    }
}
