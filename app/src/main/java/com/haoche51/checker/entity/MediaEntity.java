package com.haoche51.checker.entity;


import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

/**
 * 多媒体实体类（音频、视频）
 */
public class MediaEntity implements Parcelable {
    public static final Creator<MediaEntity> CREATOR = new Creator<MediaEntity>() {
        @Override
        public MediaEntity createFromParcel(Parcel in) {
            return new MediaEntity(in);
        }

        @Override
        public MediaEntity[] newArray(int size) {
            return new MediaEntity[size];
        }
    };
    private int id;
    /**
     * 文件名
     */
    private String displayName;
    /**
     * 文件类型
     */
    private String mimeType;
    /**
     * 文件路径
     */
    private String path;
    /**
     * 对应压缩后的文件存储路径
     */
    private String compressedPath;
    /**
     * 对应复制后的文件存储路径
     */
    private String copyedPath;
    /**
     * 七牛云存储url
     */
    private String url;
    private String uuid;
    /**
     * 文件大小
     */
    private long size;
    /**
     * 文件时长
     */
    private long duration;
    /**
     * 修改时间
     */
    private long modifyDate;
    /**
     * 是否被选中
     */
    private boolean isChecked;
    private Bitmap bitmap;

    /**
     * 是否已经压缩完成
     */
    private boolean isCompressed;

    public MediaEntity() {
        super();
    }

    protected MediaEntity(Parcel in) {
        id = in.readInt();
        displayName = in.readString();
        mimeType = in.readString();
        path = in.readString();
        compressedPath = in.readString();
        copyedPath = in.readString();
        url = in.readString();
        uuid = in.readString();
        size = in.readLong();
        duration = in.readLong();
        modifyDate = in.readLong();
        isChecked = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(displayName);
        dest.writeString(mimeType);
        dest.writeString(path);
        dest.writeString(compressedPath);
        dest.writeString(copyedPath);
        dest.writeString(url);
        dest.writeString(uuid);
        dest.writeLong(size);
        dest.writeLong(duration);
        dest.writeLong(modifyDate);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }


    public long getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(long modifyDate) {
        this.modifyDate = modifyDate;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCompressedPath() {
        return compressedPath;
    }

    public void setCompressedPath(String compressedPath) {
        this.compressedPath = compressedPath;
    }

    public String getCopyedPath() {
        return copyedPath;
    }

    public void setCopyedPath(String copyedPath) {
        this.copyedPath = copyedPath;
    }

    /**
     * 设置是否压缩完成
     */
    public void setCompressed(boolean compressed) {
        isCompressed = compressed;
    }

    public boolean isCompressed() {
        return isCompressed;
    }

    public String toJson() {
        MediaEntity entity = new MediaEntity();
        entity.setPath(getPath());
        entity.setUuid(getUuid());
        entity.setUrl(getUrl());
        entity.setCopyedPath(getCopyedPath());
        entity.setCompressedPath(getCompressedPath());
        entity.setChecked(isChecked());
        entity.setDisplayName(getDisplayName());
        entity.setDuration(getDuration());
        entity.setId(getId());
        entity.setMimeType(getMimeType());
        entity.setSize(getSize());
        entity.setModifyDate(getModifyDate());
        return new Gson().toJson(entity);
    }

}