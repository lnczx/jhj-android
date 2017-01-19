package com.meijialife.dingdang.utils.image.loader;

import android.content.Context;
import android.widget.TextView;

/**
 * 图片加载类
 * Created by jiangjianxiong on 16/6/7.
 */
public class ImageLoaderUtil {

    private static ImageLoaderUtil mInstance;
    private BaseImageLoaderProvider mProvider;

    private ImageLoaderUtil() {
        mProvider = new FrescoImageLoaderProvider();
    }

    public static ImageLoaderUtil getInstance() {
        if (mInstance == null) {
            synchronized (ImageLoaderUtil.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoaderUtil();
                    return mInstance;
                }
            }
        }
        return mInstance;
    }

    public TextView loadImage(Context ctx, ImageLoader loader) {
        if (null != loader.getImageView()) {
            mProvider.loadImage(ctx, loader);
        }
        return null;
    }
}