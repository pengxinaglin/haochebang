package com.haoche51.checker.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mac on 16/1/23.
 */
public class HostlingTaskEntity implements Serializable {


	/**
	 * stock_id : 10
	 * title : 福特 福克斯 2012款 三厢 1.6L
	 * worker_name : 王俊东
	 * worker_phone : 188888888888
	 * back_price : 1000000
	 * sold_price : 70000
	 * cheap_price : 60000
	 * task_num : SC12345
	 * plate_number : 京A2345
	 * plate_time : 2011年9月
	 * mile : 12.5
	 * transfer_count : 2
	 * project : [{"repair_id":"int","name":"喷漆","pre_image":["",""],"after_image":["",""],"real_price":200,"expect_price":205}]
	 */

	private int stock_id;//库存id
	private String title;//任务名称
	private String worker_name;//地收姓名
	private String worker_phone;//地收电话
	private int back_price;//收购价格，单位“元”
	private int sold_price;//出售价格，单位“元”
	private int cheap_price;//出售底价，单位“元”
	private String task_num;//任务编号
	private String plate_number;//车牌号
	private String plate_time; //上牌时间
	private int pick_up_time;//提车时间
	private double mile;//行驶里程，单位“万公里"
	private int transfer_count;//过户次数
	/**
	 * repair_id : int
	 * name : 喷漆
	 * pre_image : ["",""]
	 * after_image : ["",""]
	 * real_price : 200
	 * expect_price : 205
	 */
	private List<ProjectEntity> project;//整备项目信息数据
	private int repair_status;//'已整备', //整备状态值
	private String repair_status_text;//'已整备', //整备状态文本值
	private long repair_free;//600, //预计整备费用总计、单位"元"
	private long real_repair_free;//700, //实际整备费用总计、单位"元"
	private List<FlowChartEntity> flow_chart;//任务进度

	public long getRepair_free() {
		return repair_free;
	}

	public void setRepair_free(long repair_free) {
		this.repair_free = repair_free;
	}

	public void setStock_id(int stock_id) {
		this.stock_id = stock_id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setWorker_name(String worker_name) {
		this.worker_name = worker_name;
	}

	public void setWorker_phone(String worker_phone) {
		this.worker_phone = worker_phone;
	}

	public void setBack_price(int back_price) {
		this.back_price = back_price;
	}

	public void setSold_price(int sold_price) {
		this.sold_price = sold_price;
	}

	public void setCheap_price(int cheap_price) {
		this.cheap_price = cheap_price;
	}

	public void setTask_num(String task_num) {
		this.task_num = task_num;
	}

	public void setPlate_number(String plate_number) {
		this.plate_number = plate_number;
	}

	public void setPlate_time(String plate_time) {
		this.plate_time = plate_time;
	}

	public void setMile(double mile) {
		this.mile = mile;
	}

	public void setTransfer_count(int transfer_count) {
		this.transfer_count = transfer_count;
	}

	public void setProject(List<ProjectEntity> project) {
		this.project = project;
	}

	public int getStock_id() {
		return stock_id;
	}

	public String getTitle() {
		return title;
	}

	public String getWorker_name() {
		return worker_name;
	}

	public String getWorker_phone() {
		return worker_phone;
	}

	public int getBack_price() {
		return back_price;
	}

	public int getSold_price() {
		return sold_price;
	}

	public int getCheap_price() {
		return cheap_price;
	}

	public String getTask_num() {
		return task_num;
	}

	public String getPlate_number() {
		return plate_number;
	}

	public String getPlate_time() {
		return plate_time;
	}

	public double getMile() {
		return mile;
	}

	public int getTransfer_count() {
		return transfer_count;
	}

	public List<ProjectEntity> getProject() {
		return project;
	}

	public static class ProjectEntity implements Serializable {
		private boolean isFocus;//仅app使用，crm不存在此字段
		private int repair_id;//整备id
		private String name;//整备名称
		private String real_price;//整备实际耗费多少钱，单位"元"
		private String expect_price;//整备预计耗费多少钱，单位“元”
		private List<String> pre_image;//整备前的图片url数组
		private List<String> after_image;//整备后的图片url数组
		/**
		 * 是否勾选未整备（0：否、1：是)
		 */
		private int no_repair;
		private int is_over;//0 / 1, //（0：还没提交整备完成、1：已经提交过整备完成了）

		public int getIs_over() {
			return is_over;
		}

		public void setIs_over(int is_over) {
			this.is_over = is_over;
		}

		public void setRepair_id(int repair_id) {
			this.repair_id = repair_id;
		}

		public void setName(String name) {
			this.name = name;
		}


		public void setPre_image(List<String> pre_image) {
			this.pre_image = pre_image;
		}

		public int getRepair_id() {
			return repair_id;
		}

		public String getName() {
			return name;
		}


		public List<String> getPre_image() {
			return pre_image;
		}

		public List<String> getAfter_image() {
			return after_image;
		}

		public void setAfter_image(List<String> after_image) {
			this.after_image = after_image;
		}

		public String getReal_price() {
			return real_price;
		}

		public void setReal_price(String real_price) {
			this.real_price = real_price;
		}

		public boolean isFocus() {
			return isFocus;
		}

		public void setFocus(boolean focus) {
			isFocus = focus;
		}

		public int getNo_repair() {
			return no_repair;
		}

		public void setNo_repair(int no_repair) {
			this.no_repair = no_repair;
		}

		public String getExpect_price() {
			return expect_price;
		}

		public void setExpect_price(String expect_price) {
			this.expect_price = expect_price;
		}
	}

	public long getReal_repair_free() {
		return real_repair_free;
	}

	public void setReal_repair_free(long real_repair_free) {
		this.real_repair_free = real_repair_free;
	}

	public String getRepair_status_text() {
		return repair_status_text;
	}

	public void setRepair_status_text(String repair_status_text) {
		this.repair_status_text = repair_status_text;
	}


	public int getPick_up_time() {
		return pick_up_time;
	}

	public void setPick_up_time(int pick_up_time) {
		this.pick_up_time = pick_up_time;
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

	public int getRepair_status() {
		return repair_status;
	}

	public void setRepair_status(int repair_status) {
		this.repair_status = repair_status;
	}
}
