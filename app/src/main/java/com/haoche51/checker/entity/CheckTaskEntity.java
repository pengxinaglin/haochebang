package com.haoche51.checker.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class CheckTaskEntity extends BaseEntity implements Parcelable{

	private int id = 0;
	// align 服务器状态 。
	// 任务状态 0发起预约 1完成检测 2评估师申请重新预约 3重新预约-完成与车主联系 4重新预约-发起新预约
	private int status = 0; // 0， 预约（待检）1.完成检测 已废弃

	private int appointment_starttime = 0;
	private float eval_price;//个人交易价
	private float eval_price_21;//21天收车价
	private float seller_price;//车主最终报价
	private int start_time = 0;
	private int finish_time = 0;
	private String appointment_place = "";
	private double place_lat;//验车地点经度
	private double place_lng;//验车地点纬度
	private String seller_name = "";
	private String seller_phone = "";
	private int brand_id = 0;//品牌id
	private String brand_name;//品牌名称
	private int series_id;
	private String class_name;//车系名称
	private int check_user_id = 0; //TODO
	private String check_user_name = "";
	private int model_id = 0;
	private String vehicle_name = "";
	private int onsite = 0;
	private int cancel_status = 0; // 1 取消
	private String cancel_reason = "";
	private String comment = "";//电寄备注
	private String checker_comment;//评估师（我的）备注
	private String starttime_comment = "";
	private int suspected_agent = 0;
	private int check_source = 0; //车检任务类型 1 主站，2 爬虫，3 帮检
	private int help_check_status = 0; //1 取消，2 帮检成功，3 帮检成交
	private int ready = 0; // 本地状态 。 1 为 检测报告完成待上传， 其他状态不予理睬
	//    private int create_time;//任务创建时间
	private int seller_phonerec_count;//车主通话录音条数
	private int from_check_user_id;// 转单人crm_user_id (把任务转给该评估师的 原评估师id)
	private String from_check_user_name;// 转单人姓名(把任务转给该评估师的 原评估师姓名)
	private int to_check_user_id;// 转单出去给新的那个评估师的crm_user_id
	private String to_check_user_name;// 转单出去给新的那个评估师的姓名
	private int check_status;//当前筛选任务的状态任务进行状态. 0待检测 1取消 2检测中 3待上传 4成功检测
    private String vin_code;//vin码
	private int check_id;//验车id
//	private List<PhotoEntity> uploadPhotoList;//待上传照片列表 add by wfx

	public CheckTaskEntity() {
	}


	protected CheckTaskEntity(Parcel in) {
		id = in.readInt();
		status = in.readInt();
		appointment_starttime = in.readInt();
		eval_price = in.readFloat();
		eval_price_21 = in.readFloat();
		seller_price = in.readFloat();
		start_time = in.readInt();
		finish_time = in.readInt();
		appointment_place = in.readString();
		place_lat = in.readDouble();
		place_lng = in.readDouble();
		seller_name = in.readString();
		seller_phone = in.readString();
		brand_id = in.readInt();
		brand_name = in.readString();
		series_id = in.readInt();
		class_name = in.readString();
		check_user_id = in.readInt();
		check_user_name = in.readString();
		model_id = in.readInt();
		vehicle_name = in.readString();
		onsite = in.readInt();
		cancel_status = in.readInt();
		cancel_reason = in.readString();
		comment = in.readString();
		checker_comment = in.readString();
		starttime_comment = in.readString();
		suspected_agent = in.readInt();
		check_source = in.readInt();
		help_check_status = in.readInt();
		ready = in.readInt();
		seller_phonerec_count = in.readInt();
		from_check_user_id = in.readInt();
		from_check_user_name = in.readString();
		to_check_user_id = in.readInt();
		to_check_user_name = in.readString();
		check_status = in.readInt();
		vin_code = in.readString();
		check_id = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(status);
		dest.writeInt(appointment_starttime);
		dest.writeFloat(eval_price);
		dest.writeFloat(eval_price_21);
		dest.writeFloat(seller_price);
		dest.writeInt(start_time);
		dest.writeInt(finish_time);
		dest.writeString(appointment_place);
		dest.writeDouble(place_lat);
		dest.writeDouble(place_lng);
		dest.writeString(seller_name);
		dest.writeString(seller_phone);
		dest.writeInt(brand_id);
		dest.writeString(brand_name);
		dest.writeInt(series_id);
		dest.writeString(class_name);
		dest.writeInt(check_user_id);
		dest.writeString(check_user_name);
		dest.writeInt(model_id);
		dest.writeString(vehicle_name);
		dest.writeInt(onsite);
		dest.writeInt(cancel_status);
		dest.writeString(cancel_reason);
		dest.writeString(comment);
		dest.writeString(checker_comment);
		dest.writeString(starttime_comment);
		dest.writeInt(suspected_agent);
		dest.writeInt(check_source);
		dest.writeInt(help_check_status);
		dest.writeInt(ready);
		dest.writeInt(seller_phonerec_count);
		dest.writeInt(from_check_user_id);
		dest.writeString(from_check_user_name);
		dest.writeInt(to_check_user_id);
		dest.writeString(to_check_user_name);
		dest.writeInt(check_status);
		dest.writeString(vin_code);
		dest.writeInt(check_id);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<CheckTaskEntity> CREATOR = new Creator<CheckTaskEntity>() {
		@Override
		public CheckTaskEntity createFromParcel(Parcel in) {
			return new CheckTaskEntity(in);
		}

		@Override
		public CheckTaskEntity[] newArray(int size) {
			return new CheckTaskEntity[size];
		}
	};

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getAppointment_starttime() {
		return appointment_starttime;
	}

	public void setAppointment_starttime(int appointment_starttime) {
		this.appointment_starttime = appointment_starttime;
	}

	public String getAppointment_place() {
		return appointment_place;
	}

	public void setAppointment_place(String appointment_place) {
		this.appointment_place = appointment_place;
	}

	public String getSeller_name() {
		return seller_name;
	}

	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}

	public String getVehicle_name() {
		return vehicle_name;
	}

	public void setVehicle_name(String vehicle_name) {
		this.vehicle_name = vehicle_name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getModel_id() {
		return model_id;
	}

	public void setModel_id(int model_id) {
		this.model_id = model_id;
	}

	public int getBrand_id() {
		return brand_id;
	}

	public void setBrand_id(int brand_id) {
		this.brand_id = brand_id;
	}

	public int getSeries_id() {
		return series_id;
	}

	public void setSeries_id(int series_id) {
		this.series_id = series_id;
	}

	public int getSuspected_agent() {
		return suspected_agent;
	}

	public void setSuspected_agent(int suspected_agent) {
		this.suspected_agent = suspected_agent;
	}

	public int getCancel_status() {
		return cancel_status;
	}

	public void setCancel_status(int cancel_status) {
		this.cancel_status = cancel_status;
	}

	public int getStart_time() {
		return start_time;
	}

	public void setStart_time(int start_time) {
		this.start_time = start_time;
	}

	public int getFinish_time() {
		return finish_time;
	}

	public void setFinish_time(int finish_time) {
		this.finish_time = finish_time;
	}

	public String getSeller_phone() {
		return seller_phone;
	}

	public void setSeller_phone(String seller_phone) {
		this.seller_phone = seller_phone;
	}

	public int getOnsite() {
		return onsite;
	}

	public void setOnsite(int onsite) {
		this.onsite = onsite;
	}

	public String getCancel_reason() {
		return cancel_reason;
	}

	public void setCancel_reason(String cancel_reason) {
		this.cancel_reason = cancel_reason;
	}

	public String getStarttime_comment() {
		return starttime_comment;
	}

	public void setStarttime_comment(String starttime_comment) {
		this.starttime_comment = starttime_comment;
	}

	public int getCheck_source() {
		return check_source;
	}

	public void setCheck_source(int check_source) {
		this.check_source = check_source;
	}

	public int getHelp_check_status() {
		return help_check_status;
	}

	public void setHelp_check_status(int help_check_status) {
		this.help_check_status = help_check_status;
	}

	public int getReady() {
		return ready;
	}

	public void setReady(int ready) {
		this.ready = ready;
	}

	public int getCheck_user_id() {
		return check_user_id;
	}

	public void setCheck_user_id(int check_user_id) {
		this.check_user_id = check_user_id;
	}

	public String getCheck_user_name() {
		return check_user_name;
	}

	public void setCheck_user_name(String check_user_name) {
		this.check_user_name = check_user_name;
	}

	public float getEval_price() {
		return eval_price;
	}

	public void setEval_price(float eval_price) {
		this.eval_price = eval_price;
	}

	public float getEval_price_21() {
		return eval_price_21;
	}

	public void setEval_price_21(float eval_price_21) {
		this.eval_price_21 = eval_price_21;
	}

	public int getSeller_phonerec_count() {

		return seller_phonerec_count;
	}

	public void setSeller_phonerec_count(int seller_phonerec_count) {
		this.seller_phonerec_count = seller_phonerec_count;
	}

	public String getChecker_comment() {
		return checker_comment;
	}

	public void setChecker_comment(String checker_comment) {
		this.checker_comment = checker_comment;
	}


	public String getFrom_check_user_name() {
		return from_check_user_name;
	}

	public void setFrom_check_user_name(String from_check_user_name) {
		this.from_check_user_name = from_check_user_name;
	}

	public int getFrom_check_user_id() {
		return from_check_user_id;
	}

	public void setFrom_check_user_id(int from_check_user_id) {
		this.from_check_user_id = from_check_user_id;
	}

	public int getTo_check_user_id() {
		return to_check_user_id;
	}

	public void setTo_check_user_id(int to_check_user_id) {
		this.to_check_user_id = to_check_user_id;
	}

	public String getTo_check_user_name() {
		return to_check_user_name;
	}

	public void setTo_check_user_name(String to_check_user_name) {
		this.to_check_user_name = to_check_user_name;
	}

	public float getSeller_price() {

		return seller_price;
	}

	public void setSeller_price(float seller_price) {
		this.seller_price = seller_price;
	}

	public int getCheck_status() {
		return check_status;
	}

	public void setCheck_status(int check_status) {
		this.check_status = check_status;
	}

	public double getPlace_lat() {
		return place_lat;
	}

	public void setPlace_lat(double place_lat) {
		this.place_lat = place_lat;
	}

	public double getPlace_lng() {
		return place_lng;
	}

	public void setPlace_lng(double place_lng) {
		this.place_lng = place_lng;
	}

	public String getBrand_name() {
		return brand_name;
	}

	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}

	public String getClass_name() {
		return class_name;
	}

	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}

	public String getVin_code() {
		return vin_code;
	}

	public void setVin_code(String vin_code) {
		this.vin_code = vin_code;
	}

	public int getCheck_id() {
		return check_id;
	}

	public void setCheck_id(int check_id) {
		this.check_id = check_id;
	}
}
