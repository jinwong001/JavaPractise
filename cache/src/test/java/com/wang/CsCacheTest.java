package com.wang;

import com.wang.bean.User;
import com.wang.store.impl.BasicDataStore;
import com.wang.store.impl.LRUDataStore;
import com.wang.store.impl.WeakValueDataStore;
import org.junit.Test;

public class CsCacheTest {


    @Test
    public void TestBasicData() {
        CsCache<String, String> cache = new CsCache<String, String>(new BasicDataStore<>());
        cache.put("key", "hi");
        System.out.println("key:" + cache.get("key"));
    }

    @Test
    public void TestWeakValue() {
        CsCache<String, User> cache = new CsCache<String, User>(new WeakValueDataStore<String, User>());
        String key = "leo";
        User user = new User();
        user.setName("leo");
        cache.put(key, user);
        user = null;
        System.out.println("Hello " + cache.get(key).getName());
        System.gc();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Hello " + cache.get(key));
    }


    @Test
    public void TestLRU() {
        CsCache<String, User> cache = new CsCache<String, User>(new LRUDataStore<String, User>(2));
        String key = "leo";
        User user = new User();
        user.setName("leo");

        String key1 = "liu";
        User user1 = new User();
        user1.setName("liu");

        String key2 = "robin";
        User user2 = new User();
        user2.setName("robin");

        cache.put(key, user);
        cache.put(key1, user1);
        cache.get(key);
        cache.put(key2, user2);

        if (cache.get(key) != null) {
            System.out.println("Hello " + cache.get(key).getName());
        }
        if (cache.get(key1) != null) {
            System.out.println("Hello " + cache.get(key1).getName());
        }
        if (cache.get(key2) != null) {
            System.out.println("Hello " + cache.get(key2).getName());
        }
    }
}
