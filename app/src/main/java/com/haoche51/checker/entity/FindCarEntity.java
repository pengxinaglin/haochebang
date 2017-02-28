package com.haoche51.checker.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 找车需求实体类
 * Created by wfx on 2016/11/1.
 */

public class FindCarEntity implements Parcelable{
    /**
     * 找车任务id
     */
    private int request_id;
    /**
     * 买家姓名
     */
    private String buyer_name;
    /**
     * 买家电话
     */
    private String buyer_phone;
    /**
     * 状态：10待匹配  20已匹配   30无法匹配
     */
    private int status;
    /**
     * 状态文本值：10待匹配  20已匹配   30无法匹配
     */
    private String status_text;
    /**
     * 车系 '宝马，奥迪'
     */
    private String vehicle_text;
    /**
     * 价格 '10~20万',
     */
    private String price_text;
    /**
     * 年龄 '0~5年'
     */
    private String age_text;
    /**
     * 变速箱 '自动'
     */
    private String gearbox_text;
    /**
     * 颜色 '黑色、白色'
     */
    private String color_text;
    /**
     * 其他 '个人一手车'
     */
    private String other_text;
    /**
     * 电销 '浮树坤'
     */
    private String custom_service_name;
    /**
     * 电销联系方式 '13800138000
     */
    private String custom_service_phone;

    protected FindCarEntity(Parcel in) {
        request_id = in.readInt();
        buyer_name = in.readString();
        buyer_phone = in.readString();
        status = in.readInt();
        status_text = in.readString();
        vehicle_text = in.readString();
        price_text = in.readString();
        age_text = in.readString();
        gearbox_text = in.readString();
        color_text = in.readString();
        other_text = in.readString();
        custom_service_name = in.readString();
        custom_service_phone = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(request_id);
        dest.writeString(buyer_name);
        dest.writeString(buyer_phone);
        dest.writeInt(status);
        dest.writeString(status_text);
        dest.writeString(vehicle_text);
        dest.writeString(price_text);
        dest.writeString(age_text);
        dest.writeString(gearbox_text);
        dest.writeString(color_text);
        dest.writeString(other_text);
        dest.writeString(custom_service_name);
        dest.writeString(custom_service_phone);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FindCarEntity> CREATOR = new Creator<FindCarEntity>() {
        @Override
        public FindCarEntity createFromParcel(Parcel in) {
            return new FindCarEntity(in);
        }

        @Override
        public FindCarEntity[] newArray(int size) {
            return new FindCarEntity[size];
        }
    };

    public int getRequest_id() {
        return request_id;
    }

    public void setRequest_id(int request_id) {
        this.request_id = request_id;
    }

    public String getBuyer_name() {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }

    public String getBuyer_phone() {
        return buyer_phone;
    }

    public void setBuyer_phone(String buyer_phone) {
        this.buyer_phone = buyer_phone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatus_text() {
        return status_text;
    }

    public void setStatus_text(String status_text) {
        this.status_text = status_text;
    }

    public String getVehicle_text() {
        return vehicle_text;
    }

    public void setVehicle_text(String vehicle_text) {
        this.vehicle_text = vehicle_text;
    }

    public String getPrice_text() {
        return price_text;
    }

    public void setPrice_text(String price_text) {
        this.price_text = price_text;
    }

    public String getAge_text() {
        return age_text;
    }

    public void setAge_text(String age_text) {
        this.age_text = age_text;
    }

    public String getGearbox_text() {
        return gearbox_text;
    }

    public void setGearbox_text(String gearbox_text) {
        this.gearbox_text = gearbox_text;
    }

    public String getColor_text() {
        return color_text;
    }

    public void setColor_text(String color_text) {
        this.color_text = color_text;
    }

    public String getOther_text() {
        return other_text;
    }

    public void setOther_text(String other_text) {
        this.other_text = other_text;
    }

    public String getCustom_service_name() {
        return custom_service_name;
    }

    public void setCustom_service_name(String custom_service_name) {
        this.custom_service_name = custom_service_name;
    }

    public String getCustom_service_phone() {
        return custom_service_phone;
    }

    public void setCustom_service_phone(String custom_service_phone) {
        this.custom_service_phone = custom_service_phone;
    }
}
