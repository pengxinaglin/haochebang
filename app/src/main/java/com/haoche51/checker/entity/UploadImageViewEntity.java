package com.haoche51.checker.entity;

import com.haoche51.checker.item.Picture;

public class UploadImageViewEntity {
	private String filePath;
	private Picture picture;
	private int adapter;
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public Picture getPicture() {
		return picture;
	}
	public void setPicture(Picture picture) {
		this.picture = picture;
	}
	public int getAdapter() {
		return adapter;
	}
	public void setAdapter(int adapter) {
		this.adapter = adapter;
	}
	public UploadImageViewEntity(String filePath, Picture picture, int adapter) {
		super();
		this.filePath = filePath;
		this.picture = picture;
		this.adapter = adapter;
	}
	public UploadImageViewEntity() {
		super();
	}
}
