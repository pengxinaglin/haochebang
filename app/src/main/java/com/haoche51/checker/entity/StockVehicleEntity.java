package com.haoche51.checker.entity;

/**
 * Created by xuhaibo on 16/7/5.
 */
public class StockVehicleEntity {
    public String title;
    public String seller_price;
    public String vehicle_type;
    public String back_task_id;
    public String vehicle_source_id;
    public String cover_img;

    public String getCover_img() {
        return cover_img;
    }
    public void setCover_img(String cover_img) {
        this.cover_img = cover_img;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSeller_price() {
        return seller_price;
    }

    public void setSeller_price(String seller_price) {
        this.seller_price = seller_price;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getBack_task_id() {
        return back_task_id;
    }

    public void setBack_task_id(String back_task_id) {
        this.back_task_id = back_task_id;
    }

    public String getVehicle_source_id() {
        return vehicle_source_id;
    }

    public void setVehicle_source_id(String vehicle_source_id) {
        this.vehicle_source_id = vehicle_source_id;
    }
}
