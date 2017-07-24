package com.meijialife.dingdang.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

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

    private String service_end_date;

    private String button_word;

    private Short order_status;

    private String order_status_str;

    private String service_addr_lat;

    private String service_addr_lng;

    private String pick_addr_lat;

    private String pick_addr_lng;

    private String service_hour;

    private String remarks;

    private String remarks_confirm;

    private int pay_type;

    private String pay_type_name;

    private String mobile;

    private String order_ratio;// 精度

    private String tel_staff;// 客服电话

    private int user_id;

    private int is_vip;

    private String user_type_str;

    private String over_work_str;

    private String order_from_name;

    public String getService_end_date() {
        return service_end_date;
    }

    public void setService_end_date(String service_end_date) {
        this.service_end_date = service_end_date;
    }

    public String getOrder_from_name() {
        return order_from_name;
    }

    public void setOrder_from_name(String order_from_name) {
        this.order_from_name = order_from_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(int is_vip) {
        this.is_vip = is_vip;
    }

    public String getUser_type_str() {
        return user_type_str;
    }

    public void setUser_type_str(String user_type_str) {
        this.user_type_str = user_type_str;
    }

    public String getOver_work_str() {
        return over_work_str;
    }

    public void setOver_work_str(String over_work_str) {
        this.over_work_str = over_work_str;
    }

    /**
     * id : 47 order_id : 0 order_no : 787274115097559040 user_id : 3 service_addon_id : 34 price : 199 item_unit : 元/小时 item_num : 1 add_time :
     * 1476535750 service_addon_name : 金牌保洁 default_num : 0 service_hour : 3
     */

    private List<ServiceAddonsBean> service_addons;

    public List<ServiceAddonsBean> getService_addons() {
        return service_addons;
    }

    public void setService_addons(List<ServiceAddonsBean> service_addons) {
        this.service_addons = service_addons;
    }

    private List<images> order_imgs;

    public List<images> getOrder_imgs() {
        return order_imgs;
    }

    public void setOrder_imgs(List<images> order_imgs) {
        this.order_imgs = order_imgs;
    }

    public static class images {
        private String img_id;
        private String user_id;
        private String link_id;
        private String link_type;
        private String img_url;
        private String add_time;

        public String getImg_id() {
            return img_id;
        }

        public void setImg_id(String img_id) {
            this.img_id = img_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getLink_id() {
            return link_id;
        }

        public void setLink_id(String link_id) {
            this.link_id = link_id;
        }

        public String getLink_type() {
            return link_type;
        }

        public void setLink_type(String link_type) {
            this.link_type = link_type;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }
    }

    public static class ServiceAddonsBean {
        private int id;
        private int order_id;
        private String order_no;
        private int user_id;
        private int service_addon_id;
        private int price;
        private String item_unit;
        private int item_num;
        private int add_time;
        private String service_addon_name;
        private int default_num;
        private double service_hour;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getService_addon_id() {
            return service_addon_id;
        }

        public void setService_addon_id(int service_addon_id) {
            this.service_addon_id = service_addon_id;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getItem_unit() {
            return item_unit;
        }

        public void setItem_unit(String item_unit) {
            this.item_unit = item_unit;
        }

        public int getItem_num() {
            return item_num;
        }

        public void setItem_num(int item_num) {
            this.item_num = item_num;
        }

        public int getAdd_time() {
            return add_time;
        }

        public void setAdd_time(int add_time) {
            this.add_time = add_time;
        }

        public String getService_addon_name() {
            return service_addon_name;
        }

        public void setService_addon_name(String service_addon_name) {
            this.service_addon_name = service_addon_name;
        }

        public int getDefault_num() {
            return default_num;
        }

        public void setDefault_num(int default_num) {
            this.default_num = default_num;
        }

        public double getService_hour() {
            return service_hour;
        }

        public void setService_hour(double service_hour) {
            this.service_hour = service_hour;
        }
    }

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

    public String getService_hour() {
        return service_hour;
    }

    public void setService_hour(String service_hour) {
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
