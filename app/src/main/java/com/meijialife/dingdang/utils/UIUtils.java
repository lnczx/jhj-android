package com.meijialife.dingdang.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.view.Gravity;
import android.widget.Toast;

import com.meijialife.dingdang.R;

public class UIUtils {

    private static Boolean debugBoolean = false;

    public static void showTestToast(Context context, String msg) {
        if (debugBoolean) {
            Toast.makeText(context, StringUtils.isEmpty(msg) ? "" : msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showTestToastLong(Context context, String msg) {
        if (debugBoolean) {
            Toast.makeText(context, StringUtils.isEmpty(msg) ? "" : msg, Toast.LENGTH_LONG).show();
        }
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToastLong(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    // 自定义吐司
    // private enum TOAST_TYPE {
    // NO_TEXT, HORIZONTAL_IMAGEVIEW, VERTICAL_IMAGEVIEW
    // };
    //
    // private static TOAST_TYPE mCurToastType = TOAST_TYPE.NO_TEXT;
    // private static Toast mToast;
    //
    // private static void initToast(Context context, String msg, Drawable imageRes, int gravity, boolean isHorizontal) {
    // View view;
    // if (StringUtils.isEmpty(msg)) {
    // view = LayoutInflater.from(context).inflate(R.layout.custom_toast_imageview, null);
    // mCurToastType = TOAST_TYPE.NO_TEXT;
    // } else {
    // view = LayoutInflater.from(context).inflate(
    // isHorizontal ? R.layout.custom_toast_imageview_horizontal : R.layout.custom_toast_imageview_vertical, null);
    // mCurToastType = isHorizontal ? TOAST_TYPE.HORIZONTAL_IMAGEVIEW : TOAST_TYPE.VERTICAL_IMAGEVIEW;
    // }
    // ((TextView) view.findViewById(android.R.id.message)).setText(msg);
    // ImageView toastImage = (ImageView) view.findViewById(R.id.fyzb_toast_image);
    // if (null != toastImage) {
    // if (null == imageRes) {
    // toastImage.setVisibility(View.GONE);
    // } else {
    // toastImage.setVisibility(View.VISIBLE);
    // toastImage.setImageDrawable(imageRes);
    // }
    // }
    //
    // if (mToast == null) {
    // mToast = new Toast(context);
    // mToast.setView(view);
    // mToast.setDuration(Toast.LENGTH_SHORT);
    // mToast.setGravity(gravity, 0, 0);
    // } else {
    // mToast.setView(view);
    // mToast.setDuration(Toast.LENGTH_SHORT);
    // mToast.setGravity(gravity, 0, 0);
    // }
    // }
    //
    // public static void showToast(Context context, String msg, Drawable imageRes, int gravity, boolean isHorizontal) {
    // if (BasicToolUtil.isActivityAtTop(context)) {
    // initToast(context, msg, imageRes, gravity, isHorizontal);
    // mToast.show();
    // }
    // }
    //
    // public static void showToast(Context context, String text) {
    // if (BasicToolUtil.isActivityAtTop(context)) {
    // initToast(context, text, null, Gravity.CENTER, true);
    // mToast.show();
    // }
    // }
    //
    // public static void showToast(Context context, int textId) {
    // if (BasicToolUtil.isActivityAtTop(context)) {
    // initToast(context, context.getString(textId), null, Gravity.CENTER, true);
    // mToast.show();
    // }
    // }
    //
    // public static void showToast(Context context, String msg, Drawable imageRes) {
    // if (BasicToolUtil.isActivityAtTop(context)) {
    // initToast(context, "", imageRes, Gravity.CENTER, true);
    // mToast.show();
    // }
    // }

    private static AlertDialog notNetworkDialog;

    /*
     * 网络不通
     */
    public static void showNotNetworkDialog(final Context context, int iconId) {
        if (!BasicToolUtil.isActivityAtTop(context)) {
            return;
        }
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setIcon(iconId).setTitle(R.string.network_unreachable_title).setMessage(R.string.network_setting_msg)
                    .setPositiveButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (BasicToolUtil.isFastClick(context)) {
                                return;
                            }
                            if (null != dialog) {
                                dialog.dismiss();
                            }
                        }
                    }).setNegativeButton(R.string.button_setting_network, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (BasicToolUtil.isFastClick(context)) {
                                return;
                            }
                            try {
                                context.startActivity(new Intent(Settings.ACTION_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            } catch (Exception e) {
                                // LogOut.trace(e.getMessage());
                            }
                            if (null != dialog) {
                                dialog.dismiss();
                            }
                        }
                    }).setCancelable(false);
            if (null != notNetworkDialog && notNetworkDialog.isShowing()) {
                try {
                    notNetworkDialog.dismiss();
                } catch (Exception e) {
                }
            }
            notNetworkDialog = builder.create();
            notNetworkDialog.getWindow().setGravity(Gravity.CENTER);
            notNetworkDialog.show();
        }
    }

    /**
     * 普通dialog
     * 
     * @param context
     * @param title
     * @param message
     * @param posMessage
     * @param negMessage
     */
    private static AlertDialog sigleDialog;

    public static void showActionDialog(final Context context, String title, String message, String posMessage, final Runnable posHandler,
            String negMessage, final Runnable negHandler) {
        if (!BasicToolUtil.isActivityAtTop(context)) {
            return;
        }
        AlertDialog.Builder builder;
        // if (Build.VERSION.SDK_INT > AndroidUtil.VERSION_CODES.HONEYCOMB) {
        // builder = new AlertDialog.Builder(context, R.style.Theme_Fyzb_AlertDialog);
        // } else {
        builder = new AlertDialog.Builder(context);
        // }
        // builder.setIcon(R.drawable.ic_launcher_logo);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        if (!StringUtils.isEmpty(posMessage)) {
            builder.setPositiveButton(posMessage, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (BasicToolUtil.isFastClick(context)) {
                        return;
                    }
                    try {
                        if (null != posHandler) {
                            posHandler.run();
                        }
                    } catch (Exception e) {
                    }
                    if (null != dialog) {
                        dialog.dismiss();
                    }
                }
            });
        }
        if (!StringUtils.isEmpty(negMessage)) {
            builder.setNegativeButton(negMessage, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (BasicToolUtil.isFastClick(context)) {
                        return;
                    }
                    try {
                        if (null != negHandler) {
                            negHandler.run();
                        }
                    } catch (Exception e) {
                    }
                    if (null != dialog) {
                        dialog.dismiss();
                    }
                }
            });
        }
        if (null != sigleDialog && sigleDialog.isShowing()) {
            sigleDialog.dismiss();
        }

        sigleDialog = builder.create();
        sigleDialog.getWindow().setGravity(Gravity.CENTER);
        sigleDialog.show();
    }

}
