package com.meijialife.dingdang.bean;

import java.io.Serializable;

import net.tsz.afinal.annotation.sqlite.Table;

/**
 * 用户数据（登陆接口）
 */
@Table(name = "User")
public class User implements Serializable {

    /** 用户ID **/
    public String id;

    /** 手机号 **/
    public String mobile;

    /**  **/
    public String third_type;

    /**  **/
    public String open_id;

    /**  **/
    public String province_name;

    /**  **/
    public String name;

    /**  **/
    public String sex;

    /**  **/
    public String head_img;

    /**  **/
    public String rest_money;

    /**  **/
    public int score;

    /**  **/
    public String user_type;

    /**  **/
    public int is_approval;

    /**  **/
    public int add_from;

    /**  **/
    public long add_time;

    /**  **/
    public long update_time;

    public String openid;
    public String real_name;
    public String id_card;
    public String birth_day;
    public String degree_id;
    public String major;
    public String app_id;
    public String channel_id;
    public String app_user_id;
    public String client_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getThird_type() {
        return third_type;
    }

    public void setThird_type(String third_type) {
        this.third_type = third_type;
    }

    public String getOpen_id() {
        return open_id;
    }

    public void setOpen_id(String open_id) {
        this.open_id = open_id;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public String getRest_money() {
        return rest_money;
    }

    public void setRest_money(String rest_money) {
        this.rest_money = rest_money;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public int getIs_approval() {
        return is_approval;
    }

    public void setIs_approval(int is_approval) {
        this.is_approval = is_approval;
    }

    public int getAdd_from() {
        return add_from;
    }

    public void setAdd_from(int add_from) {
        this.add_from = add_from;
    }

    public long getAdd_time() {
        return add_time;
    }

    public void setAdd_time(long add_time) {
        this.add_time = add_time;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getApp_user_id() {
        return app_user_id;
    }

    public void setApp_user_id(String app_user_id) {
        this.app_user_id = app_user_id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getBirth_day() {
        return birth_day;
    }

    public void setBirth_day(String birth_day) {
        this.birth_day = birth_day;
    }

    public String getDegree_id() {
        return degree_id;
    }

    public void setDegree_id(String degree_id) {
        this.degree_id = degree_id;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    
}
