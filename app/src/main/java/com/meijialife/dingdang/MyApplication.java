package com.meijialife.dingdang;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.igexin.sdk.PushManager;
import com.meijialife.dingdang.service.LocationReportService;
import com.meijialife.dingdang.utils.LogOut;

import org.xutils.x;

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
    BMapManager mBMapMan = null;// MKGeneralListener

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        instance = this;

        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        
        bindService();
        
        PushManager.getInstance().initialize(this.getApplicationContext());

        //fresco 初始化
        try {
            Fresco.initialize(this, ImagePipelineConfig.newBuilder(this)
                    .setDownsampleEnabled(true).build());//fresco
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Xutils3.0 初始化
        try {
            x.Ext.init(this);
            x.Ext.setDebug(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startAlarm() {
        LogOut.debug("start alarm service");
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent collectIntent = new Intent(this, LocationReportService.class);
        PendingIntent collectSender = PendingIntent.getService(this, 0, collectIntent, 0);
        am.cancel(collectSender);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 10 * 1000, collectSender);
    }

    public static MyApplication getInstance() {
        return instance;
    }
    
    private void bindService() {
        LogOut.debug("start LocationReportService service");
        Intent intent2 = new Intent(this, LocationReportService.class);
        startService(intent2);
    }

}
