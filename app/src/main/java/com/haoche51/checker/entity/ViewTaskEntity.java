package com.haoche51.checker.entity;

public class ViewTaskEntity extends BaseEntity {

  public ViewTaskEntity() {

  }

  private int id = 0;
  private int status = 0; // 任务状态  0,提交预约; 1,试驾取消订单; 2,驾完成后取消; 3,试驾完成后并支付定金 4,试驾完成后并全额支付 5,交易完成'
  private int appointment_starttime = 0;
  private String place = "";
  private String seller_name = "";
  private String seller_phone = "";
  private String buyer_name = "";
  private String buyer_phone = "";
  private int vehicle_source_id = 0;
  private String vehicle_name = "";
  private int price = 0; // 成交价
  private int prepay = 0; // 买家给公司定金
  private int seller_prepay = 0; //买家给车主定金
  private int seller_company_prepay = 0;//车主给公司定金
  private int transfer_mode = 0; //过户方式 0，公司过户 1 自行过户
  private int commission = 0;  //佣金
  private int transfer_time = 0; // 过户时间
  private String transfer_place = ""; //过户地点
  private int saller_confirm = 0; //0， 已确认过户时间，1 销售确认过户时间，
  private int cancel_status = 0; // 取消原因
  private String comment = "";
  private int view_registration = 0;//是否见到登记证。 0 未见到，1 见到
  private String saller_name = "";//销售名称
  private int trans_count;//看车次数
  private String saler_name;//销售
  private String saler_phone;//销售电话
  private String checker_name;//评估师名称
  private String checker_phone;//评估师电话
  private int chejinding_status;// 鉴定报告 0:订单未获取 1:订单获取中 2:订单获取成功 3:订单获取失败
  private String cjd_url;//车鉴定报告url
  private String buyer_info = "";
  private String ext_info = "";
  private int create_time;//创建时间
  private int cash_back;//返现金额
  private int online_time;//上线时间
  private float seller_price; //车主报价
  private float cheap_price; //底价
//	private Che300Entity che300_price; //che300 价格

  public int getTransfer_time() {
    return transfer_time;
  }

  public void setTransfer_time(int transfer_time) {
    this.transfer_time = transfer_time;
  }

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

  public String getPlace() {
    return place;
  }

  public void setPlace(String place) {
    this.place = place;
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

  public int getVehicle_source_id() {
    return vehicle_source_id;
  }

  public void setVehicle_source_id(int vehicle_source_id) {
    this.vehicle_source_id = vehicle_source_id;
  }

  public String getVehicle_name() {
    return vehicle_name;
  }

  public void setVehicle_name(String vehicle_name) {
    this.vehicle_name = vehicle_name;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public int getPrepay() {
    return prepay;
  }

  public void setPrepay(int prepay) {
    this.prepay = prepay;
  }

  public int getSeller_prepay() {
    return seller_prepay;
  }

  public void setSeller_prepay(int seller_prepay) {
    this.seller_prepay = seller_prepay;
  }

  public int getSeller_company_prepay() {
    return seller_company_prepay;
  }

  public void setSeller_company_prepay(int seller_company_prepay) {
    this.seller_company_prepay = seller_company_prepay;
  }

  public int getTransfer_mode() {
    return transfer_mode;
  }

  public void setTransfer_mode(int transfer_mode) {
    this.transfer_mode = transfer_mode;
  }

  public int getCommission() {
    return commission;
  }

  public void setCommission(int commission) {
    this.commission = commission;
  }

  public String getTransfer_place() {
    return transfer_place;
  }

  public void setTransfer_place(String transfer_place) {
    this.transfer_place = transfer_place;
  }

  public int getSaller_confirm() {
    return saller_confirm;
  }

  public void setSaller_confirm(int saller_confirm) {
    this.saller_confirm = saller_confirm;
  }

  public int getCancel_status() {
    return cancel_status;
  }

  public void setCancel_status(int cancel_status) {
    this.cancel_status = cancel_status;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getBuyer_info() {
    return buyer_info;
  }

  public void setBuyer_info(String buyer_info) {
    this.buyer_info = buyer_info;
  }

  public String getSaller_name() {
    return saller_name;
  }

  public void setSaller_name(String saller_name) {
    this.saller_name = saller_name;
  }

  public int getView_registration() {
    return view_registration;
  }

  public void setView_registration(int view_registration) {
    this.view_registration = view_registration;
  }

  public String getExt_info() {
    return ext_info;
  }

  public void setExt_info(String ext_info) {
    this.ext_info = ext_info;
  }

  public int getTrans_count() {
    return trans_count;
  }

  public void setTrans_count(int trans_count) {
    this.trans_count = trans_count;
  }

  public String getSaler_name() {
    return saler_name;
  }

  public void setSaler_name(String saler_name) {
    this.saler_name = saler_name;
  }

  public String getSaler_phone() {
    return saler_phone;
  }

  public void setSaler_phone(String saler_phone) {
    this.saler_phone = saler_phone;
  }

  public String getChecker_name() {
    return checker_name;
  }

  public void setChecker_name(String checker_name) {
    this.checker_name = checker_name;
  }

  public String getChecker_phone() {
    return checker_phone;
  }

  public void setChecker_phone(String checker_phone) {
    this.checker_phone = checker_phone;
  }

  public int getChejinding_status() {
    return chejinding_status;
  }

  public void setChejinding_status(int chejinding_status) {
    this.chejinding_status = chejinding_status;
  }

  public String getCjd_url() {
    return cjd_url;
  }

  public void setCjd_url(String cjd_url) {
    this.cjd_url = cjd_url;
  }

  public int getOnline_time() {
    return online_time;
  }

  public void setOnline_time(int online_time) {
    this.online_time = online_time;
  }

  public int getCreate_time() {
    return create_time;
  }

  public void setCreate_time(int create_time) {
    this.create_time = create_time;
  }

  public int getCash_back() {
    return cash_back;
  }

  public void setCash_back(int cash_back) {
    this.cash_back = cash_back;
  }

  public void setSeller_price(float seller_price) {
    this.seller_price = seller_price;
  }

  public void setCheap_price(float cheap_price) {
    this.cheap_price = cheap_price;
  }

  public float getSeller_price() {
    return seller_price;
  }

  public float getCheap_price() {
    return cheap_price;
  }

}
