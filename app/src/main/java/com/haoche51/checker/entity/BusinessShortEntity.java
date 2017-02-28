package com.haoche51.checker.entity;

/**
 * Created by PengXianglin on 16/3/1.
 */
public class BusinessShortEntity {

	private int id;//1, //商家id
	private String name;//北京xxx车商, //商家名称
	private String contact_name;//张经理, //商家联系人姓名
	private String contact_phone;//18559947110, //商家联系人电话
	private String address;//北京西二旗, //商家地址
	private String maintain_time;//2月15日 12:00, //上次维护时间
	private int sell_car;//100, //在售车源数量
	private String crm_user_name;//崔子成, //维护人姓名
	private String crm_user_phone;//18559947440, //维护人 手机
	private String remark;//维护备注, //维护备注

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact_name() {
		return contact_name;
	}

	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}

	public String getContact_phone() {
		return contact_phone;
	}

	public void setContact_phone(String contact_phone) {
		this.contact_phone = contact_phone;
	}

	public String getCrm_user_name() {
		return crm_user_name;
	}

	public void setCrm_user_name(String crm_user_name) {
		this.crm_user_name = crm_user_name;
	}

	public String getCrm_user_phone() {
		return crm_user_phone;
	}

	public void setCrm_user_phone(String crm_user_phone) {
		this.crm_user_phone = crm_user_phone;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMaintain_time() {
		return maintain_time;
	}

	public void setMaintain_time(String maintain_time) {
		this.maintain_time = maintain_time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getSell_car() {
		return sell_car;
	}

	public void setSell_car(int sell_car) {
		this.sell_car = sell_car;
	}
}
