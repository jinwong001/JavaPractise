package com.wang.store.impl;

import com.wang.store.ValueHolder;

import java.util.Map;

public class LRUEntry<K, V extends ValueHolder<?>> implements Map.Entry<K, ValueHolder<?>> {
    final K key;
    ValueHolder<?> v;
    LRUEntry<K, ValueHolder<?>> pre;
    LRUEntry<K, ValueHolder<?>> next;

    public LRUEntry(K key, V v) {
        this.key = key;
        this.v = v;
    }

    public LRUEntry<K, ValueHolder<?>> getPre() {
        return pre;
    }

    public LRUEntry<K, ValueHolder<?>> getNext() {
        return next;
    }

    public void setPre(LRUEntry<K, ValueHolder<?>> pre) {
        this.pre = pre;
    }

    public void setNext(LRUEntry<K, ValueHolder<?>> next) {
        this.next = next;
    }


    @Override
    public K getKey() {
        return key;
    }

    @Override
    public ValueHolder<?> getValue() {
        return this.v;
    }

    @Override
    public ValueHolder<?> setValue(ValueHolder<?> value) {
        ValueHolder<?> oldValue = this.v;
        this.v = value;
        return oldValue;
    }
}
