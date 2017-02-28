package com.haoche51.checker.item;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

public class Picture {
	private int type = 0;
	private String path = null;
	private String url = null;
	private String comment = "";
	private float positionX = 0;
	private float positionY = 0;
	
	public Picture() {
		
	}
	
	public Picture(String path) {
		this.path = path;
	}
	/**
	 * 
	 * @param path
	 */
	public void setPosition(String path){
		Pattern mPattern=Pattern.compile(".*scratch_[0-9]+_([0-9]+)_([0-9]+).*");
		Matcher mMatcher = mPattern.matcher(path);
		if (mMatcher.find() && mMatcher.groupCount() == 2){
			positionX = Float.parseFloat(mMatcher.group(1))/10000;
			positionY = Float.parseFloat(mMatcher.group(2))/10000;
		}
	}
	public JSONObject toJson() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("t", type);
			obj.put("u", url != null ? url : "");
			if (positionX != 0 || positionY != 0) {
				obj.put("p", positionX + "," + positionY);
				obj.put("c", positionX + "," + positionY);
			} else {
				obj.put("c", comment);
			}
		} catch (JSONException e) {
			return null;
		}
		return obj;
	}
	
	public boolean isFlaw() {
		return positionX != 0 || positionY != 0;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public float getPositionX() {
		return positionX;
	}
	
	public float getPositionY() {
		return positionY;
	}

	public void setPosition(float x, float y) {
		positionX = x;
		positionY = y;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
