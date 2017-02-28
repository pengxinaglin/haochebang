package com.haoche51.checker.entity;

import android.os.Bundle;

/**
 * 用户权限实体类
 * Created by wfx on 2016/7/18.
 */
public class UserRightShortEntity {
    /**
     * 权限id
     */
    private int id;
    /**
     * 权限编号
     */
    private int code;
    /**
     * 权限名称
     */
    private String name;
    /**
     * 资源id
     */
    private int resId;
    /**
     * 类名
     */
    private String className;

    private Bundle params;

    public UserRightShortEntity() {
    }

    public UserRightShortEntity(int code, String name, int resId, String className) {
        this.code = code;
        this.name = name;
        this.resId = resId;
        this.className = className;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Bundle getParams() {
        return params;
    }

    public void setParams(Bundle params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "UserRightShortEntity{" +
                "id=" + id +
                ", code=" + code +
                ", name='" + name + '\'' +
                ", resId=" + resId +
                ", className='" + className + '\'' +
                ", params=" + params +
                '}';
    }
}
