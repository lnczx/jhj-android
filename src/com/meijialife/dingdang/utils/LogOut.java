package com.meijialife.dingdang.utils;

import android.util.Log;

public class LogOut {
    private static final boolean isOnlineModel = false;//这是总开关,是否是上线模式(当上线时置为true)
    protected static final String TAG = "Debug";

    public static void i(String tag, String content) {
        if (!isOnlineModel)
            Log.i(tag, content);
    }

    public static void debug(String content) {
        if (!isOnlineModel)
            Log.d(TAG, content + "  " + "time: " + System.currentTimeMillis());
    }
    
}
