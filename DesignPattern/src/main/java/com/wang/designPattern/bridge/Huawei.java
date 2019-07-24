package com.wang.designPattern.bridge;

public class Huawei extends Phone {
    @Override
    public void buyPhone() {
        phoneMemory.addMemory();
        System.out.println("购买华为手机");
    }
}
