package com.haoche51.checker;

/**
 * Created by xuhaibo on 15/9/26.
 */
public class LoginToken {

	private String token;
	private int timeOut;
	private int create_time;

	public LoginToken(String token,int timeOut,int create_time) {
		this.token = token;
		this.timeOut = timeOut;
		this.create_time = create_time;
	}
	public int getCreate_time() {
		return create_time;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public void setCreate_time(int create_time) {
		this.create_time = create_time;
	}
}
