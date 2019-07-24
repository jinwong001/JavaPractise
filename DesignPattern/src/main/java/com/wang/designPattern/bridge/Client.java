package com.wang.designPattern.bridge;

public class Client {
    /**
     * 使用桥接模式，将两个纬度就像桥梁一样被链接起来，体现了松耦合的特性
     *
     * 桥接优点：
     * 1，分离抽象和实现部分：把手机、内存抽象出来，实现与之分离
     * 2，松耦合： 将两个纬度分开
     * 3，单一职责
     *
     *
     * @param args
     */

    public static void main(String[] args) {
        Phone huawei=new Huawei();
        huawei.setPhoneMemory(new Memory8G());
        huawei.buyPhone();

        Phone mi=new Mi();
        mi.setPhoneMemory(new Memory6G());
        mi.buyPhone();
    }
}
