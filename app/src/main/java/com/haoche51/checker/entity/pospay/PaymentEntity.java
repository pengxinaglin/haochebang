package com.haoche51.checker.entity.pospay;

import java.io.Serializable;

/**
 * Created by yangming on 2016/1/14.
 */
public class PaymentEntity implements Serializable {

  private String order_number;
  private Integer good_type;
  private String good_desc;
  private String price;         //price
  private String fee;           //手续费
  private String text_desc;     //过户展示的名字
  private String by_pos;        //是否通过pos转账 1：是；0：否

  public String getOrder_number() {
    return order_number;
  }

  public void setOrder_number(String order_number) {
    this.order_number = order_number;
  }

  public Integer getGood_type() {
    return good_type;
  }

  public void setGood_type(Integer good_type) {
    this.good_type = good_type;
  }

  public String getGood_desc() {
    return good_desc;
  }

  public void setGood_desc(String good_desc) {
    this.good_desc = good_desc;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public String getFee() {
    return fee;
  }

  public void setFee(String fee) {
    this.fee = fee;
  }

  public String getText_desc() {
    return text_desc;
  }

  public void setText_desc(String text_desc) {
    this.text_desc = text_desc;
  }

  public String getBy_pos() {
    return by_pos;
  }

  public void setBy_pos(String by_pos) {
    this.by_pos = by_pos;
  }
}
