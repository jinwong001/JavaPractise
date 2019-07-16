package com.wang.memcache;

import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MainTest {
    public static void main(String[] args) {
        try {
            // 建立链接
            MemcachedClient mcc = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
            System.out.println("connection to server successful");

            // 存储数据
            OperationFuture fo = mcc.set("test", 60, "free edu");

            System.out.println("set status：" + fo.getStatus());
            System.out.println("test value in cache：" + mcc.get("test"));


            fo = mcc.add("test", 90, "mm");
            System.out.println("set status：" + fo.getStatus());
            System.out.println("test value in cache：" + mcc.get("test"));

            fo = mcc.add("test1", 90, "mm");
            System.out.println("set status：" + fo.getStatus());
            System.out.println("test value in cache：" + mcc.get("test1"));

            fo = mcc.replace("test1", 90, "replace");
            System.out.println("set status：" + fo.getStatus());
            System.out.println("test value in cache：" + mcc.get("test1"));

            fo = mcc.replace("test2", 90, "replace");
            System.out.println("set status：" + fo.getStatus());
            System.out.println("test value in cache：" + mcc.get("test2"));


            fo = mcc.append("test1", "append");
            System.out.println("set status：" + fo.getStatus());
            System.out.println("test value in cache：" + mcc.get("test1"));


            fo = mcc.prepend("test1", "rp");
            System.out.println("set status：" + fo.getStatus());
            System.out.println("test value in cache：" + mcc.get("test1"));


            CASValue casValue = mcc.gets("test1");
            System.out.println("CAS token - " + casValue);
            CASResponse resp = mcc.cas("test1", casValue.getCas(), 900, "new cas");
            System.out.println("CAS Response - " + resp);

            System.out.println("runoob value in cache - " + mcc.get("test1"));

            // 关闭memcache 连接
            mcc.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
