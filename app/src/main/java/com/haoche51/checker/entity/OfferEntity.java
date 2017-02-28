package com.haoche51.checker.entity;

/**
 * 报价
 * Created by wfx on 2016/8/9.
 */
public class OfferEntity {
    /**
     * 报价 单位 “万元"
     */
    private float price;

    /**
     * 车源标题
     */
    private String title;

    /**
     * 车商还是个人
     */
    private String agent;

    /**
     * 上牌时间
     */
    private String plate_time;

    /**
     * 行驶里程， 单位 "万公里"
     */
    private float mile;

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getPlate_time() {
        return plate_time;
    }

    public void setPlate_time(String plate_time) {
        this.plate_time = plate_time;
    }

    public float getMile() {
        return mile;
    }

    public void setMile(float mile) {
        this.mile = mile;
    }
}
