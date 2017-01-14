package com.meijialife.dingdang.alipay;


public class ConsAli {

    /** 根目录 **/
    public static final String ROOT_URL = "http://123.57.173.36/simi/pay/";
//    public static final String ROOT_URL = Constants.HOST + "/simi/pay/";

    /** 回调URL-会员充值 **/
    public static final String NOTIFY_URL_MEMBER = ROOT_URL + "notify_alipay_ordercard.jsp";
    /** 回调URL-秘书服务支付 **/
    public static final String NOTIFY_URL_GJ_CARD = ROOT_URL + "notify_alipay_ordersenior.jsp";
    /** 回调URL-订单在线支付 **/
    public static final String NOTIFY_URL_ORDER = ROOT_URL + "notify_alipay_order.jsp";

    /** 支付类型 **/
    public static final int PAY_TO_MEMBER = 1; // 欠款数据
    public static final int PAY_TO_MS_CARD = 2; // 秘书服务支付
    public static final int PAY_TO_ORDER = 3; // 订单在线支付
}
