package com.haoche51.checker.entity;

import java.io.Serializable;

/**
 * 买家支付定金自增加款项
 * Created by PengXianglin on 16/3/2.
 */
public class TransactionPospayExtraEntity implements Serializable {
  private String monies;//款项名称
  private double money;//金额
  private int type;//金额

  public double getMoney() {
    return money;
  }

  public void setMoney(double money) {
    this.money = money;
  }

  public String getMonies() {
    return monies;
  }

  public void setMonies(String monies) {
    this.monies = monies;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }
}
