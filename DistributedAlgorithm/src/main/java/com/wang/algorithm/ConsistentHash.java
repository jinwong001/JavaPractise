package com.wang.algorithm;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 一致性hash 算法实现
 *
 * @param <T>
 * @see <href>  https://www.cnblogs.com/moonandstar08/p/5405991.html</href>
 */
public class ConsistentHash<T> {

    HashFunc hashFunc;
    private final int numberofReplicas;
    private final SortedMap<Long, T> cicle = new TreeMap<>();

    public ConsistentHash(HashFunc hashFunc, int numberofReplicas, Collection<T> nodes) {
        this.numberofReplicas = numberofReplicas;
        this.hashFunc = hashFunc;
        for (T node : nodes) {
            add(node);
        }
    }

    public ConsistentHash(int numberofReplicas, Collection<T> nodes) {
        this.hashFunc = new HashFunc() {
            @Override
            public Long hash(Object key) {
                return fnv1HashingAlg(key.toString());
            }
        };
        this.numberofReplicas = numberofReplicas;
        for (T node : nodes) {
            add(node);
        }

    }

    /**
     *
     * 增加服务器节点
     *
     * @param node
     */
    public void add(T node) {
        // 每台节点，增加n个虚拟节点
        for (int i = 0; i < numberofReplicas; i++) {
            cicle.put(hashFunc.hash(node.toString() + i), node);
        }
    }

    /**
     * 移除服务器节点
     *
     * @param node
     */
    public void remove(T node) {
        for (int i = 0; i < numberofReplicas; i++) {
            cicle.remove(hashFunc.hash(node.toString() + i), node);
        }
    }

    /**
     * 获取最近的顺时针节点
     *
     * @param key
     * @return
     */
    public T get(Object key) {
        if (cicle.isEmpty()) {
            return null;
        }

        long hash = hashFunc.hash(key);
        if (!cicle.containsKey(hash)) {
            // 返回 key 大于或等于hash的余下的 sortMap
            SortedMap<Long, T> tailMap = cicle.tailMap(hash);
            hash = tailMap.isEmpty() ? cicle.firstKey() : tailMap.firstKey();
        }
        return cicle.get(hash);
    }

    /**
     * 使用FNV1hash算法
     *
     * @param key
     * @return
     */
    private static long fnv1HashingAlg(String key) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < key.length(); i++)
            hash = (hash ^ key.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return hash;
    }


    public interface HashFunc {
        Long hash(Object key);
    }

}
