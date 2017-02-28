package com.haoche51.checker.entity;

/**
 * Created by mac on 16/1/25.
 */
public class OfflineShortEntity {

  private int stock_id;// 1, //库存id
  private String task_num;// SC1009, //任务编号
  private String status_text;// 待回款, //状态文本显示
  private String title;// 宝马 x5, //车源
  private String plate_number;// 京A23344, //车牌号
  private String plate_time;// 2011年1月, //上牌时间
  private double mile;// 12.6, //行驶里程，单位“万公里”
  private long price;// 100000, //报价，单位“元”
  private long sold_price;// 50000, //售价，单位“元”
  private String head_url;// http://xxxxxxxxxxx, //车源封面图
  private int is_self;//0否 1是 （是否是本人）

  /**
   * 库存中列表显示的标签文本内容
   */
  private String tag;

  public String getHead_url() {
    return head_url;
  }

  public void setHead_url(String head_url) {
    this.head_url = head_url;
  }

  public double getMile() {
    return mile;
  }

  public void setMile(double mile) {
    this.mile = mile;
  }

  public String getPlate_number() {
    return plate_number;
  }

  public void setPlate_number(String plate_number) {
    this.plate_number = plate_number;
  }

  public String getPlate_time() {
    return plate_time;
  }

  public void setPlate_time(String plate_time) {
    this.plate_time = plate_time;
  }

  public long getPrice() {
    return price;
  }

  public void setPrice(long price) {
    this.price = price;
  }

  public long getSold_price() {
    return sold_price;
  }

  public void setSold_price(long sold_price) {
    this.sold_price = sold_price;
  }

  public String getStatus_text() {
    return status_text;
  }

  public void setStatus_text(String status_text) {
    this.status_text = status_text;
  }

  public int getStock_id() {
    return stock_id;
  }

  public void setStock_id(int stock_id) {
    this.stock_id = stock_id;
  }

  public String getTask_num() {
    return task_num;
  }

  public void setTask_num(String task_num) {
    this.task_num = task_num;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public int getIs_self() {
    return is_self;
  }

  public void setIs_self(int is_self) {
    this.is_self = is_self;
  }
}
