package com.wang.bloomFilter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

/**
 * <@see> https://mp.weixin.qq.com/s/045gGxwPWIsdikRELgG88A</@see>
 * <@see> https://blog.csdn.net/xinzhongtianxia/article/details/81294922</@see>
 *
 * 布隆过滤器原理：
 * 初始化一个二进制数组，同时初始值全为0，对数据进行多次hash运算，各个hashcode取模后,将对应的二进制位置为1；
 * 当一个新数据同样进行hash运算取模，当定位到二进制数组对应的数位都为0，则该数据之前不存在，否则可能存在
 *
 * 因为存在数据hash运算后hashcode 相同，另外二进制数组长度太小也很影响错误率。
 *
 *
 *
 * 布隆过滤器特点：
 * 1. 指要返回数据不存在，则肯定不存在
 * 2. 返回数据存在，但只可能是大概率存在
 * 3. 同时不能清除其中的数据
 *
 */
public class BloomTest {


    @Test
    public void hashMapTest() {

        long start=System.currentTimeMillis();
        int size=100;
        //int size=1000000;
        //int size=100000000;
        Set<Integer> hashset=new HashSet<>(size);

        for(int i=0;i<size;i++){
            hashset.add(i);
        }

        Assert.assertTrue(hashset.contains(1));
        Assert.assertTrue(hashset.contains(2));
        Assert.assertTrue(hashset.contains(3));

        long end=System.currentTimeMillis();

        System.out.println("执行时间："+(end-start));
    }

    @Test
    public void bloomFilterTest(){
        long start=System.currentTimeMillis();
        //int size=100;
        //int size=1000000;
        int size=100000000;
        BloomFilters bloomFilters=new BloomFilters(size);

        for(int i=0;i<size;i++){
            bloomFilters.add(String.valueOf(i));
        }

        Assert.assertTrue(bloomFilters.check(String.valueOf(1)));
        Assert.assertTrue(bloomFilters.check(String.valueOf(2)));
        Assert.assertTrue(bloomFilters.check(String.valueOf(3)));
       // Assert.assertTrue(bloomFilters.check(String.valueOf(400000000)));

        long end=System.currentTimeMillis();

        System.out.println("执行时间："+(end-start));
    }

    @Test
    public void bloomBitFilterTest(){
        long start=System.currentTimeMillis();
        //int size=100;
        //int size=1000000;
        int size=100000000;
        BitSetFilter bloomFilters=new BitSetFilter(size);

        for(int i=0;i<size;i++){
            bloomFilters.add(String.valueOf(i));
        }

        Assert.assertTrue(bloomFilters.check(String.valueOf(1)));
        Assert.assertTrue(bloomFilters.check(String.valueOf(2)));
        Assert.assertTrue(bloomFilters.check(String.valueOf(3)));
        // Assert.assertTrue(bloomFilters.check(String.valueOf(400000000)));

        long end=System.currentTimeMillis();

        System.out.println("执行时间："+(end-start));
    }


    @Test
    public void guavaTest(){
        long start=System.currentTimeMillis();
        //int size=100;
        //int size=1000000;
        int size=100000000;
        /**
         *
         * 0.01 为可接受的误报率
         */
        BloomFilter<String> bloomFilters=BloomFilter.create(
                Funnels.stringFunnel(Charset.defaultCharset()),
                size,
                0.01
        );

        for(int i=0;i<size;i++){
            bloomFilters.put(String.valueOf(i));
        }

        Assert.assertTrue(bloomFilters.mightContain(String.valueOf(1)));
        Assert.assertTrue(bloomFilters.mightContain(String.valueOf(2)));
        Assert.assertTrue(bloomFilters.mightContain(String.valueOf(3)));
        //Assert.assertTrue(bloomFilters.mightContain(String.valueOf(400000000)));

        long end=System.currentTimeMillis();

        System.out.println("执行时间："+(end-start));
    }




}
