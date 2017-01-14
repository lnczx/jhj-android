package com.meijialife.dingdang.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.meijialife.dingdang.service.LocationReportService;
import com.meijialife.dingdang.utils.LogOut;

public class LocationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        LogOut.debug("接收到广播，启动LocationReportService");
        Intent ootStartIntent = new Intent(context, LocationReportService.class);
        context.startService(ootStartIntent);
        
        // if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
        // Log.d("Debug", "开机启动");
        // }else if(Intent.ACTION_USER_PRESENT.equals(intent.getAction())){
        // Log.d("Debug", "手机被唤醒");
        // Intent i = new Intent();
        // i.setClass(context, LocationReportService.class);
        // context.startService(i);
        // }else if(Constants.LOCATION_RECEIVER_ACTION.equals(intent.getAction())){
        // Log.d("Debug", "上次服务被挂了");
        // Intent i = new Intent();
        // i.setClass(context, LocationReportService.class);
        // context.startService(i);
        // }else if(Constants.LOCATION_RECEIVER_CLOCK.equals(intent.getAction())){
        // Log.d("Debug", "定时闹钟的广播");
        // Intent i = new Intent();
        // i.setClass(context, LocationReportService.class);
        // context.startService(i);
        // }
    }
}