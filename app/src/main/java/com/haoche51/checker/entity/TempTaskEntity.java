package com.haoche51.checker.entity;

/**
 * 评估师临时任务
 * Created by wufx on 2016/2/25.
 */
public class TempTaskEntity {
  /**
   * 空闲起始时间
   */
  private int idleStartTime;

  /**
   * 空闲结束时间
   */
  private int idleEndTime;

  /**
   * 当前位置关键词
   */
  private String key;

  /**
   * 当前城市
   */
  private String city;

  /**
   * 位置=城市+key
   */
  private String location;

  /**
   * 纬度
   */
  private float latitude;

  /**
   * 经度
   */
  private float longitude;

  public int getIdleStartTime() {
    return idleStartTime;
  }

  public void setIdleStartTime(int idleStartTime) {
    this.idleStartTime = idleStartTime;
  }

  public int getIdleEndTime() {
    return idleEndTime;
  }

  public void setIdleEndTime(int idleEndTime) {
    this.idleEndTime = idleEndTime;
  }

  public float getLatitude() {
    return latitude;
  }

  public void setLatitude(float latitude) {
    this.latitude = latitude;
  }

  public float getLongitude() {
    return longitude;
  }

  public void setLongitude(float longitude) {
    this.longitude = longitude;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }
}
