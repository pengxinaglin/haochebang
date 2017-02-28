package com.haoche51.checker.entity.push;

/**
 * Created by xuhaibo on 15/9/14.
 */
public class PushTask {
    private int task_id;
    private int msg_type = -1; // 11 审批成功   12 审批失败
    private String phone;

    /**
     * 库存id
     */
    private int stock_id;
    /**
     * 是否是关注度消息
     * 0否 1是
     */
    private int is_care;


    public int getStock_id() {
        return stock_id;
    }

    public void setStock_id(int stock_id) {
        this.stock_id = stock_id;
    }

    public int getIs_care() {
        return is_care;
    }

    public void setIs_care(int is_care) {
        this.is_care = is_care;
    }

    public int getTask_id() {

        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public int getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
