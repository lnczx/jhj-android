package com.meijialife.dingdang.utils;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *  SharedPreferences 文件，可以把file和key的名字写在这里面
 * @author windows7
 *
 */
public class SpFileUtil {
   
    public static final String FILE_UI_PARAMETER = "ui_parameter";
     
    public static final String KEY_SHORTCUT_ADDED = "shortcut_added";
    public static final String KEY_FIRST_INTO = "first_into";
    
    public static final String KEY_STAFF_ID = "staff_id";
    public static final String KEY_USER_MOBILE = "user_mobile";
    public static final String KEY_USER_AUTH_STATUS = "auth_status";
    
    public static final String KEY_LAT = "lat";
    public static final String KEY_LNG = "lng";

    /**
     * @WARNING Be careful, this maybe overwrite a exist value.<br>
     */
    public static void removeKey(Context context, String filename, String key) {
        SharedPreferences preferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * @WARNNING Be careful, this maybe overwrite a exist value.<br>
     */
    public static void saveString(Context context, String filename, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * get String from shared Preferences.<br>
     * 
     * @PROMPT if the target value is not exist, it will return the defaultValue.
     */
    public static String getString(Context context, String filename, String key, String defaultValue) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
            return preferences.getString(key, defaultValue);
        } catch (Exception e) {
        }
        return defaultValue;
    }

    /**
     * @WARNNING Be careful, this maybe overwrite a exist value.<br>
     */
    public static void savelong(Context context, String filename, String key, long value) {
        SharedPreferences preferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * get long value from shared Preferences.<br>
     * 
     * @PROMPT if the target value is not exist, it will return the defaultValue.
     */
    public static long getlong(Context context, String filename, String key, long defaultValue) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
            return preferences.getLong(key, defaultValue);
        } catch (Exception e) {
        }
        return defaultValue;
    }

    /**
     * @WARNNING Be careful, this maybe overwrite a exist value.<br>
     */
    public static void saveInt(Context context, String filename, String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * get int value from shared Preferences.<br>
     * 
     * @PROMPT if the target value is not exist, it will return the defaultValue.
     */
    public static int getInt(Context context, String filename, String key, int defaultValue) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
            return preferences.getInt(key, defaultValue);
        } catch (Exception e) {
        }
        return defaultValue;
    }

    /**
     * @WARNNING Be careful, this maybe overwrite a exist value.<br>
     */
    public static void saveFloat(Context context, String filename, String key, float value) {
        SharedPreferences preferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    /**
     * get float value from shared Preferences.<br>
     * 
     * @PROMPT if the target value is not exist, it will return the defaultValue.
     */
    public static float getFloat(Context context, String filename, String key, float defaultValue) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
            return preferences.getFloat(key, defaultValue);
        } catch (Exception e) {
        }
        return defaultValue;
    }

    /**
     * @WARNNING Be careful, this maybe overwrite a exist value.<br>
     */
    public static void saveBoolean(Context context, String filename, String key, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * get boolean value from shared Preferences.<br>
     * 
     * @PROMPT if the target value is not exist, it will return the defaultValue.
     */
    public static boolean getBoolean(Context context, String filename, String key, boolean defaultValue) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
            return preferences.getBoolean(key, defaultValue);
        } catch (Exception e) {
        }
        return defaultValue;
    }

    /**
     * get key-value map of particular filename from shared Preferences.<br>
     */
    public static Map<String, ?> getFile(Context context, String filename) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
            return preferences.getAll();
        } catch (Exception e) {
        }
        return null;
    }

    public static void clearFile(Context context, String filename) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
            preferences.edit().clear().commit();
        } catch (Exception e) {
        }
    }
}
