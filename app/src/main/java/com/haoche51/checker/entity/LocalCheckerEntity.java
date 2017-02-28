package com.haoche51.checker.entity;


import java.io.Serializable;

/**
 * Created by mac on 15/11/24.
 */
public class LocalCheckerEntity implements Serializable{
	private int id;//评估师id
	private String name;//评估师姓名
	private String first_char;//评估师姓名拼音字母 用于排序
	private boolean isChoose;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirst_char() {
		return first_char;
	}

	public void setFirst_char(String first_char) {
		this.first_char = first_char;
	}

	public boolean isChoose() {
		return isChoose;
	}

	public void setIsChoose(boolean isChoose) {
		this.isChoose = isChoose;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}