package com.haoche51.checker.entity;

import java.util.List;

/**
 * Created by yangming on 2015/11/19.
 */
public class LiYangDistinctEntity {

  private String key;
  private String name;
  private List<String> value;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getValue() {
    return value;
  }

  public void setValue(List<String> value) {
    this.value = value;
  }
}
