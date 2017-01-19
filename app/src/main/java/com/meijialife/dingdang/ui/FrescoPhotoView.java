package com.meijialife.dingdang.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.meijialife.dingdang.utils.Utils;

import uk.co.senab.photoview.PhotoView;


/**
 * PhotoView结合Fresco方式加载
 */
public class FrescoPhotoView extends PhotoView {

    private DraweeHolder<GenericDraweeHierarchy> mDraweeHolder;

    public FrescoPhotoView(Context context) {
        this(context, null);
    }

    public FrescoPhotoView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public FrescoPhotoView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        initDraweeHierarchy();
    }

    private void initDraweeHierarchy() {
        if (mDraweeHolder == null) {
            final GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
                    .setProgressBarImage(new LoadingProgressDrawable(getContext())).build();
            mDraweeHolder = DraweeHolder.create(hierarchy, getContext());
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mDraweeHolder.onDetach();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mDraweeHolder.onAttach();
    }

    @Override
    protected boolean verifyDrawable(Drawable dr) {
        super.verifyDrawable(dr);
        return dr == mDraweeHolder.getHierarchy().getTopLevelDrawable();
    }

    @Override
    public void onStartTemporaryDetach() {
        super.onStartTemporaryDetach();
        mDraweeHolder.onDetach();
    }

    @Override
    public void onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach();
        mDraweeHolder.onAttach();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDraweeHolder.onTouchEvent(event) || super.onTouchEvent(event);
    }

    public void setImageUri(String uri, ResizeOptions options) {
        if (null != uri) {
            ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri))
                    .setResizeOptions(options)
                    .setAutoRotateEnabled(true)
                    .build();
            final DataSource<CloseableReference<CloseableImage>> dataSource = Fresco.getImagePipeline().fetchDecodedImage(imageRequest, this);
            AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(mDraweeHolder.getController())
                    .setImageRequest(imageRequest)
                    .setControllerListener(new BaseControllerListener<ImageInfo>() {
                        @Override
                        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                            super.onFinalImageSet(id, imageInfo, animatable);

                            CloseableReference<CloseableImage> imageCloseableReference = null;
                            try {
                                imageCloseableReference = dataSource.getResult();
                                if (imageCloseableReference != null) {
                                    final CloseableImage image = imageCloseableReference.get();
                                    if (image != null && image instanceof CloseableStaticBitmap) {
                                        CloseableStaticBitmap closeableStaticBitmap = (CloseableStaticBitmap) image;
                                        final Bitmap bitmap = closeableStaticBitmap.getUnderlyingBitmap();
                                        if (bitmap != null) {
                                            if (Utils.isBigPicture(bitmap)) {
                                                FrescoPhotoView.this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                                            }
                                            setImageBitmap(bitmap);
                                            setScaleType(ImageView.ScaleType.FIT_CENTER);//大图缩小至屏幕内
                                        }
                                    }
                                }
                            } finally {
                                dataSource.close();
                                CloseableReference.closeSafely(imageCloseableReference);
                            }
                        }
                    })
                    .build();
            mDraweeHolder.setController(controller);
            setImageDrawable(mDraweeHolder.getTopLevelDrawable());
        }
    }

    @Override
    public boolean onFilterTouchEventForSecurity(MotionEvent event) {
        try {
            return super.onFilterTouchEventForSecurity(event);
        } catch (IllegalArgumentException ex) {
        }
        return false;
    }
}