package com.haoche51.checker.entity;

/**
 * 回访记录实体类
 * Created by wfx on 2016/7/19.
 */
public class RevisitRecordEntity {
    /**
     * 收车任务id
     */
    private int task_id;

    /**
     * 下次回访时间（1、2、3、5天后）
     */
    private int next_visit;

    /**
     * 车主卖车原因
     */
    private String reason;

    /**
     * 预计卖车时间（手动填写字符串）
     */
    private String sold_time;

    /**
     * 提交回访的人
     */
    private String visit_user;

    /**
     * 提交回访的时间
     */
    private String visit_time;

    /**
     * 卖车决定人
     */
    private String decide_user;

    /**
     * 车主预期售价，单位 “元”
     */
    private int expect_price;

    /**
     * 同行最高报价，单位元
     */
    private int peer_price;

    /**
     * 其他
     */
    private String other;

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public int getNext_visit() {
        return next_visit;
    }

    public void setNext_visit(int next_visit) {
        this.next_visit = next_visit;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSold_time() {
        return sold_time;
    }

    public void setSold_time(String sold_time) {
        this.sold_time = sold_time;
    }

    public String getDecide_user() {
        return decide_user;
    }

    public void setDecide_user(String decide_user) {
        this.decide_user = decide_user;
    }

    public int getExpect_price() {
        return expect_price;
    }

    public void setExpect_price(int expect_price) {
        this.expect_price = expect_price;
    }

    public int getPeer_price() {
        return peer_price;
    }

    public void setPeer_price(int peer_price) {
        this.peer_price = peer_price;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getVisit_user() {
        return visit_user;
    }

    public void setVisit_user(String visit_user) {
        this.visit_user = visit_user;
    }

    public String getVisit_time() {
        return visit_time;
    }

    public void setVisit_time(String visit_time) {
        this.visit_time = visit_time;
    }
}
