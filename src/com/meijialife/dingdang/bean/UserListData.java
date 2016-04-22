package com.meijialife.dingdang.bean;

import java.io.Serializable;

/**
 * @description：客户列表实体
 * @author： kerryg
 * @date:2016年4月21日 
 */
public class UserListData implements Serializable {

	private String staff_id;//员工Id
	
	private String user_id;//用户Id
	
	private String service_times;//服务次数
	
	private String mobile;//用户手机号
	
	private String service_addr;//用户地址

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

	public String getService_times() {
		return service_times;
	}

	public void setService_times(String service_times) {
		this.service_times = service_times;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getService_addr() {
		return service_addr;
	}

	public void setService_addr(String service_addr) {
		this.service_addr = service_addr;
	}
	
	
	
}
