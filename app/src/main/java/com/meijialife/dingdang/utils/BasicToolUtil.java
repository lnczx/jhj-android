package com.meijialife.dingdang.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.meijialife.dingdang.MainActivity;
import com.meijialife.dingdang.R;

public class BasicToolUtil {
    /**
     * 拷贝单个文件，不支持拷贝文件夹
     * 
     * @param srcPath
     * @param destPath
     * @return
     */
    public static boolean copyFile(String srcPath, String destPath) {
        InputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            if (StringUtils.isEmpty(srcPath) || StringUtils.isEmpty(destPath)) {
                return false;
            }
            File srcFile = new File(srcPath);
            File destFile = new File(destPath);
            if (!srcFile.exists() || srcFile.isDirectory()) {
                return false;
            }
            if (destFile.exists()) {
                if (destFile.isDirectory()) {
                    return false;
                }
                destFile.deleteOnExit();
            }
            inStream = new FileInputStream(srcFile);
            outStream = new FileOutputStream(destFile);
            byte[] buffer = new byte[2048];
            int read = 0;
            while (-1 != (read = inStream.read(buffer))) {
                outStream.write(buffer, 0, read);
            }
            return true;
        } catch (Exception e) {
        } finally {
            if (null != inStream) {
                try {
                    inStream.close();
                } catch (Exception e2) {
                }
            }
            if (null != outStream) {
                try {
                    outStream.close();
                } catch (Exception e2) {
                }
            }
        }
        return false;
    }

    public static String getSDCardDir(Context context,String subFile) {
        if (StringUtils.isEmpty(subFile)) {
            return null;
        }
        if (null == context) {
            return null;
        }
        try {
            String state = Environment.getExternalStorageState();
            if (StringUtils.isNotEmpty(state) && state.equals(Environment.MEDIA_MOUNTED)) {
                String extDir = Environment.getExternalStorageDirectory() + File.separator + context.getPackageName() + File.separator + subFile;
                File extFile = new File(extDir);
                if (!extFile.exists()) {
                    extFile.mkdirs();
                } else if (extFile.isFile()) {
                    extFile.deleteOnExit();
                    new File(extDir).mkdirs();
                }
                return extDir;
            }
        } catch (Exception e) {
        }

        return null;
    }

    public static boolean getSDCardState() {
        String state = Environment.getExternalStorageState();
        if (StringUtils.isNotEmpty(state) && state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getSDCardDir(Context context) {
        try {
            if (null != context) {
                String state = Environment.getExternalStorageState();
                if (StringUtils.isNotEmpty(state) && state.equals(Environment.MEDIA_MOUNTED)) {
                    String extDir = Environment.getExternalStorageDirectory() + File.separator + context.getPackageName();
                    File extFile = new File(extDir);
                    if (!extFile.exists()) {
                        extFile.mkdirs();
                    } else if (extFile.isFile()) {
                        extFile.deleteOnExit();
                        new File(extDir).mkdirs();
                    }
                    return extDir;
                }
            }
        } catch (Exception e) {
        }

        return null;
    }

    /**
     * make sure open gps settings
     * 
     * @param activity
     */
    public static void openGPSSettings(final Activity activity) {
        if (null == activity) {
            return;
        }
        LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            UIUtils.showActionDialog(activity, activity.getString(R.string.tip_open_gps), activity.getString(R.string.tip_open_gps_title),
                    activity.getString(R.string.button_open_gps), new Runnable() {
                        @Override
                        public void run() {
                            activity.startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                        }
                    }, activity.getString(R.string.button_cancel), null);
        }
    }

    public static String getTimeLabel() {
        Calendar calendar = Calendar.getInstance();
        return "__W" + calendar.get(Calendar.DAY_OF_WEEK) + "__H" + calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static String getHourLabel() {
        Calendar calendar = Calendar.getInstance();
        return "__H" + calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static boolean isActivityAtTop(Context context) {
        if (null == context) {
            return false;
        }
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<RunningTaskInfo> list = am.getRunningTasks(1);
            if (list == null) {
                return true;
            }
            for (RunningTaskInfo info : list) {
                if (context.getPackageName().equals(info.topActivity.getPackageName())) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    private static long lastClickTime = 0;
    private static final long CLICK_TIME = 500;
    private static int fastClickTime = 0;
    private static final int FAST_CLICK_TIME_FOR_TIP = 3;

    /**
     * 判断是否快速点击
     * @param context
     * @return
     */
    public static boolean isFastClick(Context context) {
        long currentTime = System.currentTimeMillis();
        long timeGap = currentTime - lastClickTime;
        lastClickTime = currentTime;
        if (timeGap < CLICK_TIME) {
            ++fastClickTime;
            if (fastClickTime == FAST_CLICK_TIME_FOR_TIP) {
                fastClickTime = 0;
                if (null != context) {
                    Toast.makeText(context, R.string.tip_fast_clicks, 0).show();
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param boundary
     * @return random integer between 0 and boundary.
     */
    public static double random(long boundary) {
        return Math.random() * boundary;
    }

    public static boolean randomEvent() {
        long event = Math.round(Math.random());
        return 1 == event;
    }

    public static int randomForward() {
        int event = (int) Math.round(Math.random() * 2);
        switch (event) {
        case 0:
            return -1;
        case 2:
            return 1;
        case 1:
        default:
            return 0;
        }
    }

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isWifiState(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (null != connectivity) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null) {
                    return info.getType() == ConnectivityManager.TYPE_WIFI || info.getType() == AndroidUtil.ConnectivityManager.TYPE_ETHERNET
                            || info.getType() == ConnectivityManager.TYPE_WIMAX;
                }
            }
        } catch (Exception e) {
        }

        return false;
    }

 
    public static String getNetworkTypeName(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null) {
                switch (info.getType()) {
                case ConnectivityManager.TYPE_MOBILE:
                    return "MOBILE";
                case ConnectivityManager.TYPE_WIFI:
                    return "WIFI";
                case ConnectivityManager.TYPE_MOBILE_MMS:
                    return "MOBILE_MMS";
                case ConnectivityManager.TYPE_MOBILE_SUPL:
                    return "MOBILE_SUPL";
                case ConnectivityManager.TYPE_MOBILE_DUN:
                    return "MOBILE_DUN";
                case ConnectivityManager.TYPE_MOBILE_HIPRI:
                    return "MOBILE_HIPRI";
                case ConnectivityManager.TYPE_WIMAX:
                    return "WIMAX";
                case AndroidUtil.ConnectivityManager.TYPE_BLUETOOTH:
                    return "BLUETOOTH";
                case AndroidUtil.ConnectivityManager.TYPE_DUMMY:
                    return "DUMMY";
                case AndroidUtil.ConnectivityManager.TYPE_ETHERNET:
                    return "ETHERNET";
                default:
                    return "UNKNOWN";
                }
            }
        }

        return "UNKNOWN";
    }

    public static String getCpuInfo() {
        FileReader fr;
        String[] array = null;
        BufferedReader br;
        try {
            fr = new FileReader("/proc/cpuinfo");
            br = new BufferedReader(fr);
            String text = br.readLine();
            br.close();
            array = text.split(":\\s+", 2);
            return array[1];
        } catch (Exception e) {
        }
        return " ";
    }

    /**
     * 获取内存
     * @return
     */
    public static String getTotalMemory() {
        FileReader fr;
        String[] array = null;
        BufferedReader br;
        try {
            fr = new FileReader("/proc/meminfo");
            br = new BufferedReader(fr);
            String text = br.readLine();
            br.close();
            array = text.split(":\\s+");
            return array[1];
        } catch (Exception e) {
        }
        return " ";
    }

    public static String getTotalRomMemroy() {
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        return stat.getBlockCount() * stat.getBlockSize() / 1024 / 1024 + "MB";
    }

    public static String getAvailableRomMemroy() {
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        return stat.getBlockSize() * stat.getAvailableBlocks() / 1024 / 1024 + "MB";
    }


    /**
     * 得到当前手机号码
     * 
     * @return 本机手机号码（仅部分能获取到）
     */
    public static String getPhoneNum(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String num = tm.getLine1Number();
        if (StringUtils.isEmpty(num) || num.length() <= 3) {
            return "";
        }
        if (num.substring(0, 3).equals("+86")) {
            num = num.substring(3);
        }
        return num;
    }

    /**
     * 格式化当前手机号码
     * 
     * @return 去除空格换行符的字符串
     */
    public static String FomatPhoneNum(String num) {
        String dest = "";
        if (num != null) {
            if (StringUtils.isEmpty(num) || num.length() <= 3) {
                return "";
            }
            if (num.substring(0, 3).equals("+86")) {
                num = num.substring(3);
            }
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(num);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 检查手机号是否合法
     * 
     * @param mobileNum
     * @return boolean
     */
    public static boolean checkMobileNum(Context context, String mobileNum) {
        if (StringUtils.isEmpty(mobileNum)) {
            Toast.makeText(context, "请输入手机号", 0).show();
            return false;
        }
        if (mobileNum.length() != 11) {
            Toast.makeText(context, "请输入正确的手机号", 0).show();
            return false;
        }
        return true;
    }

    /**
     * 判断该应用在手机中的安装情况
     * 
     * @param packageName
     *            要判断应用的包名
     */
    public static boolean checkInstelledAPK(Context context, String packageName) {
        List<PackageInfo> pakageinfos = context.getPackageManager().getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        try {
            for (PackageInfo pi : pakageinfos) {
                String pi_packageName = pi.packageName;
                if (packageName.endsWith(pi_packageName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 赔率除法计算，保留两位小数
     * 
     * @param packageName
     *            要判断应用的包名
     */
    public static String calculateRate(int a, int b) {
        float size = (float) a / b;
        DecimalFormat df = new DecimalFormat("0.00");// 格式化小数，不足的补0
        String rate = df.format(size);// 返回的是String类型的
        return rate;
    }

    // 判断email格式是否正确
    public static boolean checkEmailaddress(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    /****************
     * 
     * 发起添加群流程。群号：风云游戏竞猜群(366576680) 的 key 为： rjZ8JjfBMYpB_dSz07NxPrp7B1GnqxXS 调用 joinQQGroup(rjZ8JjfBMYpB_dSz07NxPrp7B1GnqxXS) 即可发起手Q客户端申请加群
     * 风云游戏竞猜群(366576680)
     * 
     * @param key
     *            由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public static boolean joinQQGroup(Context context, String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D"
                + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面 //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    public static void CheckShortCut(Activity activity) {
        if (!SpFileUtil.getBoolean(activity, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_SHORTCUT_ADDED, false)) {
            SpFileUtil.saveBoolean(activity, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_SHORTCUT_ADDED, true);
            try {
                String launcherName = null;
                ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
                List<RunningAppProcessInfo> list = activityManager.getRunningAppProcesses();
                for (RunningAppProcessInfo info : list) {
                    String pkgName = info.processName;
                    if (pkgName.contains("launcher") && pkgName.contains("android")) {
                        launcherName = pkgName;
                        break;
                    }
                }
                if (StringUtils.isEquals("com.android.launcher", launcherName)) {
                    launcherName += "2";
                }
                String url = "content://" + launcherName + ".settings/favorites?Notify=true";
                ContentResolver resolver = activity.getContentResolver();
                Cursor cursor = resolver.query(Uri.parse(url), new String[] { "intent" }, "intent like ?",
                        new String[] { "%com.fyzb.activity.FyzbWelcomActivity%" }, null);
                if (cursor.getCount() == 0) {
                    Intent localIntent1 = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
                    localIntent1.putExtra(Intent.EXTRA_SHORTCUT_NAME, activity.getResources().getString(R.string.app_name));
                    localIntent1.putExtra("duplicate", false);
                    Intent localIntent2 = new Intent(Intent.ACTION_MAIN);
                    localIntent2.addCategory(Intent.CATEGORY_LAUNCHER);
                    localIntent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    localIntent2.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                    localIntent2.setClass(activity, MainActivity.class);
                    localIntent1.putExtra(Intent.EXTRA_SHORTCUT_INTENT, localIntent2);
                    localIntent1.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(activity, R.drawable.ic_launcher));
                    activity.sendBroadcast(localIntent1);
                }
            } catch (Exception e) {
            }
            SpFileUtil.saveBoolean(activity, SpFileUtil.FILE_UI_PARAMETER, SpFileUtil.KEY_SHORTCUT_ADDED, true);
        }
    }

    public static boolean getIsMiuiSystem() {
		String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
		String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
		String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
			return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
						|| prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
						|| prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
		} catch (final IOException e) {
			return false;
		}
	}
    
 

    public static boolean isGif(String path) {
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        try {
            String suffix = path.substring(path.lastIndexOf("."), path.length());
            if (suffix.contains("gif") || suffix.contains("GIF")) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
    
    /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int getdip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  

}
