package com.haoche51.checker.item;

public class ECheckItem {

	private int has;
	
	private int status;
	
	private String desc;

	private boolean extra ;
	public ECheckItem(){
		
	}
	public ECheckItem (int has, int status, String desc, boolean extra){
		this.has = has;
		this.status = status;
		this.desc = desc;
		this.extra = extra;
	}
	
	public int getHas() {
		return has;
	}
	public void setHas(int has) {
		this.has = has;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	public boolean isExtra() {
		return extra;
	}
	public void setExtra(boolean extra) {
		this.extra = extra;
	}
	
	

}
