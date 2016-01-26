package com.meijialife.dingdang;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.igexin.sdk.PushManager;

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    public static Context applicationContext;
    private static MyApplication instance;
    // login user name
    public final String PREF_USERNAME = "username";

    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
    public static String currentUserNick = "";

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        instance = this;
 
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);

        PushManager.getInstance().initialize(this.getApplicationContext());
    }

    public static MyApplication getInstance() {
        return instance;
    }

}
