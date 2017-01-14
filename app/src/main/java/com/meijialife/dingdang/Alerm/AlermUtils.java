package com.meijialife.dingdang.Alerm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;

import com.meijialife.dingdang.utils.DateUtils;
import com.meijialife.dingdang.utils.LogOut;

/**
 * 提醒工具类
 *
 */
public class AlermUtils {
	
    public static final String TAG = "Alerm";
    
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
    private static SimpleDateFormat formatMonth = new SimpleDateFormat("MM");
    private static SimpleDateFormat formatDay = new SimpleDateFormat("dd");
    private static SimpleDateFormat formatHours = new SimpleDateFormat("HH");
    private static SimpleDateFormat formatMinutes = new SimpleDateFormat("mm");
    private static SimpleDateFormat formatSeconds = new SimpleDateFormat("ss");
    private static SimpleDateFormat formatCode = new SimpleDateFormat("MMddHHmm");
    
    /**
     * 初始化本地提醒闹钟
     * 
     * @param context
     * @param remindAlerm 提醒设置 0 = 不提醒 1 = 按时提醒 2 = 5分钟 3 = 15分钟 4 = 提前30分钟 5 = 提前一个小时 6 = 提前2小时 7 = 提前6小时 8 = 提前一天 9 = 提前两天
     * @param date 时间   yyyy-MM-dd HH:mm:ss
     * @param title 提醒类型 差旅规划 or 会议安排 or 事务提醒。。。。。等等等
     * 
     */
    public static void initAlerm(Context context, int remindAlerm, Date date, String title, String msg){
        if(date == null){
            return;
        }
//        Date date = new Date(lDate.getYear()-1900, lDate.getMonthOfYear()-1, lDate.getDayOfMonth(), lTime.getHourOfDay(), lTime.getMinuteOfHour(), lTime.getSecondOfMinute());
        
        LogOut.i("===========", "任务时间"+formatYear.format(date)+"-" + formatMonth.format(date)+"-" + formatDay.format(date)+" " + formatHours.format(date)+":" + formatMinutes.format(date)+":" + formatSeconds.format(date));
        
        switch (remindAlerm) {
        case 0://不提醒
            LogOut.i(TAG, "不提醒");
            break;
        case 1://按时提醒
            setAlerm(context, date, title, msg);
            break;
        case 2://5分钟
            setAlerm(context, DateUtils.getDate5(date), title, msg);
            break;
        case 3://15分钟
            setAlerm(context, DateUtils.getDate15(date), title, msg);
            break;
        case 4://提前30分钟
            setAlerm(context, DateUtils.getDate30(date), title, msg);
            break;
        case 5://提前一个小时
            setAlerm(context, DateUtils.getDate1(date), title, msg);
            break;
        case 6://提前2小时
            setAlerm(context, DateUtils.getDate2(date), title, msg);
            break;
        case 7://提前6小时
            setAlerm(context, DateUtils.getDate6(date), title, msg);
            break;
        case 8://提前一天
            setAlerm(context, DateUtils.getDate1d(date), title, msg);
            break;
        case 9://提前两天
            setAlerm(context, DateUtils.getDate2d(date), title, msg);
            break;

        default:
            break;
        }
        
    }
    public static void initAlerm(Context context, int remindAlerm, Date date, String title, String msg,String card_id){
        if(date == null){
            return;
        }
//        Date date = new Date(lDate.getYear()-1900, lDate.getMonthOfYear()-1, lDate.getDayOfMonth(), lTime.getHourOfDay(), lTime.getMinuteOfHour(), lTime.getSecondOfMinute());
        
        LogOut.i("===========", "任务时间"+formatYear.format(date)+"-" + formatMonth.format(date)+"-" + formatDay.format(date)+" " + formatHours.format(date)+":" + formatMinutes.format(date)+":" + formatSeconds.format(date));
        
        switch (remindAlerm) {
        case 0://不提醒
            LogOut.i(TAG, "不提醒");
            break;
        case 1://按时提醒
            setAlerm(context, date, title, msg,card_id);
            break;
        case 2://5分钟
            setAlerm(context, DateUtils.getDate5(date), title, msg,card_id);
            break;
        case 3://15分钟
            setAlerm(context, DateUtils.getDate15(date), title, msg,card_id);
            break;
        case 4://提前30分钟
            setAlerm(context, DateUtils.getDate30(date), title, msg,card_id);
            break;
        case 5://提前一个小时
            setAlerm(context, DateUtils.getDate1(date), title, msg,card_id);
            break;
        case 6://提前2小时
            setAlerm(context, DateUtils.getDate2(date), title, msg,card_id);
            break;
        case 7://提前6小时
            setAlerm(context, DateUtils.getDate6(date), title, msg,card_id);
            break;
        case 8://提前一天
            setAlerm(context, DateUtils.getDate1d(date), title, msg,card_id);
            break;
        case 9://提前两天
            setAlerm(context, DateUtils.getDate2d(date), title, msg,card_id);
            break;

        default:
            break;
        }
        
    }
    /**
     * 初始化本地提醒闹钟
     * 
     * @param context
     * @param date 时间   yyyy-MM-dd HH:mm:ss
     * @param title 提醒类型 差旅规划 or 会议安排 or 事务提醒。。。。。等等等
     * 
     */
    public static void initAlerm(Context context, Date date, String title, String msg){
        if(date == null){
            return;
        }
        LogOut.i("===========", "任务时间"+formatYear.format(date)+"-" + formatMonth.format(date)+"-" + formatDay.format(date)+" " + formatHours.format(date)+":" + formatMinutes.format(date)+":" + formatSeconds.format(date));
        
        setAlerm(context, date, title, msg);
    }
    
	/**
	 * 设置提醒闹钟
	 */
	private static void setAlerm(Context context, Date date, String title, String text){}
	private static void setAlerm(Context context, Date date, String title, String text,String card_id){}
	/**
	 * 取消提醒闹钟
	 */
	public static void cancelSigninAlerm(Context context){}
	
	/**
     * 播放通知
     */
    public static void playAudio(Context context) {
        MediaPlayer mp = new MediaPlayer();
        try {
            mp.setDataSource(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
