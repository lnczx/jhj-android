package com.meijialife.dingdang.picker.bean;

import android.text.TextUtils;

/**
 * 图片实体
 * Created by Nereo on 2015/4/7.
 * Updated by jiangjianxiong on 16/8/19.
 */
public class Image {
    public String path;
    public String name;
    public long dateAdded;

    public Image(String path, String name, long dateAdded) {
        this.path = path;
        this.name = name;
        this.dateAdded = dateAdded;
    }

    @Override
    public boolean equals(Object o) {
        try {
            Image other = (Image) o;
            return TextUtils.equals(this.path, other.path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}