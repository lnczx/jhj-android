package com.meijialife.dingdang.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 交易明细
 * 
 * @author windows7
 * 
 */
public class OrderPayDetail implements Serializable {


    private Long staff_id;
    private String mobile;
    private String order_type_name;
    private String order_pay;
    private String add_time_str;
    public Long getStaff_id() {
        return staff_id;
    }
    public void setStaff_id(Long staff_id) {
        this.staff_id = staff_id;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getOrder_type_name() {
        return order_type_name;
    }
    public void setOrder_type_name(String order_type_name) {
        this.order_type_name = order_type_name;
    }
    public String getOrder_pay() {
        return order_pay;
    }
    public void setOrder_pay(String order_pay) {
        this.order_pay = order_pay;
    }
    public String getAdd_time_str() {
        return add_time_str;
    }
    public void setAdd_time_str(String add_time_str) {
        this.add_time_str = add_time_str;
    }


    
}
