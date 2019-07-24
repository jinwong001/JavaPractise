package com.wang.designPattern.bridge;

public class Mi extends Phone {
    @Override
    public void buyPhone() {
        //memory 是继承f 父类调用的 setMemory中方法
        phoneMemory.addMemory();
        System.out.println("购买小米手机");
    }
}
