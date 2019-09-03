package com.wang.serialport.model;

/**
 * Created by wanny-n1 on 2018/5/28.
 */

public class Discount {
    /**
     * 优惠id
     */
    public String discountsid;
    /**
     * 优惠方式
     */
    public String dsctype;
    /**
     * 优惠金额
     */
    public String singlecall;

    public Discount(String dsctype, String singlecall) {
        this.dsctype = dsctype;
        this.singlecall = singlecall;
    }
}
