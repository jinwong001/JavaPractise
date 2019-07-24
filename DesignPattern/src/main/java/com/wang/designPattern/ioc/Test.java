package com.wang.designPattern.ioc;

public class Test {
    public static void main(String[] args) {

        Driveable bike = new Bike();
        Driveable car = new Car();
        Person person = new Person(bike);
        person.chumen();

        Person person2 = new Person();
        person.setDriveable(car);
        person.chumen();

    }
}
