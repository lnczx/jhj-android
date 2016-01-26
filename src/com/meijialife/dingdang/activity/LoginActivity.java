package com.meijialife.dingdang.activity;

import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.meijialife.dingdang.BaseActivity;
import com.meijialife.dingdang.Constants;
import com.meijialife.dingdang.MainActivity;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.activity.BaiduLocationActivity.OnLocationListener;
import com.meijialife.dingdang.bean.UserIndexData;
import com.meijialife.dingdang.utils.BasicToolUtil;
import com.meijialife.dingdang.utils.LogOut;
import com.meijialife.dingdang.utils.NetworkUtils;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.StringUtils;
import com.meijialife.dingdang.utils.UIUtils;

public class LoginActivity extends BaseActivity implements OnClickListener {

    private EditText et_user, et_pwd;
    private TextView login_btn;

    private TextView login_getcode;

    private TextView tv_user_tip;

    private TextView login_not_get_captcha;

    private LinearLayout login_nocode_tip, login_voice_tip;

    private Handler mHandler;
    private static final String SMS_TOKEN = "sms_toke";
    private static final String VOICE_TOKEN = "voice_toke";
    private static String FLAG = SMS_TOKEN;
    // private User user;// 登陆成功后的用户数据
    // private UserInfo userInfo;// 用户详情数据

    private TextView tv_nocode_tip;

    private TextView tv_number, tv_voice, tv_location;
    public static String clientid;
    private String lat;// 纬度
    private String lng;// 经度
    private String addStr;// 经度
    private String city;// 经度
    private String mobile;
    String staff_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);

        staff_id = SpFileUtil.getString(LoginActivity.this, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        int auth_status = SpFileUtil.getInt(LoginActivity.this, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_USER_AUTH_STATUS, 0);
        if (StringUtils.isNotEmpty(staff_id) && auth_status == 1) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            LoginActivity.this.finish();
        } else if (StringUtils.isNotEmpty(staff_id) && auth_status == 0) {
            startActivity(new Intent(LoginActivity.this, AuthStatusActivity.class));
            LoginActivity.this.finish();
        }
        initView();
        FLAG = SMS_TOKEN;
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                case 0:
                    if (msg.obj.toString().equalsIgnoreCase("0")) {
                        login_getcode.setText("获取验证码");
                        login_getcode.setClickable(true);
                        login_getcode.setFocusable(true);
                        login_getcode.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_login_getcode));
                    } else {
                        login_getcode.setText("重发验证码" + msg.obj + "s");
                    }
                    break;
                }
            }
        };
    }

    private void initView() {

        setTitleName("绑定手机登录");
        requestBackBtn();

        et_user = (EditText) findViewById(R.id.login_user_name);
        et_pwd = (EditText) findViewById(R.id.login_password);

        login_getcode = (TextView) findViewById(R.id.login_getcode);
        login_not_get_captcha = (TextView) findViewById(R.id.login_not_get_captcha);
        login_btn = (TextView) findViewById(R.id.login_btn);
        tv_user_tip = (TextView) findViewById(R.id.tv_user_tip);
        tv_nocode_tip = (TextView) findViewById(R.id.tv_nocode_tip);
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_voice = (TextView) findViewById(R.id.tv_voice);
        tv_location = (TextView) findViewById(R.id.tv_location);
        login_nocode_tip = (LinearLayout) findViewById(R.id.login_nocode_tip);
        login_voice_tip = (LinearLayout) findViewById(R.id.login_voice_tip);

        login_btn.setOnClickListener(this);
        tv_number.setOnClickListener(this);
        tv_voice.setOnClickListener(this);
        login_getcode.setOnClickListener(this);
        login_not_get_captcha.setOnClickListener(this);
        tv_user_tip.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        case R.id.login_getcode:// 获取验证码
            String reg_phone = et_user.getText().toString().trim();
            if (BasicToolUtil.checkMobileNum(this, reg_phone)) {
                getSmsToken(reg_phone, FLAG);
            }

            break;
        case R.id.login_btn: // 登录
            login_nomal();
            break;
        case R.id.login_not_get_captcha:// 没有收到
            String styledText = "<font color='blue'>" + Constants.SERVICE_NUMBER + "</font>";
            Spanned number = Html.fromHtml("<u>" + styledText + "</u>");
            tv_number.setText(number, TextView.BufferType.SPANNABLE);
            String text = getResources().getString(R.string.login_no_code_tip);
            tv_nocode_tip.setText(text, TextView.BufferType.SPANNABLE);
            if (login_nocode_tip.getVisibility() == View.INVISIBLE) {
                login_nocode_tip.setVisibility(View.VISIBLE);
            }

            break;
        case R.id.tv_user_tip:// 用户协议
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("url", Constants.URL_WEB_AGREE);
            intent.putExtra("title", "用户使用协议");
            startActivity(intent);

            break;
        case R.id.tv_voice:
            FLAG = VOICE_TOKEN;
            login_voice_tip.setVisibility(View.GONE);
            login_nocode_tip.setVisibility(View.VISIBLE);

            String phone = et_user.getText().toString().trim();
            if (BasicToolUtil.checkMobileNum(this, phone)) {
                getSmsToken(phone, FLAG);
            }

            break;
        case R.id.tv_number:

            Intent intent2 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "4001691615"));
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent2);

            break;
        default:
            break;
        }
    }

    /**
     * 普通登陆
     * 
     */
    private void login_nomal() {
        final String mobile = et_user.getText().toString().trim();
        String sms_token = et_pwd.getText().toString().trim();

        if (StringUtils.isEmpty(mobile)) {
            UIUtils.showToast(this, "手机号不能为空");
            return;
        }
        if (StringUtils.isEmpty(sms_token)) {
            UIUtils.showToast(this, "验证码不能为空");
            return;
        }

        showDialog();

        Map<String, String> map = new HashMap<String, String>();
        map.put("mobile", mobile);
        map.put("sms_token", sms_token);
        // map.put("device_type", "android");
        // map.put("login_from", "0");
        AjaxParams param = new AjaxParams(map);
        new FinalHttp().post(Constants.URL_LOGIN, param, new AjaxCallBack<Object>() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                LogOut.debug("错误码：" + errorNo);
                dismissDialog();
                Toast.makeText(getApplicationContext(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                dismissDialog();
                LogOut.debug("成功:" + t.toString());
                UIUtils.showTestToastLong(LoginActivity.this, "登陆 返回数据：" + t.toString());
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            loginSuccess(data);
                        } else if (status == Constants.STATUS_SERVER_ERROR) { // 服务器错误
                            Toast.makeText(LoginActivity.this, getString(R.string.servers_error), Toast.LENGTH_SHORT).show();
                        } else if (status == Constants.STATUS_PARAM_MISS) { // 缺失必选参数
                            Toast.makeText(LoginActivity.this, getString(R.string.param_missing), Toast.LENGTH_SHORT).show();
                        } else if (status == Constants.STATUS_PARAM_ILLEGA) { // 参数值非法
                            Toast.makeText(LoginActivity.this, getString(R.string.param_illegal), Toast.LENGTH_SHORT).show();
                        } else if (status == Constants.STATUS_OTHER_ERROR) { // 999其他错误
                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.servers_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    UIUtils.showToast(LoginActivity.this, "登录失败,请稍后重试");
                }

            }
        });

    }

    /**
     * 获取手机验证码
     */
    private void getSmsToken(String mobile, String flag) {
        showDialog();
        String url = "";
        if (flag.equals(SMS_TOKEN)) {
            url = Constants.URL_GET_SMS_TOKEN;
        } else if (flag.equals(VOICE_TOKEN)) {
            url = Constants.URL_GET_VOICE_TOKEN;
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("mobile", mobile);
        map.put("sms_type", "2");
        AjaxParams param = new AjaxParams(map);
        new FinalHttp().get(url, param, new AjaxCallBack<Object>() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                LogOut.debug("错误码：" + errorNo);
                dismissDialog();
                Toast.makeText(getApplicationContext(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                dismissDialog();
                LogOut.debug("成功:" + t.toString());
                UIUtils.showTestToastLong(LoginActivity.this, "获取验证码 返回数据：" + t.toString());

                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            // login_not_get_captcha.setVisibility(View.VISIBLE);
                            login_getcode.setClickable(false);
                            login_getcode.setFocusable(false);
                            login_getcode.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_login_getcode_over));

                            UIUtils.showToast(getApplicationContext(), "验证码获取成功");

                            new Thread() {
                                int a = 60;

                                public void run() {
                                    while (a > 0) {
                                        try {
                                            sleep(1000);
                                            a--;
                                            Message msg = mHandler.obtainMessage(0, a);
                                            mHandler.sendMessage(msg);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                            }.start();

                        } else if (status == Constants.STATUS_SERVER_ERROR) { // 服务器错误
                            Toast.makeText(LoginActivity.this, getString(R.string.servers_error), Toast.LENGTH_SHORT).show();
                        } else if (status == Constants.STATUS_PARAM_MISS) { // 缺失必选参数
                            Toast.makeText(LoginActivity.this, getString(R.string.param_missing), Toast.LENGTH_SHORT).show();
                        } else if (status == Constants.STATUS_PARAM_ILLEGA) { // 参数值非法
                            Toast.makeText(LoginActivity.this, getString(R.string.param_illegal), Toast.LENGTH_SHORT).show();
                        } else if (status == Constants.STATUS_OTHER_ERROR) { // 999其他错误
                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.servers_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, getString(R.string.servers_error), Toast.LENGTH_SHORT).show();
                }

            }
        });

    };

    /**
     * 后台接口登陆成功，
     */
    private void loginSuccess(String data) {
        // Gson gson = new Gson();
        // user = gson.fromJson(data, User.class);
        // DBHelper.updateUser(LoginActivity.this, user);

        try {
            JSONObject jsonObject = new JSONObject(data);
            staff_id = jsonObject.optString("staff_id");
            mobile = jsonObject.optString("mobile");
            // auth_status = jsonObject.optInt("auth_status");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SpFileUtil.saveString(LoginActivity.this, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, staff_id);
        SpFileUtil.saveString(LoginActivity.this, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_USER_MOBILE, mobile);
        // SpFileUtil.saveInt(LoginActivity.this, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_USER_AUTH_STATUS, auth_status);

        // 登录成功
        Toast.makeText(getApplicationContext(), "登录成功！", Toast.LENGTH_SHORT).show();
        getUserData();
    }

    /**
     * 获取个人信息数据
     */
    private void getUserData() {
        if (!NetworkUtils.isNetworkConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), getString(R.string.net_not_open), 0).show();
            return;
        }

        String staffid = SpFileUtil.getString(getApplicationContext(), SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        if (StringUtils.isEmpty(staffid)) {
            return;
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", staffid);
        AjaxParams param = new AjaxParams(map);

        new FinalHttp().get(Constants.URL_GET_USER_INFO, param, new AjaxCallBack<Object>() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                UIUtils.showTestToast(getApplicationContext(), "errorMsg:" + strMsg);
                Toast.makeText(getApplicationContext(), getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                String errorMsg = "";
                LogOut.i("========", "onSuccess：" + t);
                // UIUtils.showTestToast(getActivity(), t.toString());
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            if (StringUtils.isNotEmpty(data)) {
                                Gson gson = new Gson();
                                UserIndexData userIndexData = gson.fromJson(data, UserIndexData.class);
                                int auth_status = userIndexData.getAuth_status();
                                if (auth_status == 0) {// 未认证
                                    startActivity(new Intent(LoginActivity.this, AuthStatusActivity.class));
                                    LoginActivity.this.finish();
                                } else if (auth_status == 1) {// 已认证
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    LoginActivity.this.finish();
                                }
                                SpFileUtil.saveInt(LoginActivity.this, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_USER_AUTH_STATUS, auth_status);
                            }
                        } else if (status == Constants.STATUS_SERVER_ERROR) { // 服务器错误
                            errorMsg = getString(R.string.servers_error);
                        } else if (status == Constants.STATUS_PARAM_MISS) { // 缺失必选参数
                            errorMsg = getString(R.string.param_missing);
                        } else if (status == Constants.STATUS_PARAM_ILLEGA) { // 参数值非法
                            errorMsg = getString(R.string.param_illegal);
                        } else if (status == Constants.STATUS_OTHER_ERROR) { // 999其他错误
                            errorMsg = msg;
                        } else {
                            errorMsg = getString(R.string.servers_error);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMsg = getString(R.string.servers_error);

                }
                // 操作失败，显示错误信息|
                if (!StringUtils.isEmpty(errorMsg.trim())) {
                    UIUtils.showToast(getApplicationContext(), errorMsg);
                }
            }
        });

    }

}
