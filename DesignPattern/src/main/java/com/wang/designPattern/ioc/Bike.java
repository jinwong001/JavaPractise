package com.wang.designPattern.ioc;

public class Bike implements Driveable {

    @Override
    public void drive() {
        System.out.println("Bike drive.");
    }
}
