package com.haoche51.checker.entity;

import java.util.List;

/**
 * Created by yangming on 2015/11/19.
 */
public class LiYangHttpRequestEntity {
  private List<LiYangDistinctEntity> distinct_key_list;
  private List<LiYangVehicleEntity> model_list;

  public List<LiYangDistinctEntity> getDistinct_key_list() {
    return distinct_key_list;
  }

  public void setDistinct_key_list(List<LiYangDistinctEntity> distinct_key_list) {
    this.distinct_key_list = distinct_key_list;
  }

  public List<LiYangVehicleEntity> getModel_list() {
    return model_list;
  }

  public void setModel_list(List<LiYangVehicleEntity> model_list) {
    this.model_list = model_list;
  }
}
