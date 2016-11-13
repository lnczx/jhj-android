package com.meijialife.dingdang.utils;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 软键盘工具类
 * Created by yejiurui on 16/11/13.
 */

public class InputMethodUtils {
    /**
     * 为给定的编辑器开启软键盘
     *
     * @param editText 给定的编辑器
     */
    public static void openSoftKeyboard(Context context, EditText editText) {
        editText.requestFocus();
        InputMethodManager inputMethodManager
                = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText,
                InputMethodManager.SHOW_IMPLICIT);
        Editable editable = editText.getEditableText();
        Selection.setSelection(editable,
                editable.toString().length());
    }

    public static void closeSoftKeyboard(Activity activity) {
        if (activity == null || activity.getCurrentFocus() == null) return;
        InputMethodManager inputMethodManager
                = (InputMethodManager) activity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

        }
    }

    //add by  andye
    //activity.getCurrentFocus() 有可能为空，切换输入法状态
    @Deprecated
    public static void closeSoftKeyboardInFragment(Activity activity) {
        if (activity == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        if (imm.isActive()) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    //通过定时器强制隐藏虚拟键盘
    public static void TimerHideKeyboard(final View v)
    {
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run()
            {
                InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
                if (imm.isActive())
                {
                    imm.hideSoftInputFromWindow( v.getApplicationWindowToken() , 0 );
                }
            }
        }, 10);
    }


    /**
     * 切换软键盘的状态
     */
    public static void toggleSoftKeyboardState(Context context) {
        ((InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE)).toggleSoftInput(
                InputMethodManager.SHOW_IMPLICIT,
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 动态隐藏软键盘
     */
    public static void hideSoftInput(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        LogOut.i("test", "view::" + view);
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 判断隐藏软键盘是否弹出,弹出就隐藏
     *
     * @param mActivity
     * @return
     */
    public static boolean keyBoxIsShow(Activity mActivity) {
        if (mActivity.getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
//            mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            return true;
        } else {
            return false;
        }
    }

}