package com.wang.serialport.model;

public class BillResult {
    /**
     * 商户号
     */
    public String mchntid;

    /**
     * 商户号
     */
    public String payMid;

    /**
     * 终端号
     */
    public String terminalid;
    /**
     * 商品订单号
     */
    public String outordernum;

    /**
     * 支付订单号
     */
    public String ordernum;
    /**
     * 合计支付金额
     */
    public String amountall;
    /**
     * 渠道优惠
     */
    public String chcddiscount;

    /**
     * 商户优惠
     */
    public String merdiscount;

    /**
     * 支付方式
     */
    public String paytype;

    /**
     * 应答码描述
     */
    public String respdetail;
    /**
     * 请求应答码
     */
    public String respcd;
}
