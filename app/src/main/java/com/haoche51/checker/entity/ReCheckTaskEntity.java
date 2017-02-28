package com.haoche51.checker.entity;

/**
 * 复检任务实体类
 * Created by wufx on 2016/1/20.
 */
public class ReCheckTaskEntity {
  /**
   * 复检时间
   */
  private int recheck_time;

  /**
   * 复检地点
   */
  private String recheck_place;
  /**
   * 复检评估师id
   */
  private int recheck_user_id;
  /**
   * 复检评估师名称
   */
  private String recheck_user_name;
  /**
   * 车辆款型
   */
  private String vehicle_type;
  /**
   * 任务id，收车进行中详情页传递过来的
   */
  private int task_id;

  private VehicleSourceEntity vehicleSource;

  /**
   * 车主电话
   */
  private String seller_phone;

  /**
   * 车主姓名
   */
  private String seller_name;

  public int getRecheck_time() {
    return recheck_time;
  }

  public void setRecheck_time(int recheck_time) {
    this.recheck_time = recheck_time;
  }

  public String getRecheck_place() {
    return recheck_place;
  }

  public void setRecheck_place(String recheck_place) {
    this.recheck_place = recheck_place;
  }

  public int getRecheck_user_id() {
    return recheck_user_id;
  }

  public void setRecheck_user_id(int recheck_user_id) {
    this.recheck_user_id = recheck_user_id;
  }

  public String getVehicle_type() {
    return vehicle_type;
  }

  public void setVehicle_type(String vehicle_type) {
    this.vehicle_type = vehicle_type;
  }

  public int getTask_id() {
    return task_id;
  }

  public void setTask_id(int task_id) {
    this.task_id = task_id;
  }

  public VehicleSourceEntity getVehicleSource() {
    return vehicleSource;
  }

  public void setVehicleSource(VehicleSourceEntity vehicleSource) {
    this.vehicleSource = vehicleSource;
  }

  public String getRecheck_user_name() {
    return recheck_user_name;
  }

  public void setRecheck_user_name(String recheck_user_name) {
    this.recheck_user_name = recheck_user_name;
  }

  public String getSeller_phone() {
    return seller_phone;
  }

  public void setSeller_phone(String seller_phone) {
    this.seller_phone = seller_phone;
  }

  public String getSeller_name() {
    return seller_name;
  }

  public void setSeller_name(String seller_name) {
    this.seller_name = seller_name;
  }
}
