package com.haoche51.checker.entity;

import java.util.List;

/**
 * 收车成功实体类
 * Created by wufx on 2016/1/24.
 */
public class PurchaseSuccessEntity {
    /**
     * 任务id
     */
    private int taskId;

    /**
     * vin码
     */
    private String vin;

    /**
     * 车源
     */
    private VehicleSourceEntity vehicleSource;

    /**
     * 上牌时间
     */
    private int registTime;

    /**
     * 表显里程
     */
    private float showMile;

    /**
     * 过户次数
     */
    private int transferTimes;

    /**
     * 收购价格
     */
    private int purchasePrice;

    /**
     * 车辆报价
     */
    private int offerPrice;

    /**
     * 车辆底价
     */
    private int lowPrice;

    /**
     * 过户费
     */
    private int transferFee;

    /**
     * 介绍费
     */
    private int referralFee;

    /**
     * 备注
     */
    private String remark;

    /**
     * 图片集合
     */
    private List<PhotoEntity> photoList;

    /**
     * 是否有保险收益
     * 1：有 2：没有
     */
    private int hasBxsy;

    /**
     * 保险到期时间
     */
    private int bxdqTime;

    public int getRegistTime() {
        return registTime;
    }

    public void setRegistTime(int registTime) {
        this.registTime = registTime;
    }

    public float getShowMile() {
        return showMile;
    }

    public void setShowMile(float showMile) {
        this.showMile = showMile;
    }

    public int getTransferTimes() {
        return transferTimes;
    }

    public void setTransferTimes(int transferTimes) {
        this.transferTimes = transferTimes;
    }

    public List<PhotoEntity> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<PhotoEntity> photoList) {
        this.photoList = photoList;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public VehicleSourceEntity getVehicleSource() {
        return vehicleSource;
    }

    public void setVehicleSource(VehicleSourceEntity vehicleSource) {
        this.vehicleSource = vehicleSource;
    }

    public int getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(int purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public int getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(int offerPrice) {
        this.offerPrice = offerPrice;
    }

    public int getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(int lowPrice) {
        this.lowPrice = lowPrice;
    }

    public int getTransferFee() {
        return transferFee;
    }

    public void setTransferFee(int transferFee) {
        this.transferFee = transferFee;
    }

    public int getReferralFee() {
        return referralFee;
    }

    public void setReferralFee(int referralFee) {
        this.referralFee = referralFee;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getBxdqTime() {
        return bxdqTime;
    }

    public void setBxdqTime(int bxdqTime) {
        this.bxdqTime = bxdqTime;
    }

    public int getHasBxsy() {
        return hasBxsy;
    }

    public void setHasBxsy(int hasBxsy) {
        this.hasBxsy = hasBxsy;
    }
}
