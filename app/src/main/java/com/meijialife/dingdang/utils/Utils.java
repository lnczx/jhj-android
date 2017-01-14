package com.meijialife.dingdang.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.view.WindowManager;

public class Utils {

	/**
	 * @Description: 验证手机号是否合法
	 * @param mobile
	 * @return
	 */
	public static boolean verifyPhoneNo(String mobile) {
		Pattern p = Pattern.compile("^(13|15|18)\\d{9}$");
		Matcher m = p.matcher(mobile);
		return m.matches();
	}

	public static int getDeviceHeight(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getHeight();
	}

	/**
	 * 根据手机版本，获取dialog风格
	 *
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static int getDialogTheme() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? android.R.style.Theme_Holo_Dialog
				: android.R.style.Theme_Dialog;
	}

	/**
	 * 获取APK当前版本号
	 *
	 * @return
	 */
	public static String getCurVerName(Context context) {
		String versionName = "";
		try {
			versionName = context.getPackageManager().getPackageInfo(
					"com.meijialife.dingdang", 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return versionName;
	}
	
	/**
     * 判断sdcard是否挂载
     * 
     * @return
     */
    public static boolean isExistSD() {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// sdcard是否挂载
            return true;

        }
        return false;
    }
}
