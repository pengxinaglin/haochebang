package com.haoche51.checker.entity;

/**
 *  出售渠道实体类
 * Created by wfx on 2016/11/29.
 */

public class SaleChannelEntity {
    private int key;//对应的渠道枚举值
    private String value;//渠道名称

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public SaleChannelEntity() {
    }

    public SaleChannelEntity(int key, String value) {
        this.key = key;
        this.value = value;
    }
}
