package com.haoche51.checker.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mac on 16/1/25.
 */
public class OfflineSoldTaskEntity {
	private int stock_id;// int 库存id
	private String title;//string 车源
	private int trans_type;//支付类型枚举
	private String receive_user_name;//李四 收车人姓名
	private String receive_user_role;//地收 收车人角色
	private String receive_user_phone; //18559938374 收车人电话
	private String task_num;//SC1234 收车编号
	private String plate_number;//'京A2455' 车牌号
	private long price;//100000 报价，单位“元”
	private long cheap_price;//80000 底价，单位“元”
	private int status;//状态
	private String status_text;//待回款 状态文本显示
	private String plate_time;//2013 年5月 上牌时间
	private String mile;//12.4 行驶里程,单位“万公里”
	private int transfer_count;//3 过户次数
	private long sold_price;//30000 售出价格，单位“元”
	private String sold_time;//1 月1日 1130 售出时间
	private String buyer_phone;//18449938474 买家电话
	private String buyer_name;//老大 买家姓名
	private int vehicle_source_id;//车源id
	private List<FlowChartEntity> flow_chart;//任务进度

	/**
	 * 售出方式
	 */
	private String sold_type;


	/**
	 * 负责地销姓名
	 */
	private String saler_name;

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

	public long getCheap_price() {
		return cheap_price;
	}

	public void setCheap_price(long cheap_price) {
		this.cheap_price = cheap_price;
	}

	public String getMile() {
		return mile;
	}

	public void setMile(String mile) {
		this.mile = mile;
	}

	public String getPlate_number() {
		return plate_number;
	}

	public void setPlate_number(String plate_number) {
		this.plate_number = plate_number;
	}

	public String getPlate_time() {
		return plate_time;
	}

	public void setPlate_time(String plate_time) {
		this.plate_time = plate_time;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public String getReceive_user_name() {
		return receive_user_name;
	}

	public void setReceive_user_name(String receive_user_name) {
		this.receive_user_name = receive_user_name;
	}

	public String getReceive_user_phone() {
		return receive_user_phone;
	}

	public void setReceive_user_phone(String receive_user_phone) {
		this.receive_user_phone = receive_user_phone;
	}

	public String getReceive_user_role() {
		return receive_user_role;
	}

	public void setReceive_user_role(String receive_user_role) {
		this.receive_user_role = receive_user_role;
	}

	public long getSold_price() {
		return sold_price;
	}

	public void setSold_price(long sold_price) {
		this.sold_price = sold_price;
	}

	public String getSold_time() {
		return sold_time;
	}

	public void setSold_time(String sold_time) {
		this.sold_time = sold_time;
	}

	public String getStatus_text() {
		return status_text;
	}

	public void setStatus_text(String status_text) {
		this.status_text = status_text;
	}

	public int getStock_id() {
		return stock_id;
	}

	public void setStock_id(int stock_id) {
		this.stock_id = stock_id;
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

	public int getTransfer_count() {
		return transfer_count;
	}

	public void setTransfer_count(int transfer_count) {
		this.transfer_count = transfer_count;
	}

	public int getVehicle_source_id() {
		return vehicle_source_id;
	}

	public void setVehicle_source_id(int vehicle_source_id) {
		this.vehicle_source_id = vehicle_source_id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public class FlowChartEntity implements Serializable {
		private int is_over;//是否完成 0未完成 1已完成
		private String text;//进度名称

		public int getIs_over() {
			return is_over;
		}

		public void setIs_over(int is_over) {
			this.is_over = is_over;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}
	}

	public List<FlowChartEntity> getFlow_chart() {
		return flow_chart;
	}

	public void setFlow_chart(List<FlowChartEntity> flow_chart) {
		this.flow_chart = flow_chart;
	}

	public int getTrans_type() {
		return trans_type;
	}

	public void setTrans_type(int trans_type) {
		this.trans_type = trans_type;
	}

	public String getSold_type() {
		return sold_type;
	}

	public void setSold_type(String sold_type) {
		this.sold_type = sold_type;
	}

	public String getSaler_name() {
		return saler_name;
	}

	public void setSaler_name(String saler_name) {
		this.saler_name = saler_name;
	}
}
