package com.haoche51.checker.entity;

/**
 * 没有获取到支付成功回调的时候，缓存支付信息
 * Created by yangming on 2016/2/26.
 */
public class PosPayOrderEntity extends BaseEntity {

  private int id;
  private String outside_goodsorder;  //商家订单号
  private String price;               //金额
  private String fee = "0";                 //服务费
  private String user_login;          //操作员
  private String device_number = "";       //pos机编号
  private String sign;                //签名
  private String oid_trader;          //商户号
  private String upload_status;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getOutside_goodsorder() {
    return outside_goodsorder;
  }

  public void setOutside_goodsorder(String outside_goodsorder) {
    this.outside_goodsorder = outside_goodsorder;
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

  public String getUser_login() {
    return user_login;
  }

  public void setUser_login(String user_login) {
    this.user_login = user_login;
  }

  public String getDevice_number() {
    return device_number;
  }

  public void setDevice_number(String device_number) {
    this.device_number = device_number;
  }

  public String getSign() {
    return sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

  public String getOid_trader() {
    return oid_trader;
  }

  public void setOid_trader(String oid_trader) {
    this.oid_trader = oid_trader;
  }

  public String getUpload_status() {
    return upload_status;
  }

  public void setUpload_status(String upload_status) {
    this.upload_status = upload_status;
  }
}
