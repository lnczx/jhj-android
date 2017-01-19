package com.meijialife.dingdang.picker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;


import com.meijialife.dingdang.R;

import java.util.ArrayList;

/**
 * 图片选择器
 * Created by andye on 16/3/17.
 */
public class MultiSelector {

    public static final String EXTRA_RESULT = MultiImageSelectorActivity.EXTRA_RESULT;
    private static MultiSelector sSelector;
    private boolean mShowCamera = true;
    private int mMaxCount = 9;
    private int mMode = MultiImageSelectorActivity.MODE_MULTI;
    private ArrayList<String> mOriginData;
    private boolean isRecord = Boolean.FALSE;

    private MultiSelector() {
    }

    public static MultiSelector create() {
        if (sSelector == null) {
            sSelector = new MultiSelector();
        }
        return sSelector;
    }

    public MultiSelector showCamera(boolean show) {
        mShowCamera = show;
        return sSelector;
    }

    public MultiSelector isRecord(boolean record) {
        isRecord = record;
        return sSelector;
    }

    public MultiSelector count(int count) {
        mMaxCount = count;
        return sSelector;
    }

    public MultiSelector single() {
        mMode = MultiImageSelectorActivity.MODE_SINGLE;
        return sSelector;
    }

    public MultiSelector multi() {
        mMode = MultiImageSelectorActivity.MODE_MULTI;
        return sSelector;
    }

    public MultiSelector origin(ArrayList<String> images) {
        mOriginData = images;
        return sSelector;
    }

    public void start(Activity activity, int requestCode) {
        if (hasPermission(activity)) {
            activity.startActivityForResult(createIntent(activity), requestCode);
        } else {
            Toast.makeText(activity, R.string.mis_error_no_permission, Toast.LENGTH_SHORT).show();
        }
    }

    public void start(Fragment fragment, int requestCode) {
        final Context context = fragment.getContext();
        if (hasPermission(context)) {
            fragment.startActivityForResult(createIntent(context), requestCode);
        } else {
            Toast.makeText(context, R.string.mis_error_no_permission, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasPermission(Context context) {
        // Permission was added in API Level 16
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN || ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private Intent createIntent(Context context) {
        Intent intent = null;
        if (!isRecord) {
            intent = new Intent(context, MultiImageSelectorActivity.class);
        }
        isRecord = false;
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, mMaxCount);
        if (mOriginData != null) {
            intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mOriginData);
        }
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, mMode);
        return intent;
    }
}