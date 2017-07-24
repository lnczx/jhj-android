package com.meijialife.dingdang.bean;

import java.io.Serializable;

/**
 * 账户中心
 */

public class UserAccountBean implements Serializable {


    /**
     * total_dept : 298.0
     * total_cash : 13700.53
     * salary_after_tax : 0.0
     * salary_status : 0
     * salary_status_name : 未发
     * ali_pay_account :
     * ali_pay_lock : 0
     * ali_pay_lock_name : 未确认
     */

    private String total_dept;
    private String total_cash;
    private String salary_after_tax;
    private int salary_status;
    private String salary_status_name;
    private String ali_pay_account;
    private int ali_pay_lock;
    private String ali_pay_lock_name;

    public String getTotal_dept() {
        return total_dept;
    }

    public void setTotal_dept(String total_dept) {
        this.total_dept = total_dept;
    }

    public String getTotal_cash() {
        return total_cash;
    }

    public void setTotal_cash(String total_cash) {
        this.total_cash = total_cash;
    }

    public String getSalary_after_tax() {
        return salary_after_tax;
    }

    public void setSalary_after_tax(String salary_after_tax) {
        this.salary_after_tax = salary_after_tax;
    }

    public int getSalary_status() {
        return salary_status;
    }

    public void setSalary_status(int salary_status) {
        this.salary_status = salary_status;
    }

    public String getSalary_status_name() {
        return salary_status_name;
    }

    public void setSalary_status_name(String salary_status_name) {
        this.salary_status_name = salary_status_name;
    }

    public String getAli_pay_account() {
        return ali_pay_account;
    }

    public void setAli_pay_account(String ali_pay_account) {
        this.ali_pay_account = ali_pay_account;
    }

    public int getAli_pay_lock() {
        return ali_pay_lock;
    }

    public void setAli_pay_lock(int ali_pay_lock) {
        this.ali_pay_lock = ali_pay_lock;
    }

    public String getAli_pay_lock_name() {
        return ali_pay_lock_name;
    }

    public void setAli_pay_lock_name(String ali_pay_lock_name) {
        this.ali_pay_lock_name = ali_pay_lock_name;
    }
}
