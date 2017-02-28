package com.haoche51.checker.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 车源信息实体类
 * Created by wufx on 2016/1/23.
 */
public class VehicleSourceEntity implements Parcelable {
  /**
   * 品牌id
   */
  private int brand_id;
  /**
   * 品牌名称
   */
  private String brand_name;
  /**
   * 车系id
   */
  private int series_id;
  /**
   * 车系名称
   */
  private String series_name;
  /**
   * 年份
   */
  private String year;
  /**
   * 车型id
   */
  private int model_id;
  /**
   * 车型名称
   */
  private String model_name;

  /**
   * 完整名称
   */
  private String full_name;

  /**
   * 跳转来源
   */
  private String jump_source;

  public int getBrand_id() {
    return brand_id;
  }

  public void setBrand_id(int brand_id) {
    this.brand_id = brand_id;
  }

  public String getBrand_name() {
    return brand_name;
  }

  public void setBrand_name(String brand_name) {
    this.brand_name = brand_name;
  }

  public int getSeries_id() {
    return series_id;
  }

  public void setSeries_id(int series_id) {
    this.series_id = series_id;
  }

  public String getSeries_name() {
    return series_name;
  }

  public void setSeries_name(String series_name) {
    this.series_name = series_name;
  }

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public int getModel_id() {
    return model_id;
  }

  public void setModel_id(int model_id) {
    this.model_id = model_id;
  }

  public String getModel_name() {
    return model_name;
  }

  public void setModel_name(String model_name) {
    this.model_name = model_name;
  }

  public String getFull_name() {
    return full_name;
  }

  public void setFull_name(String full_name) {
    this.full_name = full_name;
  }

  public String getJump_source() {
    return jump_source;
  }

  public void setJump_source(String jump_source) {
    this.jump_source = jump_source;
  }

  /**
   * 内容描述接口，基本不用管，默认返回0就可以了
   *
   * @return
   */
  @Override
  public int describeContents() {
    return 0;
  }

  /**
   * 写入接口函数，打包
   *
   * @param dest
   * @param flags
   */
  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(brand_id);
    dest.writeString(brand_name);
    dest.writeInt(series_id);
    dest.writeString(series_name);
    dest.writeString(year);
    dest.writeInt(model_id);
    dest.writeString(model_name);
    dest.writeString(full_name);
    dest.writeString(jump_source);
  }

  public static final Parcelable.Creator<VehicleSourceEntity> CREATOR = new Creator<VehicleSourceEntity>() {

    @Override
    public VehicleSourceEntity createFromParcel(Parcel source) {
      //实现从source中创建出类的实例的功能
      VehicleSourceEntity sourceEntity = new VehicleSourceEntity();
      sourceEntity.brand_id = source.readInt();
      sourceEntity.brand_name = source.readString();
      sourceEntity.series_id = source.readInt();
      sourceEntity.series_name = source.readString();
      sourceEntity.year = source.readString();
      sourceEntity.model_id = source.readInt();
      sourceEntity.model_name = source.readString();
      sourceEntity.full_name = source.readString();
      sourceEntity.jump_source = source.readString();
      return sourceEntity;
    }

    @Override
    public VehicleSourceEntity[] newArray(int size) {
      return new VehicleSourceEntity[size];
    }


  };
}
