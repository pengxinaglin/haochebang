package com.haoche51.checker.item;
/**
 *  轮胎及刹车片数值
 *
 * @author xuhaibo
 *
 */
public class TireData {

	private float lf;//左前
	private float lr;//左后
	private float rf;//右前
	private float rr;//右后
	
	
	public TireData(float lf, float lr, float rf, float rr){
		this.lf = lf;
		this.lr = lr;
		this.rf = rf;
		this.rr = rr;
	}
	public float getLf() {
		return lf;
	}
	public void setLf(float lf) {
		this.lf = lf;
	}
	public float getLr() {
		return lr;
	}
	public void setLr(float lr) {
		this.lr = lr;
	}
	public float getRf() {
		return rf;
	}
	public void setRf(float rf) {
		this.rf = rf;
	}
	public float getRr() {
		return rr;
	}
	public void setRr(float rr) {
		this.rr = rr;
	}
	
	
	
	

}
