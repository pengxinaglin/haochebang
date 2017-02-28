package com.haoche51.checker.entity;

/**
 * 验车悬浮窗数据实体
 * Created by wufx on 2016/2/24.
 */
public class CheckPopupEntity {
  /**
   * 日期
   */
  private int time;
  /**
   * 上车数量
   */
  private int online;
  /**
   * 售出数量
   */
  private int sold;
  /**
   * 回购数量
   */
  private int buy_back;
  /**
   * 售出比
   */
  private String sold_rate;

  public int getTime() {
    return time;
  }

  public void setTime(int time) {
    this.time = time;
  }

  public int getOnline() {
    return online;
  }

  public void setOnline(int online) {
    this.online = online;
  }

  public int getSold() {
    return sold;
  }

  public void setSold(int sold) {
    this.sold = sold;
  }

  public int getBuy_back() {
    return buy_back;
  }

  public void setBuy_back(int buy_back) {
    this.buy_back = buy_back;
  }

  public String getSold_rate() {
    return sold_rate;
  }

  public void setSold_rate(String sold_rate) {
    this.sold_rate = sold_rate;
  }
}
