package com.haoche51.checker.entity;

/**
 * 车源
 * Created by wufx on 2016/3/1.
 */
public class CarSourceEntity {

  /**
   * 车源id
   */
  private int id;

  /**
   * 状态文本值:待审核
   */
  private String status_text;

  /**
   * 车源标题
   */
  private String title;

  /**
   * 车源封面图
   */
  private String cover_img;

  /**
   * 报价，单位 万元
   */
  private float price;

  /**
   * 底价，单位 万元
   */
  private float cheap_price;

  /**
   * 更新时间
   */
  private String up_time;

  /**
   * 车商名称
   */
  private String dealer;

  /**
   * 商家地址
   */
  private String address;

  /**
   * 商家联系人姓名
   */
  private String contact_name;

  /**
   * 车商联系人电话
   */
  private String contact_phone;

  /**
   * 商家id
   */
  private int dealer_id;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getStatus_text() {
    return status_text;
  }

  public void setStatus_text(String status_text) {
    this.status_text = status_text;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getCover_img() {
    return cover_img;
  }

  public void setCover_img(String cover_img) {
    this.cover_img = cover_img;
  }

  public float getPrice() {
    return price;
  }

  public void setPrice(float price) {
    this.price = price;
  }

  public float getCheap_price() {
    return cheap_price;
  }

  public void setCheap_price(float cheap_price) {
    this.cheap_price = cheap_price;
  }

  public String getUp_time() {
    return up_time;
  }

  public void setUp_time(String up_time) {
    this.up_time = up_time;
  }

  public String getDealer() {
    return dealer;
  }

  public void setDealer(String dealer) {
    this.dealer = dealer;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getContact_name() {
    return contact_name;
  }

  public void setContact_name(String contact_name) {
    this.contact_name = contact_name;
  }

  public String getContact_phone() {
    return contact_phone;
  }

  public void setContact_phone(String contact_phone) {
    this.contact_phone = contact_phone;
  }

  public int getDealer_id() {
    return dealer_id;
  }

  public void setDealer_id(int dealer_id) {
    this.dealer_id = dealer_id;
  }
}
