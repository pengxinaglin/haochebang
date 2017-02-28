package com.haoche51.checker.entity;

public class Picture {
	private int id = 0;
	private String path = "";
	private float positionX = 0;
	private float positionY = 0;
	private String url = "";
	private String comment = "";
	private int type = 0;

	public Picture() {

	}

	public Picture(String path, String url, String comment, float positionX,
			float positionY, int type) {
		this.path = path;
		this.url = url;
		this.comment = comment;
		this.positionX = positionX;
		this.positionY = positionY;
		this.type = type;
	}
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public float getPositionX() {
		return positionX;
	}

	public void setPositionX(float positionX) {
		this.positionX = positionX;
	}

	public float getPositionY() {
		return positionY;
	}

	public void setPositionY(float positionY) {
		this.positionY = positionY;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
