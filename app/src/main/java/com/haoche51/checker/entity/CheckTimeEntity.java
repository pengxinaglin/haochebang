package com.haoche51.checker.entity;

public class CheckTimeEntity {
	private int errno;
	private String errmsg;

	public CheckTimeEntity() {

	}

	public CheckTimeEntity(int errno, String errmsg) {
		this.errmsg = errmsg;
		this.errno = errno;
	}

	public int getErrno() {
		return errno;
	}

	public void setErrno(int errno) {
		this.errno = errno;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
}
