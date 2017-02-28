package com.haoche51.checker.entity;

/**
 * Created by PengXianglin on 16/3/4.
 */
public class MerchantVehicleSourceEntity {
	private int id;// int, //车源id
	private String status_text;// 待审核, //状态文本值
	private String title;// 宝马 x5, //车源标题
	private String cover_img;// http://xxxxxx/xxx, //车源封面图
	private String price;// 10.5, //报价，单位 万元
	private String up_time;// 2月15好 12：00, //更新时间

	public String getCover_img() {
		return cover_img;
	}

	public void setCover_img(String cover_img) {
		this.cover_img = cover_img;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getStatus_text() {
		return status_text;
	}

	public void setStatus_text(String status_text) {
		this.status_text = status_text;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUp_time() {
		return up_time;
	}

	public void setUp_time(String up_time) {
		this.up_time = up_time;
	}
}
