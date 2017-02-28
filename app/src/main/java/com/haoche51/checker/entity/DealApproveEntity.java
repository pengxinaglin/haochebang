package com.haoche51.checker.entity;

/**
 * 交易实体类
 * Created by wfx on 2016/7/27.
 */
public class DealApproveEntity {
    /**
     * 订单id
     */
    private String trans_id;
    /**
     * 订单id "YD003857"
     */
    private String order_id;

    /**
     * 审核状态描述
     */
    private String audit_desc;
    /**
     * 是否是我的审核: 0不是（显示黑色） 1是（显示红色）
     */
    private String my_audit;
    /**
     * 车源名称
     */
    private String vehicle_name;
    /**
     * 品牌车系
     */
    private String class_name;
    /**
     * 车源url
     */
    private String url;

    public String getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(String trans_id) {
        this.trans_id = trans_id;
    }

    public String getAudit_desc() {
        return audit_desc;
    }

    public void setAudit_desc(String audit_desc) {
        this.audit_desc = audit_desc;
    }

    public String getMy_audit() {
        return my_audit;
    }

    public void setMy_audit(String my_audit) {
        this.my_audit = my_audit;
    }

    public String getVehicle_name() {
        return vehicle_name;
    }

    public void setVehicle_name(String vehicle_name) {
        this.vehicle_name = vehicle_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }
}
