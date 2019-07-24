package com.wang.designPattern.bridge;

public class Memory6G implements Memory {
    @Override
    public void addMemory() {
        System.out.println("手机安装了6G内存");
    }
}
