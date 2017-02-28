package com.haoche51.checker.entity.thirdparty;

import com.haoche51.checker.entity.BaseEntity;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by xuhaibo on 15/9/17.
 */
public class Che300Entity extends BaseEntity implements Serializable{

	private float low_price;
	private float high_price;



	public Che300Entity(){

	}
	/**
	 * 返回che300 实体String
	 * @param String che300_price
	 * @return
	 */
	public Che300Entity(String che300_price) { //数据库读取string 后转成float
		try {
			JSONObject obj = new JSONObject(che300_price);
			this.low_price = (float)obj.optDouble("low_price");
			this.high_price = (float)obj.optDouble("high_price");
		}catch (Exception e){
			this.low_price = 0;
			this.high_price =0;
		}
	}
	public float getLow_price() {
		return low_price;
	}
	public float getHigh_price() {
		return high_price;
	}

	public void setLow_price(float low_price) {

		this.low_price = low_price;
	}

	public void setHigh_price(float high_price) {
		this.high_price = high_price;
	}

	@Override
	public String toString() {
		return gson.toJson(this).toString();//本地数据库转成String
	}
}
