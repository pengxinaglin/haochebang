package com.haoche51.checker.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 报价参考
 * Created by wfx on 2016/8/8.
 */
public class OfferReferEntity implements Parcelable{
    /**
     * 城市id
     */
    private int city_id;

    /**
     * 品牌id
     */
    private int brand_id;

    /**
     * 车系id
     */
    private int class_id;

    /**
     * 车型id
     */
    private int model_id;

    /**
     * 车辆年款
     */
    private String vehicle_year;

    /**
     * 车源
     */
    private String vehicle_source;

    /**
     * 上牌时间（时间戳）
     */
    private long plate_time;

    /**
     * 表显里程（万公里）
     */
    private float mile;

    /**
     * 近期成交价区间，如：20-30万元
     */
    private String deal_price;

    /**
     * 最新市场报价区间，如：30-40万元
     */
    private String market_price;

    /**
     * 均价，如：4.5 ， 单位“万元”
     */
    private float average_price;

    /**
     * 新车成交价区间，如：30-40万元
     */
    private String new_car_price;

    /**
     * 最低配车价， 如：9.8， 单位“万元”
     */
    private float low_match_price;

    /**
     * 排序（1：价格从高到低、2：价格从低到高、3：上牌时间降序、4：上牌时间升序）
     */
    private int order;

    /**
     * 来源（1:不限、不传默认为不限 、2：58、3：汽车之家(二手车之家)、4：淘车(易车)
     */
    private int source;

    public OfferReferEntity(){}


    protected OfferReferEntity(Parcel in) {
        city_id = in.readInt();
        brand_id = in.readInt();
        class_id = in.readInt();
        model_id = in.readInt();
        vehicle_year = in.readString();
        vehicle_source = in.readString();
        plate_time = in.readLong();
        mile = in.readFloat();
        deal_price = in.readString();
        market_price = in.readString();
        average_price = in.readFloat();
        new_car_price = in.readString();
        low_match_price = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(city_id);
        dest.writeInt(brand_id);
        dest.writeInt(class_id);
        dest.writeInt(model_id);
        dest.writeString(vehicle_year);
        dest.writeString(vehicle_source);
        dest.writeLong(plate_time);
        dest.writeFloat(mile);
        dest.writeString(deal_price);
        dest.writeString(market_price);
        dest.writeFloat(average_price);
        dest.writeString(new_car_price);
        dest.writeFloat(low_match_price);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OfferReferEntity> CREATOR = new Creator<OfferReferEntity>() {
        @Override
        public OfferReferEntity createFromParcel(Parcel in) {
            return new OfferReferEntity(in);
        }

        @Override
        public OfferReferEntity[] newArray(int size) {
            return new OfferReferEntity[size];
        }
    };

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public int getModel_id() {
        return model_id;
    }

    public void setModel_id(int model_id) {
        this.model_id = model_id;
    }

    public String getVehicle_year() {
        return vehicle_year;
    }

    public void setVehicle_year(String vehicle_year) {
        this.vehicle_year = vehicle_year;
    }

    public long getPlate_time() {
        return plate_time;
    }

    public void setPlate_time(long plate_time) {
        this.plate_time = plate_time;
    }

    public float getMile() {
        return mile;
    }

    public void setMile(float mile) {
        this.mile = mile;
    }

    public String getDeal_price() {
        return deal_price;
    }

    public void setDeal_price(String deal_price) {
        this.deal_price = deal_price;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public float getAverage_price() {
        return average_price;
    }

    public void setAverage_price(float average_price) {
        this.average_price = average_price;
    }

    public String getNew_car_price() {
        return new_car_price;
    }

    public void setNew_car_price(String new_car_price) {
        this.new_car_price = new_car_price;
    }

    public float getLow_match_price() {
        return low_match_price;
    }

    public void setLow_match_price(float low_match_price) {
        this.low_match_price = low_match_price;
    }

    public String getVehicle_source() {
        return vehicle_source;
    }

    public void setVehicle_source(String vehicle_source) {
        this.vehicle_source = vehicle_source;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }
}
