package com.meijialife.dingdang.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.meijialife.dingdang.Alerm.AlermDialog;
import com.meijialife.dingdang.Constants;
import com.meijialife.dingdang.R;
import com.meijialife.dingdang.adapter.PhotoPagerAdapter;
import com.meijialife.dingdang.ui.FixedViewPager;
import com.meijialife.dingdang.utils.FileUtil;
import com.meijialife.dingdang.utils.Utils;
import com.meijialife.dingdang.utils.image.FrescoUtil;

import java.io.File;
import java.util.ArrayList;

import static com.meijialife.dingdang.picker.MultiSelector.EXTRA_RESULT;


/**
 * 图片预览页
 */
public class PhotoPreviewActivity extends Activity implements View.OnClickListener {

    private FixedViewPager mPhotoViewPager;
    private PhotoPagerAdapter mPhotoPagerAdapter;

    //top
    private RelativeLayout mLayoutTop;
    private ImageView mIvBack, mIvAction;

    //bottom
    private RelativeLayout mLayoutBottom;
    private TextView mTvIndex;//当前位置
    private TextView mTvCount;//总数
    private ImageView mIvSave;

    private ArrayList<String> mList;
    private String videoPath;//视频地址
    private int currentPosition = 0;//当前位置
    private boolean isLocal = Boolean.FALSE;//默认false为普通预览,true为可取消选择的本地图片(来自:发动态-图片预览)
    private boolean isVideo = Boolean.FALSE;//true为视频预览
    private boolean isCache = Boolean.FALSE;//true为发动态后的本地缓存图片预览

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);


        //extra
        mList = getIntent().getStringArrayListExtra("data");
        currentPosition = getIntent().getIntExtra("position", 0);
        isLocal = getIntent().getBooleanExtra("isLocal", false);
        isVideo = getIntent().getBooleanExtra("isVideo", false);
        if (isVideo) {
            videoPath = getIntent().getStringExtra("videoPath");
        }
        isCache = getIntent().getBooleanExtra("isCache", false);


        if (!isLocal) {
            findViewById(R.id.layout_top).setVisibility(View.GONE);
        } else {
            mLayoutTop = (RelativeLayout) findViewById(R.id.layout_top);
            mIvBack = (ImageView) findViewById(R.id.iv_back);
            mIvAction = (ImageView) findViewById(R.id.iv_action);

            if (!isVideo) {
                ((TextView) findViewById(R.id.tv_title)).setText("图片预览");
            } else {
                ((TextView) findViewById(R.id.tv_title)).setText("视频预览");
            }

        }

        mPhotoViewPager = (FixedViewPager) findViewById(R.id.viewpager);
        mLayoutBottom = (RelativeLayout) findViewById(R.id.layout_bottom);
        mTvIndex = (TextView) findViewById(R.id.tv_index);
        mTvCount = (TextView) findViewById(R.id.tv_count);
        if (!isLocal) {
            mIvSave = (ImageView) findViewById(R.id.iv_save);
            mIvSave.setVisibility(View.VISIBLE);
        }


        mPhotoPagerAdapter = new PhotoPagerAdapter(PhotoPreviewActivity.this, isLocal, isVideo, isCache);
        mPhotoViewPager.setAdapter(mPhotoPagerAdapter);
        mPhotoPagerAdapter.updateDatas(mList);

        mPhotoViewPager.setCurrentItem(currentPosition);
        mTvCount.setText(String.valueOf(mList.size()));
        updateTitle();

        mPhotoViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                updateTitle();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        if (!isLocal) {
            mIvSave.setOnClickListener(this);
        } else {
            mIvBack.setOnClickListener(this);
            mIvAction.setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_save:
                savePhoto();
                break;
            case R.id.iv_back:
                exit();
                break;
            case R.id.iv_action:
//                    delCurrent();
                showDelCurrentDialog();
                break;
            default:
                break;
        }
    }

    /**
     * 更新序号
     */
    private void updateTitle() {
        mTvIndex.setText(String.valueOf(currentPosition + 1));
    }

    /**
     * 保存到手机
     */
    private void savePhoto() {
        //create file
        String folder = Environment.getExternalStorageDirectory().getPath() + Constants.FOLDER_NAME;
        if (!FileUtil.isFolderExist(folder)) {
            FileUtil.makeDirs(folder);
        }
        File toFile = new File(folder, System.currentTimeMillis() + ".jpg");

        //save
        File file = FrescoUtil.getCachedImageOnDisk(mList.get(currentPosition));//首先检查本地是否存在图片的缓存文件
        if (null != file) {
            FileUtil.savePhoto(file, toFile);//直接保存
        } else {
            FrescoUtil.saveBitmap(PhotoPreviewActivity.this, mList.get(currentPosition), toFile);
        }

        //通知本地相册更新
        MediaScannerConnection.scanFile(this, new String[]{toFile.toString()}, null, null);
    }


    /**
     * 显隐状态栏
     */
    public void toggleStatusBar() {
        if (mLayoutTop.isShown()) {
            mLayoutTop.setVisibility(View.GONE);
            mLayoutBottom.setVisibility(View.GONE);
        } else {
            mLayoutTop.setVisibility(View.VISIBLE);
            mLayoutBottom.setVisibility(View.VISIBLE);
        }
    }

    private void showDelCurrentDialog() {
        //弹出dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否删除该图片?"); //设置内容
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
                delCurrent();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 删除单张
     */
    private void delCurrent() {


        mList.remove(currentPosition);//remove
        if (!mList.isEmpty()) {
            //update viewpager
            mPhotoPagerAdapter = new PhotoPagerAdapter(PhotoPreviewActivity.this, isLocal, isVideo, isCache);
            mPhotoViewPager.setAdapter(mPhotoPagerAdapter);
            mPhotoPagerAdapter.updateDatas(mList);

            // 更新index
            if (currentPosition == mList.size()) {
                currentPosition--;
            }
            mPhotoViewPager.setCurrentItem(currentPosition);
            updateTitle();
            mTvCount.setText(String.valueOf(mList.size()));
        } else {//返回
            exit();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            exit();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 返回
     */
    public void exit() {
        mPhotoPagerAdapter.release();
        if (isLocal) {
            Intent data = new Intent();
            data.putStringArrayListExtra(EXTRA_RESULT, mList);
            setResult(RESULT_OK, data);
        }
        finish();
        overridePendingTransition(0, R.anim.fade_out_quick);
    }
}