package com.meijialife.dingdang.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

 
public class KeyBoardUtils {

    /**
     * 打卡软键盘
     * 
     * @param mEditText输入框
     * @param mContext上下文
     */
    public static void openKeybord(View view, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     * 
     * @param mEditText输入框
     * @param mContext上下文
     */
    public static void closeKeybord(View view, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
