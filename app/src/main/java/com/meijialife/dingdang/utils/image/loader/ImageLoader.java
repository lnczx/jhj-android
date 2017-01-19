package com.meijialife.dingdang.utils.image.loader;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * 图片加载配置
 * Created by jiangjianxiong on 16/6/7.
 */
public class ImageLoader {

    private String url;//网络图片地址
    private int res;//资源图片
    private String path;//本地内存卡图片地址
    private int placeHolder;//占位图
    private int width, height;//控件宽高
    private int fadeDuration;//渐现时间ms
    private SimpleDraweeView mImageView;

    private ImageLoader(Builder builder) {
        this.url = builder.url;
        this.res = builder.res;
        this.path = builder.path;
        this.placeHolder = builder.placeHolder;
        this.mImageView = builder.mImageView;
        this.width = builder.width;
        this.height = builder.height;
        this.fadeDuration = builder.fadeDuration;
    }



    String getUrl() {
        return url;
    }

    public int getDrawable() {
        return res;
    }

    String getPath() {
        return path;
    }

    int getPlaceHolder() {
        return placeHolder;
    }

    SimpleDraweeView getImageView() {
        return mImageView;
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    int getFadeDuration() {
        return fadeDuration;
    }

    public static class Builder {
        private String url;
        private int res;
        private String path;
        private int placeHolder;
        private int width, height;
        private int fadeDuration;
        private SimpleDraweeView mImageView;

        public Builder() {//init
            this.url = "";
            this.res = 0;
            this.path = "";
            this.placeHolder = 0;
            this.mImageView = null;
            this.width = -1;
            this.height = -1;
            this.fadeDuration = 150;
        }


        public Builder url(String url) {
            if (null != url) {
                this.url = url;
            }
            return this;
        }

        public Builder drawable(int res) {
            if (res != 0) {
                this.res = res;
            }
            return this;
        }

        public Builder path(String path) {
            if (null != path) {
                this.path = path;
            }
            return this;
        }

        public Builder placeHolder(int placeHolder) {
            if (placeHolder != 0) {
                this.placeHolder = placeHolder;
            }
            return this;
        }

        public Builder mImageView(SimpleDraweeView mImageView) {
            this.mImageView = mImageView;
            return this;
        }

        public Builder widthAndHeight(int width) {
            if (width != 0) {
                this.width = width;
                this.height = width;//正方形
            }
            return this;
        }

        public Builder widthAndHeight(int width, int height) {
            if (width != 0 && height != 0) {
                this.width = width;
                this.height = height;
            }
            return this;
        }

        public Builder fadeDuration(int fadeDuration) {
            this.fadeDuration = fadeDuration;
            return this;
        }

        public ImageLoader build() {
            return new ImageLoader(this);
        }

    }
}