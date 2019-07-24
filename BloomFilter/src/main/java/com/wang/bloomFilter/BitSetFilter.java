package com.wang.bloomFilter;

import java.util.BitSet;

/**
 * 使用 bitset 可以将内存降低 8
 */
public class BitSetFilter {
    private int arraySize;
    private BitSet array;

    public BitSetFilter(int arraySize) {
        this.arraySize = arraySize;
        array = new BitSet(arraySize);
    }

    /**
     * 写入数据
     *
     * @param key
     */
    public void add(String key) {
        int first = hashCode1(key);
        int second = hashCode2(key);
        int third = hashCode3(key);
        array.set(first % arraySize);
        array.set(second % arraySize);
        array.set(third % arraySize);
    }


    /**
     * 判断数据是否存在
     *
     * @param key
     * @return
     */
    public boolean check(String key) {
        int first = hashCode1(key);
        int second = hashCode2(key);
        int third = hashCode3(key);
        if (!array.get(first % arraySize)) {
            return false;
        }
        if (!array.get(second % arraySize)) {
            return false;
        }
        if (!array.get(third % arraySize)) {
            return false;
        }
        return true;
    }


    /**
     * @param key
     * @return
     */
    private int hashCode1(String key) {
        int hash = 0;
        int i;
        for (i = 0; i < key.length(); ++i) {
            hash = 33 * hash + key.charAt(i);
        }
        return Math.abs(hash);
    }

    private int hashCode2(String data) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < data.length(); i++) {
            hash = (hash ^ data.charAt(i)) * p;
        }

        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return Math.abs(hash);
    }

    private int hashCode3(String key) {
        int hash, i;
        for (hash = 0, i = 0; i < key.length(); ++i) {
            hash += key.charAt(i);
            hash += (hash << 10);
            hash ^= (hash >> 6);

        }

        hash += (hash << 3);
        hash ^= (hash >> 11);
        hash += (hash << 15);
        return Math.abs(hash);
    }

}
