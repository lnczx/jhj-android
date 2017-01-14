package com.meijialife.dingdang.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationData;
import com.meijialife.dingdang.Constants;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.broadcastReceiver.LocationReceiver;
import com.meijialife.dingdang.utils.DateUtils;
import com.meijialife.dingdang.utils.LogOut;
import com.meijialife.dingdang.utils.NetworkUtils;
import com.meijialife.dingdang.utils.SpFileUtil;
import com.meijialife.dingdang.utils.StringUtils;
import com.meijialife.dingdang.utils.UIUtils;

/**
 * @description： 每天7:00——22::00上报当前位置
 * @author： YEJIURUI
 * @date:2016年11月01日
 */
public class LocationReportService extends Service implements BDLocationListener {

    private LocationClient mLocClient;
    private String addStr = "";
    private String lat = "";
    private String lng = "";

    private boolean location_status = false;// 是否成功获去地理置位置
    private boolean start_flag = false;// 是否是7点到22点
    private int sleep_start_time = 22;//睡眠开始时间
    private int sleep_stop_time = 7;//睡眠结束时间
    static private int SleepCircle = 15 * 60 * 1000;// 15分钟上报一次

    static private Thread statThread;
    static private boolean alive = true;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            mLocClient.start();
            reportLocationHttp();
            super.handleMessage(msg);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogOut.debug("service create");
        initLocation();

        Intent intent = new Intent(getApplicationContext(), LocationReceiver.class);
        intent.setAction("fyzb.alam.action");
        long firstime = SystemClock.elapsedRealtime();
        PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME, firstime, 300 * 1000, sender);
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.setPriority(2147483647);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.ACTION_SHUTDOWN");
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.intent.action.BOOT_COMPLETED");
        filter.addAction("android.intent.action.USER_PRESENT");
        filter.addAction("android.intent.action.PACKAGE_RESTARTED");
        filter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        filter.addAction("com.baidu.android.moplus.action.RESTART");
        filter.addAction("com.baidu.android.moplus.action.START");
        filter.addAction("android.net.wifi.p2p.PERSISTENT_GROUPS_CHANGED");
        filter.addAction("android.net.wifi.p2p.CONNECTION_STATE_CHANGE");
        filter.addAction("android.net.nsd.STATE_CHANGED");
        filter.addAction("android.media.ACTION_SCO_AUDIO_STATE_UPDATED");
        filter.addAction("android.media.MASTER_MUTE_CHANGED_ACTION");
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE_IMMEDIATE");
        filter.addAction("fyzb.alam.action");
        registerReceiver(new LocationReceiver(), localIntentFilter);

        reportThread();

    }

    /**
     * 位置上报进程
     */
    private void reportThread() {
        try {
            if (null == statThread || statThread.isAlive() == false) {
                statThread = new Thread() {
                    @Override
                    public void run() {
                        while (alive) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            try {
                                sleep(SleepCircle);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                };
                statThread.start();
            }
        } catch (Throwable e) {
        }
    }

    /**
     * 位置上报请求
     */
    private void reportLocationHttp() {
        LogOut.debug("service request try");

//        Calendar c = Calendar.getInstance();
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MARCH) + 1;
//        int day = c.get(Calendar.DATE);
//        String startTime = year + "-" + month + "-" + day + " " + sleep_start_time;
//        String endTime = year + "-" + month + "-" + day + " " + sleep_stop_time;

        // Long startLong = DateUtils.getDateByPattern(startTime, "yyyy-MM-dd HH:mm").getTime();
        // Long endLong = DateUtils.getDateByPattern(endTime, "yyyy-MM-dd HH:mm").getTime();
        // Long currentTime = new Date().getTime();
        // if (currentTime >= startLong && currentTime < endLong) {
        // start_flag = true;
        // } else {
        // start_flag = false;
        // }

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour >= sleep_start_time && hour < sleep_stop_time) {
            start_flag = false;
        } else {
            start_flag = true;
        }

        LogOut.debug("location_status: " + location_status + " start_flag: " + start_flag);
        if (location_status && start_flag) {
            sendLocation(lat, lng, addStr);
        } else {
            LogOut.debug("2点-7点不上报位置");
        }
    }

    private void initLocation() {
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        mLocClient.setLocOption(option);

    }

    /**
     * 发送地理位置接口
     * 
     * @param city
     * @param lng
     */
    private void sendLocation(String lat, String lng, String city) {
        LogOut.debug("service send location now");
        if (!NetworkUtils.isNetworkConnected(LocationReportService.this)) {
            Toast.makeText(LocationReportService.this, getString(R.string.net_not_open), 0).show();
            return;
        }
        String staffid = SpFileUtil.getString(getApplicationContext(), SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_STAFF_ID, "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", staffid);
        map.put("user_type", "1");
        map.put("lat", lat);// weidu
        map.put("lng", lng);// jingdu
        map.put("poi_name", city);
        AjaxParams param = new AjaxParams(map);
        new FinalHttp().post(Constants.URL_POST_USER_TRAIL, param, new AjaxCallBack<Object>() {
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                UIUtils.showTestToast(LocationReportService.this, "发送位置errorMsg:" + strMsg);
                UIUtils.showToast(LocationReportService.this, "发送位置失败");
                LogOut.debug("service send location error:" + strMsg);
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                String errorMsg = "";
                try {
                    if (StringUtils.isNotEmpty(t.toString())) {
                        JSONObject obj = new JSONObject(t.toString());
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        String data = obj.getString("data");
                        if (status == Constants.STATUS_SUCCESS) { // 正确
                            /*
                             * UIUtils.showToast(MainActivity.this, "发送位置成功");
                             */
                            LogOut.debug("service send loac success!!!!");
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
                    UIUtils.showToast(LocationReportService.this, errorMsg);
                }
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogOut.debug("Service onStartCommand");
        if (!mLocClient.isStarted()) {
            mLocClient.start();
        }
        alive = true;
        reportThread();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        LogOut.debug("service destory");
        if (mLocClient != null && mLocClient.isStarted()) {
            mLocClient.stop();
            mLocClient = null;
        }

        alive = false;
        stopForeground(true);

        try {
            if (null != statThread && statThread.isAlive()) {
                statThread.interrupt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent i = new Intent("com.meijialife.service_destory");
        sendBroadcast(i);

        super.onDestroy();
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        if (location == null)
            return;
        MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
        // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();

        addStr = location.getAddrStr();// 详细地址信息
        lat = location.getLatitude() + "";
        lng = location.getLongitude() + "";

        LogOut.debug("service lat:" + lat + " lng:" + lng + " addStr:" + addStr);
        if (location.hasAddr()) {// 如果有地址信息
            if (StringUtils.isNotEmpty(lat) && StringUtils.isNotEmpty(lng) && StringUtils.isNotEmpty(addStr)) {
                location_status = true;
            }
            // 如果已经得到位置，销毁定位
            mLocClient.stop();
        }

    }
}
