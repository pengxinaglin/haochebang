package com.haoche51.checker.entity;

import java.util.List;

/**
 * Created by mac on 15/11/4.
 */
public class TransFailBuyerEntity {

	/**
	 * phone : 13800000002
	 * name : 韩女士
	 * create_time : 1446467054
	 * comment : 上门带看失败，自动加入地销客户列表
	 * subscribe_rule : {"subscribe_series":[{"id":"577","name":"蒙迪欧-致胜"},{"id":"657","name":"科鲁兹"},{"id":"982","name":"英朗"}],"emission":1,"gearbox":2,"year":[3,5],"price":[12,15]}
	 * status : 2
	 * cancel_trans_status : 0
	 * abort_type : 3
	 */

	private String phone;
	private String name;
	private int create_time;//创建时间
	private String comment;
	private SubscribeRuleEntity subscribe_rule;
	private int status;//预约状态，0,提交预约; 1,取消试驾订单; 2,试驾完成后取消; 3,试驾完成后并支付定金 4,试驾完成后并全额支付 5,交易完成
	private int cancel_trans_status;//毁约操作状态 取值有：0初始 1待确认 2已确认
	private int abort_type;//abort_type：取消交易类型 1未上门 2车没看上 3价格没谈成 4其他原因
	private int level;

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCreate_time(int create_time) {
		this.create_time = create_time;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setSubscribe_rule(SubscribeRuleEntity subscribe_rule) {
		this.subscribe_rule = subscribe_rule;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setCancel_trans_status(int cancel_trans_status) {
		this.cancel_trans_status = cancel_trans_status;
	}

	public void setAbort_type(int abort_type) {
		this.abort_type = abort_type;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getPhone() {
		return phone;
	}

	public String getName() {
		return name;
	}

	public int getCreate_time() {
		return create_time;
	}

	public String getComment() {
		return comment;
	}

	public SubscribeRuleEntity getSubscribe_rule() {
		return subscribe_rule;
	}

	public int getStatus() {
		return status;
	}

	public int getCancel_trans_status() {
		return cancel_trans_status;
	}

	public int getAbort_type() {
		return abort_type;
	}

	public int getLevel() {
		return level;
	}



	public static class SubscribeRuleEntity {
		/**
		 * subscribe_series : [{"id":"577","name":"蒙迪欧-致胜"},{"id":"657","name":"科鲁兹"},{"id":"982","name":"英朗"}]
		 * emission : 1
		 * gearbox : 2
		 * year : [3,5]
		 * price : [12,15]
		 */

		private int emission;//排放标准
		private int gearbox;//变数箱
		private List<SubscribeSeriesEntity> subscribe_series;//订阅车系信息包含id 和name
		private List<Integer> year;//年份区间[low,high]
		private List<Float> price;//价格区间[low,high]

		public void setEmission(int emission) {
			this.emission = emission;
		}

		public void setGearbox(int gearbox) {
			this.gearbox = gearbox;
		}

		public void setSubscribe_series(List<SubscribeSeriesEntity> subscribe_series) {
			this.subscribe_series = subscribe_series;
		}

		public void setYear(List<Integer> year) {
			this.year = year;
		}

		public void setPrice(List<Float> price) {
			this.price = price;
		}

		public int getEmission() {
			return emission;
		}

		public int getGearbox() {
			return gearbox;
		}

		public List<SubscribeSeriesEntity> getSubscribe_series() {
			return subscribe_series;
		}

		public List<Integer> getYear() {
			return year;
		}

		public List<Float> getPrice() {
			return price;
		}

		public static class SubscribeSeriesEntity {
			/**
			 * id : 577
			 * name : 蒙迪欧-致胜
			 */

			private String id;//车系id
			private String name;//车系名称

			public void setId(String id) {
				this.id = id;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getId() {
				return id;
			}

			public String getName() {
				return name;
			}
		}
	}
}
