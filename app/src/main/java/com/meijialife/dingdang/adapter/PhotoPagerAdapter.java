package com.meijialife.dingdang.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.meijialife.dingdang.R;
import com.meijialife.dingdang.activity.PhotoPreviewActivity;
import com.meijialife.dingdang.ui.FrescoPhotoView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 图片全屏浏览adapter
 * Created by jiangjianxiong on 16/6/12.
 */
public class PhotoPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<String> mList;

    private ArrayList<ImageView> mImageViews;

    private boolean isLocal = Boolean.FALSE;//是否本地图片
    private boolean isVideo = Boolean.FALSE;//是否视频预览
    private boolean isCache = Boolean.FALSE;//默认false,true为发动态后的本地缓存图片预览

    public PhotoPagerAdapter(Context context, boolean isLocal, boolean isVideo, boolean isCache) {
        this.mContext = context;
        this.mList = new ArrayList<>();
        this.mImageViews = new ArrayList<>();
        this.isLocal = isLocal;
        this.isVideo = isVideo;
        this.isCache = isCache;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        if (!isVideo) {//图片预览
            FrescoPhotoView photoView = new FrescoPhotoView(container.getContext());
            if (!isLocal) {
                if (!isCache) {//online
                    photoView.setImageUri(mList.get(position), null);
                } else {//本地缓存
                    photoView.setImageUri("file://" + mList.get(position), null);
                }
            } else {//本地图片
                photoView.setImageUri("file://" + mList.get(position), null);
            }
            container.addView(photoView);
            try{
                mImageViews.add(photoView);
            }catch (Exception e){
            }

            //listener
            photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float v, float v1) {
                    if (!isLocal) {
                        ((PhotoPreviewActivity) mContext).exit();
                    } else {
                        ((PhotoPreviewActivity) mContext).toggleStatusBar();
                    }
                }

                @Override
                public void onOutsidePhotoTap() {
                    if (!isLocal) {
                        ((PhotoPreviewActivity) mContext).exit();
                    } else {
                        ((PhotoPreviewActivity) mContext).toggleStatusBar();
                    }
                }
            });
            return photoView;
        } else {//单视频预览
            RelativeLayout layout = new RelativeLayout(mContext);
            return layout;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void updateDatas(List<String> list) {
        mList.clear();
        if (null != list) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void release() {
        if(null!=mImageViews){
            mImageViews.clear();
            mImageViews = null;
        }
        System.gc();
    }
}