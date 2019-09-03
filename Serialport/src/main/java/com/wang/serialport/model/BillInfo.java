package com.wang.serialport.model;


import java.util.List;

/**
 * Created by wanny-n1 on 2018/5/16.
 */

public class BillInfo {
    /**
     * 商品订单号
     */
    public String outordernum;

    /**
     * 商户订单号，
     * 不能超过24位，
     * 微信要求 支付订单号不能超过32位
     */
    public String merordernum;

    /**
     * 商品品种类
     */
    public String countall;

    /**
     * 商品总量
     */
    public String quantityall;

    /***
     * 商品
     */
    public String goodslist;

    /**
     * 优惠信息
     */
    public String discountslist;
    /**
     * 合计支付金额,
     */
    public String amountall;

    /**
     * 合计优惠金额
     */
    public String dscall;

    /**
     * 合计金额
     */
    public String amountraw;

    public List<Discount> discountList;
    public List<Goods> goodsList;
}
