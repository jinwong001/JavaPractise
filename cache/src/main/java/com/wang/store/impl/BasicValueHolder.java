package com.wang.store.impl;

import com.wang.store.ValueHolder;

public class BasicValueHolder<V> implements ValueHolder<V> {

    private final V refValue;

    public BasicValueHolder(V refValue) {
        this.refValue = refValue;
    }

    @Override
    public V value() {
        return refValue;
    }
}
