package com.meijialife.dingdang.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import com.google.gson.Gson;
import com.meijialife.dingdang.Constants;
import com.meijialife.dingdang.MainActivity;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.bean.User;
import com.meijialife.dingdang.bean.UserIndexData;
import com.meijialife.dingdang.bean.UserInfo;
import com.meijialife.dingdang.database.DBHelper;
import com.meijialife.dingdang.utils.LogOut;
import com.meijialife.dingdang.utils.NetworkUtils;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.StringUtils;
import com.meijialife.dingdang.utils.UIUtils;

public class SplashActivity extends  Activity    {

    private static final int sleepTime = 2000;
    public static String clientid;
    private String staff_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.layout_splash);
        super.onCreate(savedInstanceState);

        getUserData();
        
        Timer timer = new Timer();
        TimerTask MyTask = new TimerTask() {
            @Override
            public void run() {
            	  startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            	  SplashActivity.this.finish();
            }
        };
        timer.schedule(MyTask, 2000);
        
//        if (StringUtils.isNotEmpty(staff_id)) {
//            bind_user(staff_id, clientid);
//        }

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
       if(StringUtils.isEmpty(staffid)){
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
                               SpFileUtil.saveInt(SplashActivity.this, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_USER_AUTH_STATUS, userIndexData.getAuth_status());
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

    /**
     * 绑定接口
     * 
     * @param clientid2
     * 
     * @param date
     */
    private void bind_user(String useid, String clientid) {

        if (StringUtils.isEmpty(clientid) || StringUtils.isEmpty(useid)) {
            return;
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", useid + "");
        map.put("device_type", "android");
        map.put("client_id", clientid);
        AjaxParams param = new AjaxParams(map);

        new FinalHttp().post(Constants.URL_POST_PUSH_BIND, param, new AjaxCallBack<Object>() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                UIUtils.showTestToast(SplashActivity.this, "绑定clientid：" + strMsg);
                Toast.makeText(SplashActivity.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                String errorMsg = "";
                LogOut.i("========", "onSuccess：" + t);
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            // UIUtils.showToast(mContext, "推送绑定成功");
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
                    UIUtils.showToast(SplashActivity.this, errorMsg);
                }
            }
        });

    }
}
