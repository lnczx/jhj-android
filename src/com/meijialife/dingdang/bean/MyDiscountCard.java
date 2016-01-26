package com.meijialife.dingdang.bean;

import java.io.Serializable;

/**
 * @description：我的--优惠券实体
 * @author： kerryg
 * @date:2015年11月17日 
 */
public class MyDiscountCard implements Serializable{
    
    private Long user_id;//用户Id
    private Long  id;//优惠券Id
    private String value;//优惠券金额
    private String max_value;//订单金额满减基数
    private String to_date;//过期时间，yyyy-MM-dd
    private short is_used;//是否使用 0=未使用 1=已使用
    private String introduction;//一句话描述
    private String description;//详细说明
    private String card_password;//优惠券密码
    private Long service_type_id;//服务类型Id
    private Long service_price_id;//服务报价Id
    private String service_type_name;//服务类型名称
    private String service_price_name;//服务报价名称 0=全部
    public Long getUser_id() {
        return user_id;
    }
    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getMax_value() {
        return max_value;
    }
    public void setMax_value(String max_value) {
        this.max_value = max_value;
    }
    public String getTo_date() {
        return to_date;
    }
    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }
    public short getIs_used() {
        return is_used;
    }
    public void setIs_used(short is_used) {
        this.is_used = is_used;
    }
    public String getIntroduction() {
        return introduction;
    }
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCard_password() {
        return card_password;
    }
    public void setCard_password(String card_password) {
        this.card_password = card_password;
    }
    public Long getService_type_id() {
        return service_type_id;
    }
    public void setService_type_id(Long service_type_id) {
        this.service_type_id = service_type_id;
    }
    public Long getService_price_id() {
        return service_price_id;
    }
    public void setService_price_id(Long service_price_id) {
        this.service_price_id = service_price_id;
    }
    public String getService_type_name() {
        return service_type_name;
    }
    public void setService_type_name(String service_type_name) {
        this.service_type_name = service_type_name;
    }
    public String getService_price_name() {
        return service_price_name;
    }
    public void setService_price_name(String service_price_name) {
        this.service_price_name = service_price_name;
    }
}
