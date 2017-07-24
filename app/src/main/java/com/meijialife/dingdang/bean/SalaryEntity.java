package com.meijialife.dingdang.bean;

import java.io.Serializable;

/**
 * 账户明细相关
 */
public class SalaryEntity implements Serializable {


    /**
     * name : 营业收入
     * value : 1340.00
     */

    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
