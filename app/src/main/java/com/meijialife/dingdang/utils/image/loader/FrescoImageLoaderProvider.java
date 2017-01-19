package com.meijialife.dingdang.utils.image.loader;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Fresco加载
 * Created by jiangjianxiong on 16/6/7.
 */
class FrescoImageLoaderProvider extends BaseImageLoaderProvider {
    @Override
    public void loadImage(Context ctx, ImageLoader loader) {
        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(ctx.getResources())
                .setFadeDuration(loader.getFadeDuration());
        if (loader.getPlaceHolder() != 0) {//占位图
            builder.setPlaceholderImage(loader.getPlaceHolder());
        }


        GenericDraweeHierarchy hierarchy = builder.build();
        if (loader.getWidth() != -1) {//缩放原图尺寸至 loader.getWidth(), loader.getHeight()
            PipelineDraweeController pipelineDraweeController = (PipelineDraweeController)
                    Fresco.newDraweeControllerBuilder()
                            .setImageRequest(ImageRequestBuilder.newBuilderWithSource(getUri(loader))
                                    .setResizeOptions(new ResizeOptions(loader.getWidth(), loader.getHeight()))
                                    .setAutoRotateEnabled(true)
                                    .build())
                            .setOldController(loader.getImageView().getController())
                            .build();
            pipelineDraweeController.setHierarchy(hierarchy);
            loader.getImageView().setController(pipelineDraweeController);
        } else {//未指定控件尺寸
            DraweeController draweeController = Fresco.newDraweeControllerBuilder().setUri(getUri(loader))
                    .setOldController(loader.getImageView().getController()).build();
            draweeController.setHierarchy(hierarchy);
            loader.getImageView().setController(draweeController);
        }
    }

    /**
     * 筛选图片Uri
     */
    private Uri getUri(ImageLoader loader) {
        if (!TextUtils.isEmpty(loader.getUrl()) && (loader.getUrl().startsWith("http://")
                ||loader.getUrl().startsWith("https://"))) {//网络图片
            return Uri.parse(loader.getUrl());
        } else if (loader.getDrawable() != 0) {//资源图片
            return Uri.parse("res://com.ovie.videochat/" + loader.getDrawable());
        } else if (!TextUtils.isEmpty(loader.getPath())) {//本地图片
            return Uri.parse("file://" + loader.getPath());
        } else if (loader.getPlaceHolder() != 0) {//占位图
            return Uri.parse("res://com.ovie.videochat/" + loader.getPlaceHolder());
        }  else {
            return Uri.parse("");
        }
    }
}