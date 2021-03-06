package com.wang;

import com.wang.bean.User;
import org.junit.Test;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.Configuration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

public class CsCache107Test {
    @Test
    public void test01() {
        /**
         *  参考  https://blog.csdn.net/wireless_com/article/details/79277272
         */
        // 获取缓存提供层对象
        CachingProvider cachingProvider = Caching.getCachingProvider();
        // 获取缓存管理层对象
        CacheManager manager = cachingProvider.getCacheManager();
        // 创建缓存实例对象
        Cache<String, User> cache = (Cache<String, User>) manager
                .<String, User, Configuration<String, User>>createCache("Test",
                        new MutableConfiguration<String, User>());
        String key = "leo";
        User user = new User();
        user.setName("leo");
        cache.put(key, user);
        System.out.println("Hello " + cache.get(key).getName());
    }
}
