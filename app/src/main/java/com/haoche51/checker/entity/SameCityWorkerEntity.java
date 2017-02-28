package com.haoche51.checker.entity;

/**
 * Created by mac on 16/1/19.
 */
public class SameCityWorkerEntity {

  private int id;//10, //地收的crm用户id
  private String name;//'王五', //地收的crm用户姓名
  private String first_char;//评估师姓名拼音字母 用于排序
  private boolean isChoose;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFirst_char() {
    return first_char;
  }

  public void setFirst_char(String first_char) {
    this.first_char = first_char;
  }

  public boolean isChoose() {
    return isChoose;
  }

  public void setChoose(boolean choose) {
    isChoose = choose;
  }
}
