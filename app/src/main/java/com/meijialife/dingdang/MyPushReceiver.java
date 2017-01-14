package com.meijialife.dingdang;

import java.util.Date;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.meijialife.dingdang.Alerm.AlermDialog;
import com.meijialife.dingdang.Alerm.AlermUtils;
import com.meijialife.dingdang.activity.LoginActivity;
import com.meijialife.dingdang.activity.SplashActivity;
import com.meijialife.dingdang.bean.ReceiverBean;
import com.meijialife.dingdang.service.LocationReportService;
import com.meijialife.dingdang.utils.LogOut;

public class MyPushReceiver extends BroadcastReceiver {

    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView == null)
     */
    public static StringBuilder payloadData = new StringBuilder();
    private Context mContext;
    private ReceiverBean receiverBean;
    private Date fdate;
    private final String ACTION_DISPATCH = "dispatch";// 派工
    private final String ACTION_MSG = "msg";

    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext = context;

        LogOut.debug("接收到个推推送广播，启动LocationReportService");
        try {
            Intent ootStartIntent = new Intent(context, LocationReportService.class);
            context.startService(ootStartIntent);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        
        Bundle bundle = intent.getExtras();
        Log.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));

        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
        case PushConsts.GET_MSG_DATA:
            // 获取透传数据
            // String appid = bundle.getString("appid");
            byte[] payload = bundle.getByteArray("payload");

            String taskid = bundle.getString("taskid");
            String messageid = bundle.getString("messageid");

            // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
            boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
            System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));

            if (payload != null) {
                String data = new String(payload);
                Log.d("GetuiSdkDemo", "receiver payload : " + data);
                payloadData.append(data);
                payloadData.append("\n");
                LogOut.debug("pushdata:" + data + "\n");
                System.out.println("个推推送data:" + data);
                try {
                    receiverBean = new Gson().fromJson(data, new TypeToken<ReceiverBean>() {
                    }.getType());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (null != receiverBean) {
                    String action = receiverBean.getAction();
                    // 推送通知
                    setNotification(receiverBean);
                    // 弹出大屏闹钟
                    AlermDialog dlg = new AlermDialog(context, receiverBean, action);
                    dlg.show();
                    AlermUtils.playAudio(context);
                }
            }
            break;
        case PushConsts.GET_CLIENTID:
            // 获取ClientID(CID)
            // 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
            String cid = bundle.getString("clientid");
            // if (GetuiSdkDemoActivity.tView != null) {
            // GetuiSdkDemoActivity.tView.setText(cid);
            // }
            LoginActivity.clientid = cid;
            SplashActivity.clientid = cid;
            MainActivity.clientid = cid;
            LogOut.debug("个推推送cid:" + cid);
            System.out.println("个推推送cid:" + cid);

            break;

        case PushConsts.THIRDPART_FEEDBACK:
            /*
             * String appid = bundle.getString("appid"); String taskid = bundle.getString("taskid"); String actionid = bundle.getString("actionid");
             * String result = bundle.getString("result"); long timestamp = bundle.getLong("timestamp");
             * 
             * Log.d("GetuiSdkDemo", "appid = " + appid); Log.d("GetuiSdkDemo", "taskid = " + taskid); Log.d("GetuiSdkDemo", "actionid = " +
             * actionid); Log.d("GetuiSdkDemo", "result = " + result); Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
             */
            break;

        default:
            break;
        }
    }

    private void setNotification(ReceiverBean receiverBean) {
        // NotificationManager状态通知的管理类，必须通过getSystemService()方法来获取
        NotificationManager manager = (NotificationManager) mContext.getSystemService(android.content.Context.NOTIFICATION_SERVICE);

        // 点击通知负责页面跳转
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra("mAction", receiverBean.getAction());
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // android3.0以后采用NotificationCompat构建
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setContentTitle(receiverBean.getRemind_title());
        builder.setContentText(receiverBean.getRemind_content());
        builder.setSmallIcon(R.drawable.ic_launcher_logo);
        builder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher_logo));
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        // builder.setSubText("点击进入播放...");
        // builder.setTicker(channelNanme + "的" + programName + "还有五分钟就要开播了！" +
        // "点击进入播放...");
        manager.notify(1, builder.build());

    }
}
