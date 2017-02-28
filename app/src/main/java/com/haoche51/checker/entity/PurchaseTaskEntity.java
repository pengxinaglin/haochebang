package com.haoche51.checker.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by mac on 16/1/19.
 */
public class PurchaseTaskEntity implements Parcelable {
	/**
	 * 交易类型
	 */
	private int trans_type;
	private int task_id; //10 任务id
	private String task_num; //SC10 任务编号
	private long vehicle_source_id;//车源id
	private String title;//奥迪 A4, //任务名称，即为标题
	private String seller_phone;//1888888888, //车主电话
	private String seller_name;//李四, //车主姓名
	private String remark;//备注, //备注
	private int is_outside;//0 1, //是否外网。0：否、1：是
	private String status_text;//状态名称, //已预约/待付定金 等状态文本值.
	private int status;//1 //状态枚举值
	/**
	 * 支付状态
	 */
	private int pay_status;
	/**
	 * 支付状态文本值
	 */
	private String pay_status_text;

	/**
	 * 最后一次支付状态
	 * 1：付款申请中 2：付款被拒绝 3：财务已付款
	 */
	private int last_pay_status;


	/**
	 * 最后一次支付状态文本值
	 * 1：付款申请中 2：付款被拒绝 3：财务已付款
	 */
	private String last_pay_status_text;

	/**
	 * 支付失败理由, 没有就为空字符串
	 */
	private String last_pay_fail_reason;

	private String appoint_time;//1月1日 12：30, //预约时间
	private int appoint_unix;
	private String appoint_address;//北京市 海淀区 西二旗 地铁站, //预约地点
	/**
	 * 已付收车金额 单位“元”
	 */
	private String has_pay;
	private String back_price;//100000 //收购价格，单位“元”
	private String total_back_price;//收购总价=收购价格+过户费+介绍费
	private long transfer_free;//过户费，单位“元”
	private long introduce_free;//介绍费，单位“元”

	private String fail_reason;//xxxx, //任务失败原因
	private int hope_price;//’  10000, //心里价位，单位“元”
	private int peer_price;//20000, //同行价位，单位“元”
	private int our_price;//30000, //我方出价，单位“元”
	private String sell_cycle;//1个月, //卖车周期
	private int brand_id;// int, //品牌id
	private String brand_name;// string, //品牌名称
	private int class_id;// int, //车系id
	private String class_name;// string, //车系名称
	private int vehicle_id;// int, //车型id
	private String vehicle_name;// string, //车型名称
	private String year;// string, //年款
	private int crm_user_id;// int, //跟进该任务的地收crm用户id
	private String crm_user_name;// string, //跟进该任务的地收crm用户姓名
	private String launch_check_msg;//点击发起复检时的、重复发起时的提示内容。空串表示不重复。
	private int online_now;//0表示不能再立即上架，1表示可以发起立即上架
	private String online_now_fail_reason;//不能立即上架的原因，如果为空表示可以立即上架
	/**
	 * 应退金额
	 */
	private int need_back_amount;
	/**
	 * 实退金额
	 */
	private int real_back_amount;

	/**
	 * 毁约备注
	 */
	private String break_remark;

	/**
	 * 备注中的链接url
	 */
	private String url;
	private int clue_add_user_id; //线索提供人crm用户id
	private String clue_add_user_name; //线索提供人crm用户姓名
	private String clue_add_user_role;//线索提供人crm主角色名称
	private String clue_add_user_phone;//线索提供人手机号
	private String vin_code;//车源vin码
	public PurchaseTaskEntity(){}


	protected PurchaseTaskEntity(Parcel in) {
		trans_type = in.readInt();
		task_id = in.readInt();
		task_num = in.readString();
		vehicle_source_id = in.readLong();
		title = in.readString();
		seller_phone = in.readString();
		seller_name = in.readString();
		remark = in.readString();
		is_outside = in.readInt();
		status_text = in.readString();
		status = in.readInt();
		pay_status = in.readInt();
		pay_status_text = in.readString();
		last_pay_status = in.readInt();
		last_pay_status_text = in.readString();
		last_pay_fail_reason = in.readString();
		appoint_time = in.readString();
		appoint_unix = in.readInt();
		appoint_address = in.readString();
		has_pay = in.readString();
		back_price = in.readString();
		total_back_price = in.readString();
		transfer_free = in.readLong();
		introduce_free = in.readLong();
		fail_reason = in.readString();
		hope_price = in.readInt();
		peer_price = in.readInt();
		our_price = in.readInt();
		sell_cycle = in.readString();
		brand_id = in.readInt();
		brand_name = in.readString();
		class_id = in.readInt();
		class_name = in.readString();
		vehicle_id = in.readInt();
		vehicle_name = in.readString();
		year = in.readString();
		crm_user_id = in.readInt();
		crm_user_name = in.readString();
		launch_check_msg = in.readString();
		online_now = in.readInt();
		online_now_fail_reason = in.readString();
		need_back_amount = in.readInt();
		real_back_amount = in.readInt();
		break_remark = in.readString();
		url = in.readString();
		clue_add_user_id = in.readInt();
		clue_add_user_name = in.readString();
		clue_add_user_role = in.readString();
		clue_add_user_phone = in.readString();
		vin_code = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(trans_type);
		dest.writeInt(task_id);
		dest.writeString(task_num);
		dest.writeLong(vehicle_source_id);
		dest.writeString(title);
		dest.writeString(seller_phone);
		dest.writeString(seller_name);
		dest.writeString(remark);
		dest.writeInt(is_outside);
		dest.writeString(status_text);
		dest.writeInt(status);
		dest.writeInt(pay_status);
		dest.writeString(pay_status_text);
		dest.writeInt(last_pay_status);
		dest.writeString(last_pay_status_text);
		dest.writeString(last_pay_fail_reason);
		dest.writeString(appoint_time);
		dest.writeInt(appoint_unix);
		dest.writeString(appoint_address);
		dest.writeString(has_pay);
		dest.writeString(back_price);
		dest.writeString(total_back_price);
		dest.writeLong(transfer_free);
		dest.writeLong(introduce_free);
		dest.writeString(fail_reason);
		dest.writeInt(hope_price);
		dest.writeInt(peer_price);
		dest.writeInt(our_price);
		dest.writeString(sell_cycle);
		dest.writeInt(brand_id);
		dest.writeString(brand_name);
		dest.writeInt(class_id);
		dest.writeString(class_name);
		dest.writeInt(vehicle_id);
		dest.writeString(vehicle_name);
		dest.writeString(year);
		dest.writeInt(crm_user_id);
		dest.writeString(crm_user_name);
		dest.writeString(launch_check_msg);
		dest.writeInt(online_now);
		dest.writeString(online_now_fail_reason);
		dest.writeInt(need_back_amount);
		dest.writeInt(real_back_amount);
		dest.writeString(break_remark);
		dest.writeString(url);
		dest.writeInt(clue_add_user_id);
		dest.writeString(clue_add_user_name);
		dest.writeString(clue_add_user_role);
		dest.writeString(clue_add_user_phone);
		dest.writeString(vin_code);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<PurchaseTaskEntity> CREATOR = new Creator<PurchaseTaskEntity>() {
		@Override
		public PurchaseTaskEntity createFromParcel(Parcel in) {
			return new PurchaseTaskEntity(in);
		}

		@Override
		public PurchaseTaskEntity[] newArray(int size) {
			return new PurchaseTaskEntity[size];
		}
	};

	public String getAppoint_address() {
		return appoint_address;
	}

	public void setAppoint_address(String appoint_address) {
		this.appoint_address = appoint_address;
	}

	public String getAppoint_time() {
		return appoint_time;
	}

	public void setAppoint_time(String appoint_time) {
		this.appoint_time = appoint_time;
	}



	public String getFail_reason() {
		return fail_reason;
	}

	public void setFail_reason(String fail_reason) {
		this.fail_reason = fail_reason;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSell_cycle() {
		return sell_cycle;
	}

	public void setSell_cycle(String sell_cycle) {
		this.sell_cycle = sell_cycle;
	}

	public String getSeller_name() {
		return seller_name;
	}

	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}

	public String getSeller_phone() {
		return seller_phone;
	}

	public void setSeller_phone(String seller_phone) {
		this.seller_phone = seller_phone;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatus_text() {
		return status_text;
	}

	public void setStatus_text(String status_text) {
		this.status_text = status_text;
	}

	public int getTask_id() {
		return task_id;
	}

	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}

	public String getTask_num() {
		return task_num;
	}

	public void setTask_num(String task_num) {
		this.task_num = task_num;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getTransfer_free() {
		return transfer_free;
	}

	public void setTransfer_free(long transfer_free) {
		this.transfer_free = transfer_free;
	}

	public int getIs_outside() {
		return is_outside;
	}

	public void setIs_outside(int is_outside) {
		this.is_outside = is_outside;
	}

	public long getVehicle_source_id() {
		return vehicle_source_id;
	}

	public void setVehicle_source_id(long vehicle_source_id) {
		this.vehicle_source_id = vehicle_source_id;
	}

	public int getBrand_id() {
		return brand_id;
	}

	public void setBrand_id(int brand_id) {
		this.brand_id = brand_id;
	}

	public int getClass_id() {
		return class_id;
	}

	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}

	public int getCrm_user_id() {
		return crm_user_id;
	}

	public void setCrm_user_id(int crm_user_id) {
		this.crm_user_id = crm_user_id;
	}

	public int getVehicle_id() {
		return vehicle_id;
	}

	public void setVehicle_id(int vehicle_id) {
		this.vehicle_id = vehicle_id;
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

	public String getCrm_user_name() {
		return crm_user_name;
	}

	public void setCrm_user_name(String crm_user_name) {
		this.crm_user_name = crm_user_name;
	}

	public String getVehicle_name() {
		return vehicle_name;
	}

	public void setVehicle_name(String vehicle_name) {
		this.vehicle_name = vehicle_name;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getBrand_name() {
		return brand_name;
	}

	public int getAppoint_unix() {
		return appoint_unix;
	}

	public void setAppoint_unix(int appoint_unix) {
		this.appoint_unix = appoint_unix;
	}

	public String getLaunch_check_msg() {
		return launch_check_msg;
	}

	public void setLaunch_check_msg(String launch_check_msg) {
		this.launch_check_msg = launch_check_msg;
	}

	public int getHope_price() {
		return hope_price;
	}

	public void setHope_price(int hope_price) {
		this.hope_price = hope_price;
	}

	public int getPeer_price() {
		return peer_price;
	}

	public void setPeer_price(int peer_price) {
		this.peer_price = peer_price;
	}

	public int getOur_price() {
		return our_price;
	}

	public void setOur_price(int our_price) {
		this.our_price = our_price;
	}

	public int getOnline_now() {
		return online_now;
	}

	public void setOnline_now(int online_now) {
		this.online_now = online_now;
	}

	public String getOnline_now_fail_reason() {
		return online_now_fail_reason;
	}

	public void setOnline_now_fail_reason(String online_now_fail_reason) {
		this.online_now_fail_reason = online_now_fail_reason;
	}

	public int getNeed_back_amount() {
		return need_back_amount;
	}

	public void setNeed_back_amount(int need_back_amount) {
		this.need_back_amount = need_back_amount;
	}

	public int getReal_back_amount() {
		return real_back_amount;
	}

	public void setReal_back_amount(int real_back_amount) {
		this.real_back_amount = real_back_amount;
	}

	public String getBreak_remark() {
		return break_remark;
	}

	public void setBreak_remark(String break_remark) {
		this.break_remark = break_remark;
	}

	public int getClue_add_user_id() {
		return clue_add_user_id;
	}

	public void setClue_add_user_id(int clue_add_user_id) {
		this.clue_add_user_id = clue_add_user_id;
	}

	public String getClue_add_user_name() {
		return clue_add_user_name;
	}

	public void setClue_add_user_name(String clue_add_user_name) {
		this.clue_add_user_name = clue_add_user_name;
	}

	public String getClue_add_user_phone() {
		return clue_add_user_phone;
	}

	public void setClue_add_user_phone(String clue_add_user_phone) {
		this.clue_add_user_phone = clue_add_user_phone;
	}

	public String getClue_add_user_role() {
		return clue_add_user_role;
	}

	public void setClue_add_user_role(String clue_add_user_role) {
		this.clue_add_user_role = clue_add_user_role;
	}

	public String getVin_code() {
		return vin_code;
	}

	public void setVin_code(String vin_code) {
		this.vin_code = vin_code;
	}

	public int getTrans_type() {
		return trans_type;
	}

	public void setTrans_type(int trans_type) {
		this.trans_type = trans_type;
	}

	public int getPay_status() {
		return pay_status;
	}

	public void setPay_status(int pay_status) {
		this.pay_status = pay_status;
	}

	public String getPay_status_text() {
		return pay_status_text;
	}

	public void setPay_status_text(String pay_status_text) {
		this.pay_status_text = pay_status_text;
	}


	public int getLast_pay_status() {
		return last_pay_status;
	}

	public void setLast_pay_status(int last_pay_status) {
		this.last_pay_status = last_pay_status;
	}

	public String getLast_pay_status_text() {
		return last_pay_status_text;
	}

	public void setLast_pay_status_text(String last_pay_status_text) {
		this.last_pay_status_text = last_pay_status_text;
	}

	public String getLast_pay_fail_reason() {
		return last_pay_fail_reason;
	}

	public void setLast_pay_fail_reason(String last_pay_fail_reason) {
		this.last_pay_fail_reason = last_pay_fail_reason;
	}

	public long getIntroduce_free() {
		return introduce_free;
	}

	public void setIntroduce_free(long introduce_free) {
		this.introduce_free = introduce_free;
	}

	public String getHas_pay() {
		if(TextUtils.isEmpty(has_pay) || "null".equals(has_pay.trim().toLowerCase())){
			return "0";
		}
		return has_pay;
	}

	public void setHas_pay(String has_pay) {
		this.has_pay = has_pay;
	}

	public String getBack_price() {
		return back_price;
	}

	public void setBack_price(String back_price) {
		this.back_price = back_price;
	}

	public String getTotal_back_price() {
		if(TextUtils.isEmpty(total_back_price) || "null".equals(total_back_price.trim().toLowerCase())){
			return "0";
		}
		return total_back_price;
	}

	public void setTotal_back_price(String total_back_price) {
		this.total_back_price = total_back_price;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
