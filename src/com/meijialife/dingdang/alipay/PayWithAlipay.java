package com.meijialife.dingdang.alipay;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.meijialife.dingdang.utils.LogOut;

public class PayWithAlipay {

    private static final String TAG = "PayWithAlipay";
    private static final int RQF_PAY = 1;// 支付宝返回标识
    private static final int RQF_LOGIN = 2;

    private Activity activity;
    private Context context;
    private OnAlipayCallback callback;
    private String mobile;
    private int payType;
    private String price;
    private String orderNo;
    private String notifyUrl;

    /**
     * 
     * @param activity
     * @param callback
     *            回调
     * @param payType
     *            支付类型 1:会员充值 2：秘书服务支付 3:订单在线支付
     * @param price
     *            金额
     * @param orderNo
     *            订单号（提交订单时的订单号）
     */
    public PayWithAlipay(Activity activity, Context context, OnAlipayCallback callback, String notifyUrl,String mobile, int payType, String price, String orderNo) {
        this.activity = activity;
        this.context = context;
        this.callback = callback;
        this.mobile = mobile;
        this.payType = payType;
        this.price = price;
        this.orderNo = orderNo;
        this.notifyUrl = notifyUrl;
    }

    public void pay() {
        initProduct();
    }

    /**
     * 初始化商品信息
     */
    private void initProduct() {
        sProduct = new Product();
        sProduct.price = price;

        switch (payType) {
        case ConsAli.PAY_TO_MEMBER: // 欠款数据
            sProduct.subject = "支付欠款";
            sProduct.body = "" + mobile;
            break;
//        case ConsAli.PAY_TO_MS_CARD:// 管家卡支付
//            notifyUrl = ConsAli.NOTIFY_URL_ORDER;
////            notifyUrl = ConsAli.NOTIFY_URL_GJ_CARD;
//            sProduct.subject = "服务费支付";//秘书服务支付
//            sProduct.body = "" + mobile;
//            break;
//        case ConsAli.PAY_TO_ORDER: // 订单在线支付
//            notifyUrl = ConsAli.NOTIFY_URL_ORDER;
//            sProduct.subject = "私密订单支付";
//            sProduct.body = "" + mobile;
//            break;

        default:
            break;
        }

        toPay();
    }

    /**
     * 支付
     */
    private void toPay() {
        try {
            LogOut.i("ExternalPartner", "onItemClick");
            String info = getNewOrderInfo();
            String sign = Rsa.sign(info, Keys.PRIVATE);
            sign = URLEncoder.encode(sign);
            info += "&sign=\"" + sign + "\"&" + getSignType();
            // LogOut.i("ExternalPartner", "start pay");
            // start the pay.
            LogOut.i(TAG, "info = " + info);

            final String orderInfo = info;

            Runnable payRunnable = new Runnable() {

                @Override
                public void run() {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask(activity);
                    // 调用支付接口，获取支付结果
                    String result = alipay.pay(orderInfo);

                    Message msg = new Message();
                    msg.what = RQF_PAY;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };
            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();

        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(activity, "调用支付宝错误", Toast.LENGTH_SHORT).show();
        }

    }

    private String getNewOrderInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("partner=\"");
        sb.append(Keys.DEFAULT_PARTNER);
        sb.append("\"&out_trade_no=\"");
        // sb.append(getOutTradeNo());
        sb.append(orderNo);
        sb.append("\"&subject=\"");
        sb.append(sProduct.subject);
        sb.append("\"&body=\"");
        sb.append(sProduct.body);
        sb.append("\"&total_fee=\"");
        sb.append(sProduct.price);
        sb.append("\"&notify_url=\"");

        // 网址需要做URL编码
        // sb.append(URLEncoder.encode("http://notify.java.jpxx.org/index.jsp"));
        sb.append(URLEncoder.encode(notifyUrl));/*
                                                 * + "?C_CONTRACT_CODE=" + orderNo
                                                 */
        sb.append("\"&service=\"mobile.securitypay.pay");
        sb.append("\"&_input_charset=\"UTF-8");
        sb.append("\"&return_url=\"");
        sb.append(URLEncoder.encode("http://m.alipay.com"));
        sb.append("\"&payment_type=\"1");
        sb.append("\"&seller_id=\"");
        sb.append(Keys.DEFAULT_SELLER);

        // 如果show_url值为空，可不传
        // sb.append("\"&show_url=\"");
        sb.append("\"&it_b_pay=\"1m");
        sb.append("\"");

        return new String(sb);
    }

    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
        Date date = new Date();
        String key = format.format(date);

        java.util.Random r = new java.util.Random();
        key += r.nextInt();
        key = key.substring(0, 15);
        LogOut.i(TAG, "outTradeNo: " + key);
        return key;
    }

    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Result result = new Result((String) msg.obj);
            Log.d(TAG, "支付宝支付结果: " + msg.obj.toString());
            String resultStatus;

            switch (msg.what) {
            case RQF_PAY:
                resultStatus = result.getResultStatus();
                if (resultStatus.equals("9000")) {
                    result.parseResult();
                    String tradeNo = result.getTradeNo();
                    tradeNo = tradeNo.replace("\"", "");
                    callback.onAlipayCallback(activity, context, true, tradeNo);
                } else if (Result.sResultStatus.containsKey(resultStatus)) {
                    String res = Result.sResultStatus.get(resultStatus);
                    callback.onAlipayCallback(activity, context, false, res);
                } else {
                    callback.onAlipayCallback(activity, context, false, result.getResult());
                }
                break;
            case RQF_LOGIN: {
                callback.onAlipayCallback(activity, context, false, result.getResult());
            }
                break;
            default:
                break;
            }
        };
    };

    public static class Product {
        public String subject;
        public String body;
        public String price;
    }

    public static Product sProduct;
}
