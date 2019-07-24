package com.wang.designPattern.ioc;


/**
 * @title 依赖倒置(DIP)，控制反转（IOC)，依赖注入（DI）
 * @see <href>https://blog.csdn.net/briblue/article/details/75093382</href>
 * <p>
 * 1，依赖倒置是面向对象开发领域的设计原则，倡导上层模块不依赖底层模版，抽象不依赖细节
 * 2，依赖反转是遵循依赖倒置原则提出的设计模式，引入IOC 容器概念
 * 3，依赖注入是为了实现依赖反转的一种手段之一
 */
public class Person implements DepedencySetter {
    private Bike mBike;
    private Car mCar;

    private Driveable mDriveable;


    public Person() {
        //mBike = new Bike();
        mCar = new Car();
    }

    public void chumen() {
        System.out.println("出门了");
        //mBike.drive();


        // chumen() 这个方法依赖于 Driveable 接口的抽象, 即依赖倒置
        //  通过对接口编程，依赖接口，完成依赖倒置
        // 依赖倒置实质上是面向接口编程的体现。
        mDriveable.drive();
    }

    /**
     * person 将内部依赖的权利移交给外部调用，比如Test中main()
     * <p>
     * 控制反转（Inverion of Control,Ioc)，反转了上层模块对于底层模块的依赖控制。
     * <p>
     * 将具体的依赖通过外部创建，然后在合适的时候注入给对象。
     * 实现依赖注入有三种方法：
     * 1. 构造函数注入
     * 2. setter 方式注入
     * 3. 接口注入  （配置文件依赖接口，实现特定接口，也是依赖注入，完成控制反转）
     */
    public Person(Driveable driveable) {
        this.mDriveable = driveable;
    }

    public void setDriveable(Driveable driveable) {
        this.mDriveable = driveable;
    }


    @Override
    public void set(Driveable driveable) {
        this.mDriveable = driveable;
    }


    /**
     * 是实例化抽象的地方，就是Ioc 容器
     */
    public void chooseDriveable(int money) {
        if (money > 1000) {
            this.mDriveable = new Car();
        } else {
            this.mDriveable = new Bike();
        }

    }
}



