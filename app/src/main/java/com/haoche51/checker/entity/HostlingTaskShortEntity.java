package com.haoche51.checker.entity;

/**
 * Created by mac on 16/1/19.
 */
public class HostlingTaskShortEntity {

  private int stock_id;//1, //库存id
  private int repair_id; //10, //整备id
  private String title; //福特 福克斯 2012款 三厢 1.6L, //任务名称
  private String head_url; //http://xxxxxxx, //封面图片url
  private String task_num; //SC12345, //任务编号
  private String plate_number; //京A2345, //车牌号
  private long expect_price; //1000, //预计费用，单位“元”
  private long real_price; //2000, //实际费用，单位“元”
  private String pick_up_time; //2016-10-11, //提车时间
  private int repair_status;//整备状态值
  private String repair_status_text;//整备状态文本值
  private String repair_over_time;// 2016-11-11, //整备完成时间

  public long getExpect_price() {
    return expect_price;
  }

  public void setExpect_price(long expect_price) {
    this.expect_price = expect_price;
  }

  public String getHead_url() {
    return head_url;
  }

  public void setHead_url(String head_url) {
    this.head_url = head_url;
  }

  public String getPick_up_time() {
    return pick_up_time;
  }

  public void setPick_up_time(String pick_up_time) {
    this.pick_up_time = pick_up_time;
  }

  public String getPlate_number() {
    return plate_number;
  }

  public void setPlate_number(String plate_number) {
    this.plate_number = plate_number;
  }

  public long getReal_price() {
    return real_price;
  }

  public void setReal_price(long real_price) {
    this.real_price = real_price;
  }

  public int getRepair_id() {
    return repair_id;
  }

  public void setRepair_id(int repair_id) {
    this.repair_id = repair_id;
  }

  public String getRepair_over_time() {
    return repair_over_time;
  }

  public void setRepair_over_time(String repair_over_time) {
    this.repair_over_time = repair_over_time;
  }

  public int getRepair_status() {
    return repair_status;
  }

  public void setRepair_status(int repair_status) {
    this.repair_status = repair_status;
  }

  public String getRepair_status_text() {
    return repair_status_text;
  }

  public void setRepair_status_text(String repair_status_text) {
    this.repair_status_text = repair_status_text;
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
}
