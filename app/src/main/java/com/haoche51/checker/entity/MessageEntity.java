package com.haoche51.checker.entity;

public class MessageEntity extends BaseEntity {

    private int id;
    private int type;//' 0 验车任务，1 看车任务
    private int status;// '0' COMMENT '0,未读，1 已读',
    private String title; //'消息title'
    private String description; //COMMENT '消息内容',
    //COMMENT '自定义数据 例如json {“task_id”:xxx,“stock_id”:xxx,“is_care”:0否1是(是否关注度消息)}
    private String custom_content;
    private String check_user_id; //'crm 用户id',
    private int create_time; //'创建时间',

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustom_content() {
        return custom_content;
    }

    public void setCustom_content(String custom_content) {
        this.custom_content = custom_content;
    }

    public String getCheck_user_id() {
        return check_user_id;
    }

    public void setCheck_user_id(String check_user_id) {
        this.check_user_id = check_user_id;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

}
