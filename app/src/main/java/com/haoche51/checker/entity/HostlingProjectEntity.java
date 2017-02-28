package com.haoche51.checker.entity;

import java.util.List;

/**
 * 整备项目实体类
 * Created by wufx on 16/1/30.
 */
public class HostlingProjectEntity {

  /**
   * 库存id
   */
  private int stock_id;
  /**
   * 整备id
   */
  private int repair_id;
  /**
   * 整备项目编号
   */
  private int project_number;
  /**
   * 整备项目名称
   */
  private String project_name;
  /**
   * 是否完成整备（0：否、1：是)
   */
  private int isFinish;
  /**
   * 整备后照片
   */
  private List<PhotoEntity> afterPhotoList;
  /**
   * 整备金额
   */
  private int hostlingMoney;

  public int getStock_id() {
    return stock_id;
  }

  public void setStock_id(int stock_id) {
    this.stock_id = stock_id;
  }

  public int getRepair_id() {
    return repair_id;
  }

  public void setRepair_id(int repair_id) {
    this.repair_id = repair_id;
  }

  public int getProject_number() {
    return project_number;
  }

  public void setProject_number(int project_number) {
    this.project_number = project_number;
  }

  public String getProject_name() {
    return project_name;
  }

  public void setProject_name(String project_name) {
    this.project_name = project_name;
  }


  public List<PhotoEntity> getAfterPhotoList() {
    return afterPhotoList;
  }

  public void setAfterPhotoList(List<PhotoEntity> afterPhotoList) {
    this.afterPhotoList = afterPhotoList;
  }

  public int getHostlingMoney() {
    return hostlingMoney;
  }

  public void setHostlingMoney(int hostlingMoney) {
    this.hostlingMoney = hostlingMoney;
  }

  public int getIsFinish() {
    return isFinish;
  }

  public void setIsFinish(int isFinish) {
    this.isFinish = isFinish;
  }
}
