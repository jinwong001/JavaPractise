package com.wang.algorithm;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class ConsistentHashTest {


    @Test
    public void testHash() {
        //  test   122445   101010    ef ob  ef ob

        ConsistentHash<String> ch = new ConsistentHash(5, Arrays.asList("a", "b", "c"));

        // 初始情况
        dumpObjectNodeMap("初始情况", ch, 0, 65536);

        // 删除物理节点
        ch.remove("a");
        dumpObjectNodeMap("删除物理节点", ch, 0, 65536);

        // 添加物理节点
        for (int i = 0; i < 10; i++) {
            ch.add(String.valueOf(i));
        }


        dumpObjectNodeMap("添加物理节点", ch, 0, 65536);
    }


    // 统计对象与节点的映射关系
    public void dumpObjectNodeMap(String label, ConsistentHash<String> hash, int objectMin, int objectMax) {
        // 统计
        Map<String, Integer> objectNodeMap = new TreeMap<>(); // IP => COUNT
        for (int object = objectMin; object <= objectMax; ++object) {
            String nodeIp = hash.get(object);
            Integer count = objectNodeMap.get(nodeIp);
            objectNodeMap.put(nodeIp, (count == null ? 0 : count + 1));
        }

        // 打印
        double totalCount = objectMax - objectMin + 1;
        System.out.println("======== " + label + " ========");
        for (Map.Entry<String, Integer> entry : objectNodeMap.entrySet()) {
            long percent = (int) (100 * entry.getValue() / totalCount);
            System.out.println("IP=" + entry.getKey() + ": RATE=" + percent + "%");
        }
    }
}
