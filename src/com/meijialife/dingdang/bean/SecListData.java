package com.meijialife.dingdang.bean;

import java.io.Serializable;

/**
 * @description：助理列表实体
 * @author： kerryg
 * @date:2016年4月21日 
 */
public class SecListData implements Serializable {

	private String name;
	
	private String mobile;
	
	private String head_img;//头像
	
	private String order_num;//服务次数

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getOrder_num() {
		return order_num;
	}

	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}
	
	
}
