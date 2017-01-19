package com.meijialife.dingdang.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;


import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Fresco工具类
 * Created by jiangjianxiong on 16/6/12.
 */

public class FrescoUtil {

    /**
     * 获取缓存在手机中的图片
     *
     * @param picUrl 图片网络地址
     * @return 本地缓存文件
     */
    public static File getCachedImageOnDisk(String picUrl) {
        File localFile = null;
        CacheKey cacheKey = getCacheKey(picUrl);
        if (cacheKey != null) {
            if (ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            } else if (ImagePipelineFactory.getInstance().getSmallImageFileCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            }
        }
        return localFile;
    }

    /**
     * 获取图片缓存的Key
     *
     * @param picUrl 图片网络地址
     * @return 图片缓存的CacheKey
     */
    private static CacheKey getCacheKey(String picUrl) {
        return DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(Uri.parse(picUrl)), null);
    }

    /**
     * 获取缓存中的bitmap,保存至本地
     *
     * @param picUrl 图片网络地址
     */
    public static String saveBitmap(Context context, String picUrl, final File toFile) {

        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(picUrl))
                .setProgressiveRenderingEnabled(true)
                .build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(imageRequest, context);

        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(Bitmap bitmap) {
                if (null != bitmap) {

                    try {
                        FileOutputStream fos = new FileOutputStream(toFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(x.app(), "保存成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(x.app(), "保存失败,请重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                Toast.makeText(x.app(), "保存失败,请重试", Toast.LENGTH_SHORT).show();
            }
        }, CallerThreadExecutor.getInstance());
        return "";
    }
}