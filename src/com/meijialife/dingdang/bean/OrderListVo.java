package com.meijialife.dingdang.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderListVo implements Serializable {

	private Long staff_id;

	private Short order_type;

	private String order_type_name;

	private Long order_id;

	private String order_no;

	private Long service_type_id;

	private String service_type_name;

	private BigDecimal order_money;

	private BigDecimal order_incoming;

	private String service_addr;

	private String service_addr_distance;

	private String pick_addr;

	private String pick_addr_distance;

	private String service_content;

	private String service_date;

	private String button_word;

	private Short order_status;
	
	private String order_status_str;

	private String service_addr_lat;

	private String service_addr_lng;

	private String pick_addr_lat;

	private String pick_addr_lng;

	private Short service_hour;

	private String remarks;
	private String remarks_confirm;
	private int pay_type;
	private String pay_type_name;
	private String mobile;
	private String order_ratio;//精度
	private String tel_staff;//客服电话

	public Long getStaff_id() {
		return staff_id;
	}

	public void setStaff_id(Long staff_id) {
		this.staff_id = staff_id;
	}

	public Short getOrder_type() {
		return order_type;
	}

	public void setOrder_type(Short order_type) {
		this.order_type = order_type;
	}

	public String getOrder_type_name() {
		return order_type_name;
	}

	public void setOrder_type_name(String order_type_name) {
		this.order_type_name = order_type_name;
	}

	public Long getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Long order_id) {
		this.order_id = order_id;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public Long getService_type_id() {
		return service_type_id;
	}

	public void setService_type_id(Long service_type_id) {
		this.service_type_id = service_type_id;
	}

	public String getService_type_name() {
		return service_type_name;
	}

	public void setService_type_name(String service_type_name) {
		this.service_type_name = service_type_name;
	}

	public BigDecimal getOrder_money() {
		return order_money;
	}

	public void setOrder_money(BigDecimal order_money) {
		this.order_money = order_money;
	}

	public BigDecimal getOrder_incoming() {
		return order_incoming;
	}

	public void setOrder_incoming(BigDecimal order_incoming) {
		this.order_incoming = order_incoming;
	}

	public String getService_addr() {
		return service_addr;
	}

	public void setService_addr(String service_addr) {
		this.service_addr = service_addr;
	}

	public String getPick_addr() {
		return pick_addr;
	}

	public void setPick_addr(String pick_addr) {
		this.pick_addr = pick_addr;
	}

	public String getService_addr_distance() {
		return service_addr_distance;
	}

	public void setService_addr_distance(String service_addr_distance) {
		this.service_addr_distance = service_addr_distance;
	}

	public String getPick_addr_distance() {
		return pick_addr_distance;
	}

	public void setPick_addr_distance(String pick_addr_distance) {
		this.pick_addr_distance = pick_addr_distance;
	}

	public String getService_content() {
		return service_content;
	}

	public void setService_content(String service_content) {
		this.service_content = service_content;
	}

	public String getService_date() {
		return service_date;
	}

	public void setService_date(String service_date) {
		this.service_date = service_date;
	}

	public String getButton_word() {
		return button_word;
	}

	public void setButton_word(String button_word) {
		this.button_word = button_word;
	}

	public Short getOrder_status() {
		return order_status;
	}

	public void setOrder_status(Short order_status) {
		this.order_status = order_status;
	}
 

	public String getOrder_status_str() {
        return order_status_str;
    }

    public void setOrder_status_str(String order_status_str) {
        this.order_status_str = order_status_str;
    }

    public String getService_addr_lat() {
        return service_addr_lat;
    }

    public void setService_addr_lat(String service_addr_lat) {
        this.service_addr_lat = service_addr_lat;
    }

    public String getService_addr_lng() {
        return service_addr_lng;
    }

    public void setService_addr_lng(String service_addr_lng) {
        this.service_addr_lng = service_addr_lng;
    }

    public String getPick_addr_lat() {
        return pick_addr_lat;
    }

    public void setPick_addr_lat(String pick_addr_lat) {
        this.pick_addr_lat = pick_addr_lat;
    }

    public String getPick_addr_lng() {
        return pick_addr_lng;
    }

    public void setPick_addr_lng(String pick_addr_lng) {
        this.pick_addr_lng = pick_addr_lng;
    }

    public Short getService_hour() {
		return service_hour;
	}

	public void setService_hour(Short service_hour) {
		this.service_hour = service_hour;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRemarks_confirm() {
		return remarks_confirm;
	}

	public void setRemarks_confirm(String remarks_confirm) {
		this.remarks_confirm = remarks_confirm;
	}

	public String getPay_type_name() {
		return pay_type_name;
	}

	public void setPay_type_name(String pay_type_name) {
		this.pay_type_name = pay_type_name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

    public String getOrder_ratio() {
        return order_ratio;
    }

    public void setOrder_ratio(String order_ratio) {
        this.order_ratio = order_ratio;
    }

    public String getTel_staff() {
        return tel_staff;
    }

    public void setTel_staff(String tel_staff) {
        this.tel_staff = tel_staff;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }
	
	

}
