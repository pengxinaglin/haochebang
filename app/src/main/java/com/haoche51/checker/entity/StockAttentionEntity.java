package com.haoche51.checker.entity;

/**
 * 库存关注度
 * Created by wfx on 2016/7/8.
 */
public class StockAttentionEntity {
    /**
     * 库存id
     */
    private int stock_id;

    /**
     * 车源标题
     */
    private String title;

    /**
     * 上线时间 单位“天”
     */
    private int online_time;

    /**
     * 浏览人次
     */
    private int visit;

    /**
     * 来的咨询次数
     */
    private int phone_ask;

    /**
     * 上门看车次数
     */
    private int look_cn;

    /**
     * 报价 元
     */
    private int seller_price;

    /**
     * 底价 元
     */
    private int cheap_price;

    /**
     * 收购价 元
     */
    private int back_price;

    /**
     * 新报价 元
     */
    private int new_seller_price;

    /**
     * 新底价 元
     */
    private int new_cheap_price;

    /**
     * 0否 1是 （是否是本人）
     */
    private int is_self;

    public int getStock_id() {
        return stock_id;
    }

    public void setStock_id(int stock_id) {
        this.stock_id = stock_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOnline_time() {
        return online_time;
    }

    public void setOnline_time(int online_time) {
        this.online_time = online_time;
    }

    public int getVisit() {
        return visit;
    }

    public void setVisit(int visit) {
        this.visit = visit;
    }

    public int getPhone_ask() {
        return phone_ask;
    }

    public void setPhone_ask(int phone_ask) {
        this.phone_ask = phone_ask;
    }

    public int getLook_cn() {
        return look_cn;
    }

    public void setLook_cn(int look_cn) {
        this.look_cn = look_cn;
    }

    public int getSeller_price() {
        return seller_price;
    }

    public void setSeller_price(int seller_price) {
        this.seller_price = seller_price;
    }

    public int getCheap_price() {
        return cheap_price;
    }

    public void setCheap_price(int cheap_price) {
        this.cheap_price = cheap_price;
    }

    public int getBack_price() {
        return back_price;
    }

    public void setBack_price(int back_price) {
        this.back_price = back_price;
    }

    public int getNew_seller_price() {
        return new_seller_price;
    }

    public void setNew_seller_price(int new_seller_price) {
        this.new_seller_price = new_seller_price;
    }

    public int getNew_cheap_price() {
        return new_cheap_price;
    }

    public void setNew_cheap_price(int new_cheap_price) {
        this.new_cheap_price = new_cheap_price;
    }

    public int getIs_self() {
        return is_self;
    }

    public void setIs_self(int is_self) {
        this.is_self = is_self;
    }
}
