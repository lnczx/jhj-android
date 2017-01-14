package com.meijialife.dingdang.bean;

import java.io.Serializable;

import net.tsz.afinal.annotation.sqlite.Table;

/**
 * 用户数据（用户详情接口）
 */
@Table(name = "UserInfo")
public class UserInfo implements Serializable {

    /** 用户ID **/
    public String id;
    
    /** 手机号 **/
    private String mobile;
    
    /** 第三方登陆类型 qq/weixin/weibo **/
    private String third_type;
    
    /** 第三方登陆openid **/
    private String open_id;
    
    /**  **/
    private String province_name;
    
    /** 称呼 **/
    private String name;
    
    /**  **/
    private String sex;
    
    /** 头像url **/
    private String head_img;
    
    /**  **/
    private String rest_money;
    
    /**  **/
    private int score;
    
    /** 用户类型 0 = 普通用户 1= 秘书 2 = 服务商 **/
    private String user_type;
    
    /**  **/
    private int is_approval;
    
    /** 注册来源 **/
    private int add_from;
    
    /**  **/
    private long add_time;
    
    /**  **/
    private long update_time;
    
    /**  **/
    private String user_id;
    
    /** 私秘卡有效期,返回值如 "有效期:2015-02-24至2015-03-24" **/
    private String senior_range;
    
    /** 是否有真人管家服务 1=是   0=否（用机器人管家） **/
    private String is_senior;
    
    /**  **/
    private String sec_id;
    
    /** 秘书Im账号 **/
    private String im_sec_username;
    
    /** 秘书Im昵称 **/
    private String im_sec_nickname;
    
    /** 即时通讯账号（环信） **/
    private String im_username;
    
    /** 机器人管家IM账号 **/
    private String im_robot_username;
    
    /** 机器人管家IM昵称 **/
    private String im_robot_nickname;
    
    /** 即时通讯密码（环信） **/
    private String im_password;
    /** 百度云 **/
    private String client_id;
    /**是否为某个公司职员0=否 1=是**/
    private int has_company;
    
    
    public int getHas_company() {
        return has_company;
    }

    public void setHas_company(int has_company) {
        this.has_company = has_company;
    }

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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSenior_range() {
        return senior_range;
    }

    public void setSenior_range(String senior_range) {
        this.senior_range = senior_range;
    }

    public String getIs_senior() {
        return is_senior;
    }

    public void setIs_senior(String is_senior) {
        this.is_senior = is_senior;
    }

    public String getSec_id() {
        return sec_id;
    }

    public void setSec_id(String sec_id) {
        this.sec_id = sec_id;
    }

    public String getIm_sec_username() {
        return im_sec_username;
    }

    public void setIm_sec_username(String im_sec_username) {
        this.im_sec_username = im_sec_username;
    }

    public String getIm_sec_nickname() {
        return im_sec_nickname;
    }

    public void setIm_sec_nickname(String im_sec_nickname) {
        this.im_sec_nickname = im_sec_nickname;
    }

    public String getIm_username() {
        return im_username;
    }

    public void setIm_username(String im_username) {
        this.im_username = im_username;
    }

    public String getIm_robot_username() {
        return im_robot_username;
    }

    public void setIm_robot_username(String im_robot_username) {
        this.im_robot_username = im_robot_username;
    }

    public String getIm_robot_nickname() {
        return im_robot_nickname;
    }

    public void setIm_robot_nickname(String im_robot_nickname) {
        this.im_robot_nickname = im_robot_nickname;
    }

    public String getIm_password() {
        return im_password;
    }

    public void setIm_password(String im_password) {
        this.im_password = im_password;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }


}
