package com.meijialife.dingdang.picker;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;


import com.meijialife.dingdang.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Multi image selector
 * Created by andye  on 2015/4/7.
 */
public class MultiImageSelectorActivity extends AppCompatActivity
        implements MultiImageSelectorFragment.Callback {

    // Single choice
    static final int MODE_SINGLE = 0;
    // Multi choice
    static final int MODE_MULTI = 1;

    /**
     * Max image size，int，{@link #DEFAULT_IMAGE_SIZE} by default
     */
    static final String EXTRA_SELECT_COUNT = "max_select_count";
    /**
     * Select mode，{@link #MODE_MULTI} by default
     */
    static final String EXTRA_SELECT_MODE = "select_count_mode";
    /**
     * Whether show camera，true by default
     */
    static final String EXTRA_SHOW_CAMERA = "show_camera";
    /**
     * Result data set，ArrayList&lt;String&gt;
     */
    static final String EXTRA_RESULT = "select_result";
    /**
     * Original data set
     */
    static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";
    // Default image size
    private static final int DEFAULT_IMAGE_SIZE = 9;

    private ArrayList<String> resultList = new ArrayList<>();
    private int mDefaultCount = DEFAULT_IMAGE_SIZE;

    private MultiImageSelectorFragment mFragment = null;

    private boolean isFirstReload = Boolean.TRUE;//是否第一次加载

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.MIS_NO_ACTIONBAR);
        setContentView(R.layout.mis_activity_default);

        ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= 14) {
            parentView.setFitsSystemWindows(true);
        }
//        SystemBarHelper.tintStatusBar(this, ContextCompat.getColor(this, R.color.purple_title));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        final Intent intent = getIntent();
        mDefaultCount = intent.getIntExtra(EXTRA_SELECT_COUNT, DEFAULT_IMAGE_SIZE);
        final int mode = intent.getIntExtra(EXTRA_SELECT_MODE, MODE_MULTI);
        final boolean isShow = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, true);
        if (mode == MODE_MULTI && intent.hasExtra(EXTRA_DEFAULT_SELECTED_LIST)) {
            resultList = intent.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
        }

        updateDoneText(resultList);

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT, mDefaultCount);
            bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_MODE, mode);
            bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, isShow);
            bundle.putStringArrayList(MultiImageSelectorFragment.EXTRA_DEFAULT_SELECTED_LIST, resultList);
            mFragment = (MultiImageSelectorFragment) Fragment.instantiate(this, MultiImageSelectorFragment.class.getName(), bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.image_grid, mFragment)
                    .commit();
        }

        //back
        findViewById(R.id.left_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    /**
     * Update done button by select image data
     *
     * @param resultList selected image data
     */
    private void updateDoneText(ArrayList<String> resultList) {
        int size = 0;
        if (resultList == null || resultList.size() <= 0) {
            this.resultList.clear();
            if (null != mFragment && mFragment.isAdded()) {
                mFragment.setEnable(false);
            }
        } else {
            size = resultList.size();
            if (null != mFragment && mFragment.isAdded()) {
                mFragment.setEnable(true);
            }
        }
        if (null != mFragment && mFragment.isAdded()) {
            mFragment.setCount(size);
        }
    }

    /**
     * 确认选择图片
     */
    public void onChoose() {
        if (resultList != null && resultList.size() > 0) {
            Intent data = new Intent();
            data.putStringArrayListExtra(EXTRA_RESULT, resultList);
            setResult(RESULT_OK, data);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    @Override
    public void onSingleImageSelected(String path) {
        Intent data = new Intent();
        resultList.add(path);
        data.putStringArrayListExtra(EXTRA_RESULT, resultList);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onImageSelected(String path) {
        if (!resultList.contains(path)) {
            resultList.add(path);
        }
        updateDoneText(resultList);
    }

    @Override
    public void onImageUnselected(String path) {
        if (resultList.contains(path)) {
            resultList.remove(path);
        }
        updateDoneText(resultList);
    }

    @Override
    public void onCameraShot(File imageFile) {
        if (imageFile != null) {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imageFile)));

            Intent data = new Intent();
            resultList.add(imageFile.getAbsolutePath());
            data.putStringArrayListExtra(EXTRA_RESULT, resultList);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void onPhotoChanged(ArrayList<String> resultList) {
        updateDoneText(resultList);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isFirstReload) {
            if (null != mFragment && mFragment.isAdded()) {
                mFragment.reloadImage();
            }
        } else {
            isFirstReload = false;
        }
    }
}