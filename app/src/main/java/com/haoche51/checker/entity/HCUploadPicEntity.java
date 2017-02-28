package com.haoche51.checker.entity;

/***
 * 上传图片返回结果实体
 * 
 * @author lightman_mac // { "code" : "0", "msg" :
 *         "http://192.168.1.106:9999/7c6c5eb270d5d57bfa6b0d2dfba485cc385b4a71.jpg"
 *         }
 */
public class HCUploadPicEntity {
	/** 结果码 0 成功 */
	private int code;
	/** 图片地址 */
	private String msg;

	public HCUploadPicEntity() {

	}

	public HCUploadPicEntity(int code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "UploadPicEntity [code=" + code + ", msg=" + msg + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + code;
		result = prime * result + ((msg == null) ? 0 : msg.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HCUploadPicEntity other = (HCUploadPicEntity) obj;
		if (code != other.code)
			return false;
		if (msg == null) {
			if (other.msg != null)
				return false;
		} else if (!msg.equals(other.msg))
			return false;
		return true;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
