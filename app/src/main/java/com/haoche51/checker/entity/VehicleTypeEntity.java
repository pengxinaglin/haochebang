package com.haoche51.checker.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 车型信息实体类
 * Created by wufx on 2016/1/23.
 */
public class VehicleTypeEntity implements Parcelable {
  /**
   * 车型id
   */
  private int id;
  /**
   * 车型名称
   */
  private String name;
  /**
   * 该车型对应的车的全称
   */
  private String fullname;
  /**
   * 年份
   */
  private String year;

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

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

  public String getFullname() {
    return fullname;
  }

  public void setFullname(String fullname) {
    this.fullname = fullname;
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
    dest.writeInt(id);
    dest.writeString(name);
    dest.writeString(fullname);
    dest.writeString(year);
  }

  public static final Creator<VehicleTypeEntity> CREATOR = new Creator<VehicleTypeEntity>() {

    @Override
    public VehicleTypeEntity createFromParcel(Parcel source) {
      //实现从source中创建出类的实例的功能
      VehicleTypeEntity sourceEntity = new VehicleTypeEntity();
      sourceEntity.id = source.readInt();
      sourceEntity.name = source.readString();
      sourceEntity.fullname = source.readString();
      sourceEntity.year = source.readString();
      return sourceEntity;
    }

    @Override
    public VehicleTypeEntity[] newArray(int size) {
      return new VehicleTypeEntity[size];
    }
  };
}
