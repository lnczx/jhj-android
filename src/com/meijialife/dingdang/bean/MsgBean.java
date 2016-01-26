package com.meijialife.dingdang.bean;

import java.io.Serializable;

import android.R.integer;

import net.tsz.afinal.annotation.sqlite.Table;

/**
 * 消息
 */
public class MsgBean implements Serializable {

    private int msg_id;
    private String title;
    private String summary;// 摘要
    private String goto_url;// 跳转url
    private String add_time_str;

    public int getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getGoto_url() {
        return goto_url;
    }

    public void setGoto_url(String goto_url) {
        this.goto_url = goto_url;
    }

    public String getAdd_time_str() {
        return add_time_str;
    }

    public void setAdd_time_str(String add_time_str) {
        this.add_time_str = add_time_str;
    }

}
