package com.haoche51.checker.entity;

import java.util.List;

/**
 * 线下售出实体类
 * Created by wufx on 2016/1/29.
 */
public class OfflineSoldEntity {
	/**
	 * 库存id
	 */
	private int stock_id;

	/**
	 * 支付类型
	 */
	private int trans_type;

	/**
	 * 收车编号
	 */
	private String task_num;

	/**
	 * 买家姓名
	 */
	private String buyer_name;

	/**
	 * 买家电话
	 */
	private String buyer_phone;

	/**
	 * 卖出价格
	 */
	private int sold_price;

	/**
	 * 是否过户，0：否、1：是
	 */
	private int is_transfer;

	/**
	 * 过户费承担(0:没选择、1：公司、2：买家)
	 */
	private int transfer_free_payer;

	/**
	 * 过户费
	 */
	private int transfer_free;

	/**
	 * 转账方式
	 */
	private String trans_way;

	/**
	 * 照片集合
	 */
	private List<PhotoEntity> photoList;

	/**
	 * 写给财务的话
	 */
	private String sold_remark;

	/**
	 * 照片路径集合
	 */
	private List<String> photoPathList;

	/**
	 * 负责地销姓名
	 */
	private String saler_name;

	/**
	 * 负责地销id
	 */
	private int saler_id;

	/**
	 *  售出渠道
	 */
	private int sale_channel;

	public int getStock_id() {
		return stock_id;
	}

	public void setStock_id(int stock_id) {
		this.stock_id = stock_id;
	}

	public String getBuyer_name() {
		return buyer_name;
	}

	public void setBuyer_name(String buyer_name) {
		this.buyer_name = buyer_name;
	}

	public String getBuyer_phone() {
		return buyer_phone;
	}

	public void setBuyer_phone(String buyer_phone) {
		this.buyer_phone = buyer_phone;
	}

	public int getSold_price() {
		return sold_price;
	}

	public void setSold_price(int sold_price) {
		this.sold_price = sold_price;
	}

	public int getIs_transfer() {
		return is_transfer;
	}

	public void setIs_transfer(int is_transfer) {
		this.is_transfer = is_transfer;
	}

	public int getTransfer_free_payer() {
		return transfer_free_payer;
	}

	public void setTransfer_free_payer(int transfer_free_payer) {
		this.transfer_free_payer = transfer_free_payer;
	}

	public int getTransfer_free() {
		return transfer_free;
	}

	public void setTransfer_free(int transfer_free) {
		this.transfer_free = transfer_free;
	}

	public String getTrans_way() {
		return trans_way;
	}

	public void setTrans_way(String trans_way) {
		this.trans_way = trans_way;
	}

	public List<PhotoEntity> getPhotoList() {
		return photoList;
	}

	public void setPhotoList(List<PhotoEntity> photoList) {
		this.photoList = photoList;
	}

	public List<String> getPhotoPathList() {
		return photoPathList;
	}

	public void setPhotoPathList(List<String> photoPathList) {
		this.photoPathList = photoPathList;
	}

	public String getSold_remark() {
		return sold_remark;
	}

	public void setSold_remark(String sold_remark) {
		this.sold_remark = sold_remark;
	}

	public String getTask_num() {
		return task_num;
	}

	public void setTask_num(String task_num) {
		this.task_num = task_num;
	}

	public int getTrans_type() {
		return trans_type;
	}

	public void setTrans_type(int trans_type) {
		this.trans_type = trans_type;
	}

	public String getSaler_name() {
		return saler_name;
	}

	public void setSaler_name(String saler_name) {
		this.saler_name = saler_name;
	}

	public int getSaler_id() {
		return saler_id;
	}

	public void setSaler_id(int saler_id) {
		this.saler_id = saler_id;
	}

	public int getSale_channel() {
		return sale_channel;
	}

	public void setSale_channel(int sale_channel) {
		this.sale_channel = sale_channel;
	}
}
