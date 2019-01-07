package com.meijialife.dingdang;

import android.os.Environment;

import java.util.ArrayList;

import static android.os.Build.HOST;
import static com.meijialife.dingdang.alipay.ConsAli.ROOT_URL;

public class Constants {
    //folder path
    public static String FOLDER_NAME = "/jhj";
    public static String FOLDER_RECORD = FOLDER_NAME + "/Record";
    public static String FOLDER_TEMP = FOLDER_NAME + "/Temp";

    //*******************************
    // 主机地址
//    public static final String HOST1 = "http://tapp.jia-he-jia.com";
    public static final String HOST1 = "https://app.jia-he-jia.com";
//     基础接口
    public static final String ROOT_URL1 = HOST1 + "/jhj-app/app/";

    // 获取验证码接口
    public final static String URL_GET_SMS_TOKEN = ROOT_URL1 + "staff/login_sms_token.json";
    public final static String URL_GET_VOICE_TOKEN = ROOT_URL1 + "user/get_voice_token.json";
    // 登录
    public final static String URL_LOGIN = ROOT_URL1 + "staff/login.json";
    //发送位置
    public final static String URL_POST_USER_TRAIL = ROOT_URL1 + "user/post_user_trail.json";

    public static final String URL_POST_PUSH_BIND = ROOT_URL1 + "user/post_push_bind.json";

    public static final String URL_GET_ORDER_LIST = ROOT_URL1 + "staff/order/get_list.json";

    //请假列表
    public static final String URL_GET_LEAVE_LIST = ROOT_URL1 + "staff/leave_list.json";
    //申请请假
    public static final String URL_GET_DO_LEAVE = ROOT_URL1 + "staff/do_leave.json";
    //取消请假
    public static final String URL_GET_CANCEL_LEAVE = ROOT_URL1 + "staff/do_leave_cancel.json";
    //设置支付宝接口
    public static final String URL_SET_ALIPAY = ROOT_URL1 + "staff/set_alipay.json";
    //获取财务明细
    public static final String URL_GET_SALARY = ROOT_URL1 + "staff/get_salary.json";
    //获取日期
    public static final String URL_GET_SALARY_DATE = ROOT_URL1 + "staff/get_salary_month.json";

    //调整日期
    public static final String URL_POST_SERVICE_HOUR = ROOT_URL1 + "staff/order/post_service_hour.json";


    //获取用户信息列表接口
    public static final String URL_GET_USER_LIST = ROOT_URL1 + "user/get_user_list.json";
    //获取助理信息列表接口
    public static final String URL_GET_SEC_LIST = ROOT_URL1 + "user/user_get_org.json";

    /**
     * 用户详情接口
     **/
    public static final String URL_GET_USER_INFO = ROOT_URL1 + "staff/get_info.json";
    //申请提现
    public static final String URL_GET_TIXIAN = ROOT_URL1 + "staff/post_cash.json";

    //获得用户身份验证状态接口
    public static final String URL_GET_STAFF_AUTH = ROOT_URL1 + "staff/get_auth.json";

    //提现记录
    public static final String URL_GET_TIXIAN_CASH = ROOT_URL1 + "staff/get_cash.json";
    //交易记录
    public static final String URL_GET_DETAIL_CASH = ROOT_URL1 + "staff/pay/get_detail.json";
    //叮当大学首页状态
    public static final String URL_GET_UNIVERSITY_STATUS = ROOT_URL1 + "university/get_staff_test_status.json";
    //当日统计数
    public static final String URL_GET_TOTAL_TODAY = ROOT_URL1 + "staff/order/total_today.json";
    //消息已读接口
    public static final String URL_POST_TOTAL_TODAY = ROOT_URL1 + "msg/post_read.json";
    //开工按钮
    public static final String URL_GET_START_WORK = ROOT_URL1 + "staff/is_work.json";
    //签到按钮
    public static final String URL_POST_DAY_SIGN = ROOT_URL1 + "staff/day_sign.json";
    //获取消息列表
    public static final String URL_GET_MSG_LIST = ROOT_URL1 + "msg/get_list.json";
    //已读消息 
    public static final String URL_GET_MSG_READ = ROOT_URL1 + "msg/post_read.json";
    //获取欠款
    public static final String URL_GET_TOTAL_DEPT = ROOT_URL1 + "staff/get_total_dept.json";
    //支付欠款
    public static final String URL_GET_PAY_DEPT = ROOT_URL1 + "staff/pay/pay_dept.json";
    //开始服务
    public static final String URL_GET_START_ORDER_WORK = ROOT_URL1 + "staff/order/post_begin.json";
    //结束服务
    public static final String URL_GET_OVER_ORDER_WORK = ROOT_URL1 + "staff/order/post_done.json";
    //调整订单
    public static final String URL_GET_CHANGE_ORDER_WORK = ROOT_URL1 + "staff/order/post_am.json";
    //调整订单
    public static final String URL_GET_AUTH = ROOT_URL1 + "staff/auth.json";
    //加时
    public static final String URL_ADD_HOUR = ROOT_URL1 + "staff/order/post_overwork.json";
    //分享
    public static final String URL_GET_TO_SHARE = ROOT_URL1 + "staff/invite.json";
    //订单详情
    public static final String URL_GET_ORDER_DETAIL = ROOT_URL1 + "staff/order/get_detail.json";
    //用户评价
    public static final String URL_GET_USER_RATE_LIST = ROOT_URL1 + "staff/order/get_rates.json";

    //叮当大学
//    public static final String URL_GET_UNIVERSITY = HOST1 + "/am-h5/university/university-first.html";
    public static final String URL_GET_UNIVERSITY = HOST1 + "/am-h5/learn/learn-index.html";
    // 用户协议
    public final static String URL_WEB_AGREE = HOST1 + "/am-h5/jhj-agreement.html";


    public final static String URL_ABOUT_US = HOST1 + "/u/#!/user/aboutus.html";
    //*********************************
    //如果是环信的， 用户为  simi-user-1
    public static final String SERVICE_ID = "1";
    public static final String SERVICE_NUMBER = "400-169-1615";
    public static final String DESCRIPTOR = "com.umeng.share";

    public final static String AlipayHOST = "http://182.92.160.194";




    /**
     * app更新接口
     **/
    public static final String URL_GET_VERSION = HOST1 + "/d/version.xml";// 测试用，需更换

    /**
     * 订单状态
     **/
    public static final int ORDER_NOT_PAY = 1; // 未支付
    public static final int ORDER_HAS_PAY = 2; // 已支付


    /*** 网络返回状态码 ***/
    public static final int STATUS_SUCCESS = 0; // 成功
    public static final int STATUS_SERVER_ERROR = 100; // 服务器错误
    public static final int STATUS_PARAM_MISS = 101; // 缺失必选参数
    public static final int STATUS_PARAM_ILLEGA = 102; // 参数值非法
    public static final int STATUS_OTHER_ERROR = 999; // 其他错误

    /**
     * 微信分享APPid
     **/
    public static final String WX_APP_ID = "wx93aa45d30bf6cba3";


    /**
     * 常量
     **/
    public static String MAIN_PLUS_FLAG = "flag";
    public static final String MEETTING = "meeting";//会议
    public static final String MORNING = "morning";
    public static final String AFFAIR = "affair";
    public static final String NOTIFICATION = "notification";
    public static final String TRAVEL = "travel";//旅行


    public static String CARD_ADD_TREAVEL_CONTENT = "";
    public static String CARD_ADD_MEETING_CONTENT = "";
    public static String CARD_ADD_MORNING_CONTENT = "";
    public static String CARD_ADD_AFFAIR_CONTENT = "";
    public static String CARD_ADD_NOTIFICATION_CONTENT = "";
    public static String ADDRESS_NAME_CONTENT = "";
    public static String DISCOUNT_CARD_CONTENT = "";
    public static String REAL_PAY_CONTENT = "";


    /**
     * 本地临时文件根目录
     **/
    public static final String PATH_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Simi";


    public static final String URL_POST_SCORE_SHOP = ROOT_URL + "user/score_shop";

    //微信支付部分
    //微信预支付接口
    public static final String URL_ORDER_WEIXIN_PRE = ROOT_URL + "order/wx_pre.json";
    //微信查询接口
    public static final String URL_ORDER_WEIXIN_QUERY = ROOT_URL + "order/wx_order_query.json";
    //微信异步通知接口
    public static final String URL_ORDER_WEIXIN_NOTIFY = HOST + "/simi/wxpay-notify-ordercard.do";

    /**
     * 保存卡片创建的联系人
     **/
    public static ArrayList<String> finalContactList = new ArrayList<String>();
    /**
     * 分享跳转链接
     **/
    public static String SHARE_TARGET_URL = "http://123.57.173.36//simi-h5/show/card-share.html?card_id=";

    public static String SHARE_CUSTOMER_TARGET_URL = "http://51xingzheng.cn/h5-app-download.html";
    /**
     * 分享标题
     **/
    public static String SHARE_TITLE = "云行政，企业行政人力服务平台";
    /**
     * 分享内容
     **/
    public static String SHARE_CONTENT = "有来自好友的分享，点击查看详情。云行政，极大降低企业运行成本，极速提升企业工作效率，快来试试吧！";

    //checkdIndex标记用于切换=动态，好友，消息
    public static int checkedIndex = 0;
    //动态标题
    public static String FEED_TITLE = "";

    //服务接受的广播
    public static String LOCATION_RECEIVER_ACTION = "com.meijialife.service_destory";
    //服务接受的闹钟action
    public static String LOCATION_RECEIVER_CLOCK = "com.meijialife.clock";

}
