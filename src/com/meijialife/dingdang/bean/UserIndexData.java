package com.meijialife.dingdang.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 用户数据（个人主页接口）
 *
 */
public class UserIndexData implements Serializable {
    
	private int staff_id;//
	private int staff_type;//员工类型 0 = 全职 1 = 兼职
	private int status;//员工状态 0 = 不可用 1 = 可用, 员工总开关
	private String name;//
	private int sex;//
	private String mobile;//
	private String head_img; 
	private int auth_status; //身份认证 0 = 未认证 1 = 认证
	private int total_order; //本月订单
	private double total_incoming;// 本月收入
	private ArrayList<String> skills;//技能
    public int getStaff_id() {
        return staff_id;
    }
    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }
    public int getStaff_type() {
        return staff_type;
    }
    public void setStaff_type(int staff_type) {
        this.staff_type = staff_type;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getSex() {
        return sex;
    }
    public void setSex(int sex) {
        this.sex = sex;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getHead_img() {
        return head_img;
    }
    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }
    public int getAuth_status() {
        return auth_status;
    }
    public void setAuth_status(int auth_status) {
        this.auth_status = auth_status;
    }
    public int getTotal_order() {
        return total_order;
    }
    public void setTotal_order(int total_order) {
        this.total_order = total_order;
    }
   
    public double getTotal_incoming() {
		return total_incoming;
	}
	public void setTotal_incoming(double total_incoming) {
		this.total_incoming = total_incoming;
	}
    public ArrayList<String> getSkills() {
        return skills;
    }
    public void setSkills(ArrayList<String> skills) {
        this.skills = skills;
    }
    @Override
    public String toString() {
        return "UserIndexData [staff_id=" + staff_id + ", staff_type=" + staff_type + ", status=" + status + ", name=" + name + ", sex=" + sex
                + ", mobile=" + mobile + ", head_img=" + head_img + ", auth_status=" + auth_status + ", total_order=" + total_order
                + ", total_incoming=" + total_incoming + ", skills=" + skills + "]";
    }
}
