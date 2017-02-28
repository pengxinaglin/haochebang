package com.haoche51.checker.entity;

public class LocationEntity {
	private double latitude = 0;
	private double longitude =0;
	public LocationEntity() {
		// TODO Auto-generated constructor stub
	}
	public LocationEntity(double latitude, double longitude){
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
