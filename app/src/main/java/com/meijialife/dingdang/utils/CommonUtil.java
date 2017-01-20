package com.meijialife.dingdang.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 工具类
 *
 * @author yejiurui
 */
public class CommonUtil {

    private static final int OPGL_MAX_TEXTURE = 4000;
    private static int height = -1;
    private static int width = -1;
    private static long lastClickTime = 0;
    private static boolean IS_LE_DEFULT_IC_SAVE = false;
    private static String ALBUM_PATH = "";
    private static String SHARE_DEFAULT_ICON = "/icon.png";

    /**
     * compute listview height
     */
    @Deprecated //废弃该方法，使用自定义控件ListViewForInner来解决
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {

            View listItem = listAdapter.getView(i, null, listView);

            if (listItem == null) continue;
            listItem.measure(0, 0);

            totalHeight += listItem.getMeasuredHeight();

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));


        listView.setLayoutParams(params);

    }


    public static String getLeVersionName(Context context) {
        String versionName = null;
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 应用版本比较
     *
     * @param versionServer 服务器版本
     * @return true 有更新,false 无更新
     */
    public static boolean appVersionComparison(Context context, String versionServer) {
        String versionLocal = getLeVersionName(context);

        if (versionServer == null || versionServer.length() == 0 || versionLocal == null || versionLocal.length() == 0)
            throw new IllegalArgumentException("Invalid parameter!");

        int index1 = 0;
        int index2 = 0;
        while (index1 < versionServer.length() && index2 < versionLocal.length()) {
            int[] number1 = getValue(versionServer, index1);
            int[] number2 = getValue(versionLocal, index2);

            if (number1[0] < number2[0]) {
                return false;
            } else if (number1[0] > number2[0]) {
                return true;
            } else {
                index1 = number1[1] + 1;
                index2 = number2[1] + 1;
            }
        }
        if (index1 == versionServer.length() && index2 == versionLocal.length()) {
            return false;
        }
        return index1 < versionServer.length();
    }

    private static int[] getValue(String version, int index) {
        int[] value_index = new int[2];
        StringBuilder sb = new StringBuilder();
        while (index < version.length() && version.charAt(index) != '.') {
            sb.append(version.charAt(index));
            index++;
        }
        value_index[0] = Integer.parseInt(sb.toString());
        value_index[1] = index;
        return value_index;
    }

    /**
     * 根据手机版本，获取dialog风格
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static int getDialogTheme() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? android.R.style.Theme_Holo_Dialog
                : android.R.style.Theme_Dialog;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /*
    * 判断是否存在微信客户端
    * */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * 判断是否快速点击
     */
    public static boolean isFastClick(Context context) {
//        long currentTime = System.currentTimeMillis();
//        long timeGap = currentTime - lastClickTime;
//        lastClickTime = currentTime;
//        if (timeGap < CLICK_TIME) {
//            ++fastClickTime;
//            if (fastClickTime == FAST_CLICK_TIME_FOR_TIP) {
//                fastClickTime = 0;
//                if (null != context) {
//                    Toast.makeText(context, R.string.tip_fast_click, Toast.LENGTH_SHORT).show();
//                }
//            }
//            return true;
//        } else {
//            return false;
//        }
        long var0 = System.currentTimeMillis();
        if (var0 - lastClickTime < 500L) {
            if (null != context) {
                Toast.makeText(context, "少年慢点", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else {
            lastClickTime = var0;
            return false;
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int getdip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static boolean isBigPicture(Bitmap bitmap) {
        //penGLRenderer: Bitmap too large to be uploaded into a texture (299x7200, max=4096x4096)
        return bitmap.getHeight() >= OPGL_MAX_TEXTURE || bitmap.getWidth() >= OPGL_MAX_TEXTURE;
    }


    /**
     * 判断奇数偶数
     */
    public static boolean isOdd(int num) {
        return !(num % 2 == 0);
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getHeight(Context context) {
        if (height < 0) {
            getValues(context);
        }
        return height;
    }

    public static int getWidth(Context context) {
        if (width < 0) {
            getValues(context);
        }
        return width;
    }

    private static void getValues(Context context) {
        DisplayMetrics display;
        display = context.getResources().getDisplayMetrics();
        width = display.widthPixels;
        height = display.heightPixels;
    }

    /**
     * 判断是否有SD卡
     */
    private static boolean sdcardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable();
    }

    public static int getMax(List<Integer> list) {
        try {
            int maxDevation = 0;
            int totalCount = list.size();
            if (totalCount >= 1) {
                int max = list.get(0);
                for (int i = 0; i < totalCount; i++) {
                    int temp = list.get(i);
                    if (temp > max) {
                        max = temp;
                    }
                }
                maxDevation = max;
            }
            return maxDevation;
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 限制昵称的长度
     */
    public static String limitNickName(String nickname) {
        try {
            if (StringUtils.isNotEmpty(nickname) && nickname.length() > 15) {
                return nickname.substring(0, 12) + "...";
            } else {
                return nickname;
            }
        } catch (Exception e) {
            return nickname;
        }
    }

    /**
     * 判断app处于后台状态
     */
    public static boolean isApplicationInBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = taskList.get(0).topActivity;
            if (topActivity != null && !topActivity.getPackageName()
                    .equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }



    private static String channel = null;

    /**
     * 获取渠道名
     *
     * @param context
     * @return
     */
    public static String getChannelName(Context context) {
        if (channel != null) {
            return channel;
        }

        final String start_flag = "META-INF/channel_";
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.contains(start_flag)) {
                    channel = entryName.replace(start_flag, "");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (channel == null || channel.length() <= 0) {
            channel = "lesports";//读不到渠道号就默认官方渠道
        }
        return channel;

    }

    /**
     * 仅可输入包含中文、英文、数字的文字
     *
     * @param str
     * @return
     */
    public static boolean isRightString(String str) {
        String regex = "^[a-zA-Z0-9\u4E00-\u9FA5]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(str);
        boolean b = match.matches();
        if (b) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 防止多次点击
     *
     * @return
     */
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 判断客户端是否安装了
     *
     * @param context
     * @param packageName
     * @return
     */

    public static boolean isClientInstalled(Context context, String packageName) {

        if (context == null || packageName == null || packageName == "")
            return false;
        PackageManager packageManager = context.getPackageManager();
        if (packageManager != null) {
            List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
            if (pinfo == null)
                return false;
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(packageName))
                    return true;
            }

        }
        return false;
    }

    /**
     * 电话号码验证
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNo(String mobiles) {
        Pattern p = Pattern.compile("^1(3[0-9]|4[57]|5[0-35-9]|7[01678]|8[0-9])\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }


    /**
     * 数字转str，小于1000万，最多含有3个数字。
     *
     * @param i
     * @return
     */
    public static String intConvertStr(int i) {
        String result = "0";
        if (i <= 0) {
            return result;
        } else if (i < 1000) {
            return String.valueOf(i);
        } else if (i < 10000) {
            return subStr(String.valueOf(i / 1000.0)) + "k";
        } else if (i < 10000000) {
            return subStr(String.valueOf(i / 10000.0)) + "w";
        }
        return result;
    }

    private static String subStr(String s) {
        if (s == null || s == "" || s.length() <= 3) {
            return s;
        } else if (s.substring(0, 3).endsWith(".")) {
            return s.substring(0, 4);
        } else return s.substring(0, 3);
    }

}