package com.haoche51.checker.entity;

import android.text.TextUtils;

/**
 * Created by mac on 16/1/19.
 */
public class PurchaseTaskShortEntity {

    private int task_id; // 10, //任务id
    private String task_num; // SC10, //任务编号
    private String title; // '奥迪 A4', //任务名称，即为标题
    private String seller_phone; // '1888888888', //车主电话
    private String seller_name; // '李四', //车主姓名
    private String remark; // '备注', //备注
    private int is_outside; // 0/1, //是否外网。0：否、1：是
    private String status_text; // '状态', //已预约/待付定金 等状态文本值
    private int status; // 1, //状态枚举值
    private String appoint_time; // '1月1日 12：30', //预约时间
    private String appoint_address; // '北京市 海淀区 西二旗 地铁站', //预约地点

    /**
     * 已付收车金额 元
     */
    private String has_pay;

    /**
     * 收购总价(收购价+过户费+介绍费)
     */
    private String total_back_price;

    /**
     * 最后一次支付状态枚举值
     * 1：付款申请中 2：付款被拒绝 3：财务已付款
     */
    private int last_pay_status;

    /**
     * 最后一次申请的支付状态
     * 1：付款申请中 2：付款被拒绝 3：财务已付款
     */
    private String last_pay_status_text;

    /**
     * 任务需要回访
     */
    private String need_visit;

    /**
     * 跳转到详情页的url
     */
    private String url;

    public String getAppoint_address() {
        return appoint_address;
    }

    public void setAppoint_address(String appoint_address) {
        this.appoint_address = appoint_address;
    }

    public String getAppoint_time() {
        return appoint_time;
    }

    public void setAppoint_time(String appoint_time) {
        this.appoint_time = appoint_time;
    }

    public int getIs_outside() {
        return is_outside;
    }

    public void setIs_outside(int is_outside) {
        this.is_outside = is_outside;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getSeller_phone() {
        return seller_phone;
    }

    public void setSeller_phone(String seller_phone) {
        this.seller_phone = seller_phone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatus_text() {
        return status_text;
    }

    public void setStatus_text(String status_text) {
        this.status_text = status_text;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public String getTask_num() {
        return task_num;
    }

    public void setTask_num(String task_num) {
        this.task_num = task_num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLast_pay_status() {
        return last_pay_status;
    }

    public void setLast_pay_status(int last_pay_status) {
        this.last_pay_status = last_pay_status;
    }

    public String getLast_pay_status_text() {
        return last_pay_status_text;
    }

    public void setLast_pay_status_text(String last_pay_status_text) {
        this.last_pay_status_text = last_pay_status_text;
    }

    public String getHas_pay() {
        if(TextUtils.isEmpty(has_pay) || "null".equals(has_pay.trim().toLowerCase())){
            return "0";
        }
        return has_pay;
    }

    public void setHas_pay(String has_pay) {
        this.has_pay = has_pay;
    }

    public String getTotal_back_price() {
        if(TextUtils.isEmpty(total_back_price) || "null".equals(total_back_price.trim().toLowerCase())){
            return "0";
        }
        return total_back_price;
    }

    public void setTotal_back_price(String total_back_price) {
        this.total_back_price = total_back_price;
    }

    public String getNeed_visit() {
        return need_visit;
    }

    public void setNeed_visit(String need_visit) {
        this.need_visit = need_visit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
