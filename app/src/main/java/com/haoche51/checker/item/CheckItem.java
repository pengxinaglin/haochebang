package com.haoche51.checker.item;

public class CheckItem {
	private String checkItem;
	private int status;
	public String getCheckItem() {
		return checkItem;
	}
	
	public CheckItem(String checkItem, int status){
		this.checkItem = checkItem;
		this.status = status;
	}
	public void setCheckItem(String checkItem) {
		this.checkItem = checkItem;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

}
