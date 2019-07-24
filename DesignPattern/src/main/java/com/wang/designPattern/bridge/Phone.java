package com.wang.designPattern.bridge;

public abstract class Phone {
    public Memory phoneMemory;

    public void setPhoneMemory(Memory phoneMemory) {
        this.phoneMemory = phoneMemory;
    }

    public abstract void buyPhone();

}
