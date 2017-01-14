package com.meijialife.dingdang.bean;

import java.io.Serializable;

/**
 * 用户评价
 *  andye
 */
public class UserRateListData implements Serializable {


    /**
     * id : 7
     * order_id : 45
     * order_no : 797783806621777920
     * staff_id : 1
     * user_id : 3
     * mobile : 186*****665
     * rate_arrival : 1
     * rate_attitude : 2
     * rate_skill : 4
     * rate_content : 服务一般，拖时间
     * add_time : 1479215565
     * user_type_str : 普通会员
     * add_time_str : 2016-11-15
     */

    private String id;
    private String order_id;
    private String order_no;
    private String staff_id;
    private String user_id;
    private String mobile;
    private float  rate_arrival;
    private float  rate_attitude;
    private float  rate_skill;
    private String rate_content;
    private long add_time;
    private String user_type_str;
    private String add_time_str;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getOrder_id() {
        return order_id;
    }
    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
    public String getOrder_no() {
        return order_no;
    }
    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }
    public String getStaff_id() {
        return staff_id;
    }
    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }
    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public float  getRate_arrival() {
        return rate_arrival;
    }
    public void setRate_arrival(int rate_arrival) {
        this.rate_arrival = rate_arrival;
    }
    public float  getRate_attitude() {
        return rate_attitude;
    }
    public void setRate_attitude(int rate_attitude) {
        this.rate_attitude = rate_attitude;
    }
    public float  getRate_skill() {
        return rate_skill;
    }
    public void setRate_skill(int rate_skill) {
        this.rate_skill = rate_skill;
    }
    public String getRate_content() {
        return rate_content;
    }
    public void setRate_content(String rate_content) {
        this.rate_content = rate_content;
    }
    public long getAdd_time() {
        return add_time;
    }
    public void setAdd_time(long add_time) {
        this.add_time = add_time;
    }
    public String getUser_type_str() {
        return user_type_str;
    }
    public void setUser_type_str(String user_type_str) {
        this.user_type_str = user_type_str;
    }
    public String getAdd_time_str() {
        return add_time_str;
    }
    public void setAdd_time_str(String add_time_str) {
        this.add_time_str = add_time_str;
    }
    
    
    
	
	
}
