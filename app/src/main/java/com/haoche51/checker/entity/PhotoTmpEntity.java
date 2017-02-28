package com.haoche51.checker.entity;


public class PhotoTmpEntity {
	private String imgPath;
	private String imgTmpPath;
	private String url;
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public String getImgTmpPath() {
		return imgTmpPath;
	}
	public void setImgTmpPath(String imgTmpPath) {
		this.imgTmpPath = imgTmpPath;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public PhotoTmpEntity(String imgPath, String imgTmpPath, String url) {
		this.imgPath = imgPath;
		this.imgTmpPath = imgTmpPath;
		this.url = url;
	}
}
