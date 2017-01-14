package com.meijialife.dingdang.broadcastReceiver;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.meijialife.dingdang.R;

@SuppressLint("NewApi")
public class ServiceManager extends Service {
    
    public static final String ACTION = "com.meijialife.dingdang.broadcastReceiver.ServiceManager";
    
    private Notification mNotification;
    private NotificationManager mManager;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        initNotifiManager();
    }
                                                                                                                                                                                                                           
    @Override
    public void onStart(Intent intent, int startId) {
        showDlg();
        /*new PollingThread().start();*/
    }
    //初始化通知栏配置
    private void initNotifiManager() {
        mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int icon = R.drawable.ic_launcher;
        mNotification = new Notification();
        mNotification.icon = icon;
        mNotification.tickerText = "New Message";
        mNotification.defaults |= Notification.DEFAULT_SOUND;
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
    }
    private void showDlg() {
        
        Toast.makeText(this,"我是后台服务", Toast.LENGTH_SHORT).show();
    }

    /**
     * Polling thread
     * 模拟向Server轮询的异步线程
     * @Author Ryan
     * @Create 2013-7-13 上午10:18:34
     */
    int count = 0;
    class PollingThread extends Thread {
        @Override
        public void run() {
            System.out.println("Polling...");
            count ++;
            //当计数能被5整除时弹出通知
            if (count % 5 == 0) {
                showDlg();
            }
        }
    }
               
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        
        Toast.makeText(this,"onStartCommand", Toast.LENGTH_SHORT).show();
        startService(intent);
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
