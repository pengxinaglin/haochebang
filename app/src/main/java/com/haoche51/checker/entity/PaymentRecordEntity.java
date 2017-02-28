package com.haoche51.checker.entity;

/**
 * 付款记录实体类
 * Created by wfx on 2016/6/14.
 */
public class PaymentRecordEntity {
    /**
     * 收车任务id
     */
    private int task_id;

    /**
     * 状态
     * 1：付款申请中 2：付款被拒绝 3：财务已付款
     */
    private int status;

    /**
     * 状态文本值
     */
    private String status_text;

    /**
     * 付款金额，单位元
     */
    private int price;

    /**
     * 付款事由，例如“车款”、“介绍费”
     */
    private String price_type;

    /**
     * 付款账号
     */
    private String account_num;

    /**
     * 付款账户名
     */
    private String account_user;

    /**
     * 付款开户行名称
     */
    private String account_bank;

    /**
     * 拒绝原因
     */
    private String fail_reason;

    /**
     * 付款时间
     */
    private long pay_time;

    public String getStatus_text() {
        return status_text;
    }

    public void setStatus_text(String status_text) {
        this.status_text = status_text;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPrice_type() {
        return price_type;
    }

    public void setPrice_type(String price_type) {
        this.price_type = price_type;
    }

    public String getAccount_num() {
        return account_num;
    }

    public void setAccount_num(String account_num) {
        this.account_num = account_num;
    }

    public String getAccount_user() {
        return account_user;
    }

    public void setAccount_user(String account_user) {
        this.account_user = account_user;
    }

    public String getAccount_bank() {
        return account_bank;
    }

    public void setAccount_bank(String account_bank) {
        this.account_bank = account_bank;
    }

    public String getFail_reason() {
        return fail_reason;
    }

    public void setFail_reason(String fail_reason) {
        this.fail_reason = fail_reason;
    }


    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public long getPay_time() {
        return pay_time;
    }

    public void setPay_time(long pay_time) {
        this.pay_time = pay_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
