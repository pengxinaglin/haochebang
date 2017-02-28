package com.haoche51.checker.entity;

/**
 * Created by 买家回访 on 15/8/27.
 */
public class BuyerRevisit {

	long id;
	String buyer_phone;//
	String buyer_name;//
	String vehicle_name;
	String content;//
	int custom_service_id;//
	int vehicle_source_id;
	String custom_service_name;//
	int time;//
	int demand_id;//
	int level;//
	String comment;//

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBuyer_phone() {
		return buyer_phone;
	}

	public void setBuyer_phone(String buyer_phone) {
		this.buyer_phone = buyer_phone;
	}

	public String getBuyer_name() {
		return buyer_name;
	}

	public void setBuyer_name(String buyer_name) {
		this.buyer_name = buyer_name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getCustom_service_id() {
		return custom_service_id;
	}

	public void setCustom_service_id(int custom_service_id) {
		this.custom_service_id = custom_service_id;
	}

	public String getCustom_service_name() {
		return custom_service_name;
	}

	public void setCustom_service_name(String custom_service_name) {
		this.custom_service_name = custom_service_name;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getDemand_id() {
		return demand_id;
	}

	public void setDemand_id(int demand_id) {
		this.demand_id = demand_id;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getVehicle_name() {
		return vehicle_name;
	}

	public void setVehicle_name(String vehicle_name) {
		this.vehicle_name = vehicle_name;
	}

	public int getVehicle_source_id() {
		return vehicle_source_id;
	}

	public void setVehicle_source_id(int vehicle_source_id) {
		this.vehicle_source_id = vehicle_source_id;
	}
}
