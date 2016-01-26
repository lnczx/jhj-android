package com.meijialife.dingdang.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * 日历工具类，封装一些日期和时间的方法
 * 
 */
public class CalendarUtils {

    /**
     * 获得当前的日期和星期
     * @return
     */
    public static String getTodayTimeAndWeek() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "日";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return mYear + "年" + mMonth + "月" + mDay + "日" + "  星期" + mWay;
    }

    /**
     * 获取某一天的日期：yyyy-MM-dd
     * 
     * @param which
     *            哪一天，默认0今天，1：明天（整数往后推,负数往前移动）
     * @return
     */
    public static String getDateStr_(int which) {
        String dateString = "";

        Date date = new Date();// 取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, which);// 把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        dateString = formatter.format(date);

        return dateString;
    }

    /**
     * 获取当前时间 格式：yyyy-MM-dd HH:mm
     * 
     * @return
     */
    public static String getDateStrYMDHM() {
        String dateString = "";

        Date date = new Date();// 取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateString = formatter.format(date);

        return dateString;
    }

    /**
     * 获取当前年份 格式：YYYY
     * 
     * @return
     */
    public static int getCurrentYear() {
        String dateString = "";

        Date date = new Date();// 取时间
        // Calendar calendar = new GregorianCalendar();
        // calendar.setTime(date);
        // date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        dateString = formatter.format(date);

        return Integer.parseInt(dateString);
    }

    /**
     * 获取当前月份 格式：MM
     * 
     * @return
     */
    public static int getCurrentMonth() {
        String dateString = "";

        Date date = new Date();// 取时间
        // Calendar calendar = new GregorianCalendar();
        // calendar.setTime(date);
        // date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("MM");
        dateString = formatter.format(date);

        return Integer.parseInt(dateString);
    }

}
