package com.haoche51.checker;

public class Checker {
  private int id = 0;//id
  private String username = "";//用户名
  private String name = "";//真实姓名
  private int group = -1;
  private String phone = "";
  private String app_token = "";
  private int city;//城市id
  private String city_name;//城市名称

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getGroup() {
    return group;
  }

  public void setGroup(int group) {
    this.group = group;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getApp_token() {
    return app_token;
  }

  public void setApp_token(String app_token) {
    this.app_token = app_token;
  }

  public int getCity() {
    return city;
  }

  public void setCity(int city) {
    this.city = city;
  }

  public String getCity_name() {
    return city_name;
  }

  public void setCity_name(String city_name) {
    this.city_name = city_name;
  }
}
