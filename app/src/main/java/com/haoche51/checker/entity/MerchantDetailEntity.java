package com.haoche51.checker.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 商家详情
 * Created by wufx on 2016/3/1.
 */
public class MerchantDetailEntity implements Parcelable {
	/**
	 * 商家id
	 */
	private int id;

	/**
	 * 商家名称
	 */
	private String name;

	/**
	 * 商家联系人姓名
	 */
	private String contact_name;

	/**
	 * 商家联系人电话
	 */
	private String contact_phone;

	private int city_id;//城市id

	/**
	 * 商家地址
	 */
	private String address;

	/**
	 * 上次维护时间
	 */
	private String maintain_time;

	/**
	 * 在售车源数量
	 */
	private int sell_car;

	/**
	 * 维护人id
	 */
	private int crm_user_id;

	/**
	 * 维护人姓名
	 */
	private String crm_user_name;

	/**
	 * 维护人 手机
	 */
	private String crm_user_phone;

	/**
	 * 维护备注
	 */
	private String remark;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContact_name() {
		return contact_name;
	}

	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}

	public String getContact_phone() {
		return contact_phone;
	}

	public void setContact_phone(String contact_phone) {
		this.contact_phone = contact_phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMaintain_time() {
		return maintain_time;
	}

	public void setMaintain_time(String maintain_time) {
		this.maintain_time = maintain_time;
	}

	public int getSell_car() {
		return sell_car;
	}

	public int getCity_id() {
		return city_id;
	}

	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}

	public void setSell_car(int sell_car) {
		this.sell_car = sell_car;
	}

	public int getCrm_user_id() {
		return crm_user_id;
	}

	public void setCrm_user_id(int crm_user_id) {
		this.crm_user_id = crm_user_id;
	}

	public String getCrm_user_name() {
		return crm_user_name;
	}

	public void setCrm_user_name(String crm_user_name) {
		this.crm_user_name = crm_user_name;
	}

	public String getCrm_user_phone() {
		return crm_user_phone;
	}

	public void setCrm_user_phone(String crm_user_phone) {
		this.crm_user_phone = crm_user_phone;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 内容描述接口，基本不用管，默认返回0就可以了
	 *
	 * @return
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * 写入接口函数，打包
	 *
	 * @param dest
	 * @param flags
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(contact_name);
		dest.writeString(contact_phone);
		dest.writeInt(city_id);
		dest.writeString(address);
		dest.writeString(maintain_time);
		dest.writeInt(sell_car);
		dest.writeInt(crm_user_id);
		dest.writeString(crm_user_name);
		dest.writeString(crm_user_phone);
		dest.writeString(remark);
	}

	public static final Parcelable.Creator<MerchantDetailEntity> CREATOR = new Creator<MerchantDetailEntity>() {

		@Override
		public MerchantDetailEntity createFromParcel(Parcel source) {
			//实现从source中创建出类的实例的功能
			MerchantDetailEntity detailEntity = new MerchantDetailEntity();
			detailEntity.id = source.readInt();
			detailEntity.name = source.readString();
			detailEntity.contact_name = source.readString();
			detailEntity.contact_phone = source.readString();
			detailEntity.city_id = source.readInt();
			detailEntity.address = source.readString();
			detailEntity.maintain_time = source.readString();
			detailEntity.sell_car = source.readInt();
			detailEntity.crm_user_id = source.readInt();
			detailEntity.crm_user_name = source.readString();
			detailEntity.crm_user_phone = source.readString();
			detailEntity.remark = source.readString();
			return detailEntity;
		}

		@Override
		public MerchantDetailEntity[] newArray(int size) {
			return new MerchantDetailEntity[size];
		}


	};
}
