package com.haoche51.checker.entity;

public class BuyerInfo {

	
	private int sex = 0; // 性别 
	private String job=""; //职业
	private int age = 0; //年龄  1 -> 20，2 -> 30，3 -> 40，4 -> 50
	private int married = 0; //婚否 1 已婚，  2 未婚
	private int income = 0;  //月收入  1->3千以下，2->3-5千，3->5-8千， 4->8-10千，5->1-2万，6->2万以上
	private int purpose = 0;  //购车用途  1家用，2占标，3倒卖
	private int for_who =0 ; //为何人购车  1为自己购车，2为亲戚朋友购车，3公司购车
	private int first_buy=0; // 是否首次购车 1是  2否
	private int first_buy_used=0; //是否首次购二手车  1是  2否
	private int friend_buy_used=0; // 身边是否有朋友购买二手车 1是  2否
	private String channel=""; //从哪里了解到好车无忧的
	
	public BuyerInfo() {
		// TODO Auto-generated constructor stub
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getMarried() {
		return married;
	}

	public void setMarried(int married) {
		this.married = married;
	}

	public int getIncome() {
		return income;
	}

	public void setIncome(int income) {
		this.income = income;
	}

	public int getPurpose() {
		return purpose;
	}

	public void setPurpose(int purpose) {
		this.purpose = purpose;
	}

	public int getFor_who() {
		return for_who;
	}

	public void setFor_who(int for_who) {
		this.for_who = for_who;
	}

	public int getFirst_buy() {
		return first_buy;
	}

	public void setFirst_buy(int first_buy) {
		this.first_buy = first_buy;
	}

	public int getFirst_buy_used() {
		return first_buy_used;
	}

	public void setFirst_buy_used(int first_buy_used) {
		this.first_buy_used = first_buy_used;
	}

	public int getFriend_buy_used() {
		return friend_buy_used;
	}

	public void setFriend_buy_used(int friend_buy_used) {
		this.friend_buy_used = friend_buy_used;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

}
