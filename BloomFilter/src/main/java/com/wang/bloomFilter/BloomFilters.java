package com.wang.bloomFilter;

public class BloomFilters {

    private int arraySize;
    private int[] array;

    public BloomFilters(int arraySize) {
        this.arraySize = arraySize;
        array = new int[arraySize];
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
        array[first % arraySize] = 1;
        array[second % arraySize] = 1;
        array[third % arraySize] = 1;
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
        if (array[first % arraySize] == 0) {
            return false;
        }
        if (array[second % arraySize] == 0) {
            return false;
        }
        if (array[third % arraySize] == 0) {
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
