package com.haoche51.checker.entity.transaction;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 渠寄车源实体类
 * Created by haoche51 on 2016/10/22.
 */
public class ChannelVehicleSourceEntity implements Parcelable{

    /**
     * 商家id
     */
    private int dealer_id;

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
    private int class_id;

    /**
     * 车系名称
     */
    private String class_name;

    /**
     * 年份
     */
    private String year;

    /**
     * 车型id
     */
    private int vehicle_id;

    /**
     * 车型名称
     */
    private String vehicle_name;

    /**
     * 车主姓名
     */
    private String seller_name;

    /**
     * 车主联系电话
     */
    private String seller_phone;

    /**
     * 预约验车时间戳
     */
    private String appoint_time;

    /**
     * 预约验车地点
     */
    private String appoint_place;

    /**
     * 地点纬度
     */
    private double place_lat;

    /**
     * 地点经度
     */
    private double place_lng;
    /**
     * vin码
     */
    private String vin_code;

    public ChannelVehicleSourceEntity(){}


    protected ChannelVehicleSourceEntity(Parcel in) {
        dealer_id = in.readInt();
        brand_id = in.readInt();
        brand_name = in.readString();
        class_id = in.readInt();
        class_name = in.readString();
        year = in.readString();
        vehicle_id = in.readInt();
        vehicle_name = in.readString();
        seller_name = in.readString();
        seller_phone = in.readString();
        appoint_time = in.readString();
        appoint_place = in.readString();
        place_lat = in.readDouble();
        place_lng = in.readDouble();
        vin_code = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dealer_id);
        dest.writeInt(brand_id);
        dest.writeString(brand_name);
        dest.writeInt(class_id);
        dest.writeString(class_name);
        dest.writeString(year);
        dest.writeInt(vehicle_id);
        dest.writeString(vehicle_name);
        dest.writeString(seller_name);
        dest.writeString(seller_phone);
        dest.writeString(appoint_time);
        dest.writeString(appoint_place);
        dest.writeDouble(place_lat);
        dest.writeDouble(place_lng);
        dest.writeString(vin_code);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChannelVehicleSourceEntity> CREATOR = new Creator<ChannelVehicleSourceEntity>() {
        @Override
        public ChannelVehicleSourceEntity createFromParcel(Parcel in) {
            return new ChannelVehicleSourceEntity(in);
        }

        @Override
        public ChannelVehicleSourceEntity[] newArray(int size) {
            return new ChannelVehicleSourceEntity[size];
        }
    };

    public int getDealer_id() {
        return dealer_id;
    }

    public void setDealer_id(int dealer_id) {
        this.dealer_id = dealer_id;
    }

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

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getSeller_phone() {
        return seller_phone;
    }

    public void setSeller_phone(String seller_phone) {
        this.seller_phone = seller_phone;
    }

    public String getAppoint_time() {
        return appoint_time;
    }

    public void setAppoint_time(String appoint_time) {
        this.appoint_time = appoint_time;
    }

    public String getAppoint_place() {
        return appoint_place;
    }

    public void setAppoint_place(String appoint_place) {
        this.appoint_place = appoint_place;
    }

    public double getPlace_lat() {
        return place_lat;
    }

    public void setPlace_lat(double place_lat) {
        this.place_lat = place_lat;
    }

    public double getPlace_lng() {
        return place_lng;
    }

    public void setPlace_lng(double place_lng) {
        this.place_lng = place_lng;
    }

    public int getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(int vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getVehicle_name() {
        return vehicle_name;
    }

    public void setVehicle_name(String vehicle_name) {
        this.vehicle_name = vehicle_name;
    }

    public String getVin_code() {
        return vin_code;
    }

    public void setVin_code(String vin_code) {
        this.vin_code = vin_code;
    }
}
