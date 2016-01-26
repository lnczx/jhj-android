package com.meijialife.dingdang.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 交易明细
 * 
 * @author windows7
 * 
 */
public class OrderTixianDetail implements Serializable {

    private Long order_id;

    private String order_no;

    private Long staff_id;

    private String mobile;
    private String account;
    private String remarks;

    private BigDecimal order_money;
    private Short order_status;

    private String add_time_str;

    private String update_time_str;

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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BigDecimal getOrder_money() {
        return order_money;
    }

    public void setOrder_money(BigDecimal order_money) {
        this.order_money = order_money;
    }

    public Short getOrder_status() {
        return order_status;
    }

    public void setOrder_status(Short order_status) {
        this.order_status = order_status;
    }

    public String getAdd_time_str() {
        return add_time_str;
    }

    public void setAdd_time_str(String add_time_str) {
        this.add_time_str = add_time_str;
    }

    public String getUpdate_time_str() {
        return update_time_str;
    }

    public void setUpdate_time_str(String update_time_str) {
        this.update_time_str = update_time_str;
    }

}
