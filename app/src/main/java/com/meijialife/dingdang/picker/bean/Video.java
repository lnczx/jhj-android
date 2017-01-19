package com.meijialife.dingdang.picker.bean;

import android.text.TextUtils;

/**
 * 本地视频实体类
 * Created by jiangjianxiong on 16/7/18.
 */
public class Video {
    public long id;
    public String path;
    public long duration;


    public Video(String path, long duration) {
        this(0, path, duration);
    }

    public Video(long id, String path, long duration) {
        this.id = id;
        this.path = path;
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        try {
            Video other = (Video) o;
            return TextUtils.equals(this.path, other.path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
