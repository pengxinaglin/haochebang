package com.haoche51.checker.entity;

/**
 * Created by mac on 16/2/19.
 */
public class BackPushDetailEntity {
  private int task_id;
  private int repair_status;//1待整备、3整备中、2、4 已完成
  private int page_type;//1:待跟进任务、2、已预约收车任务、3：收车其他详情页、4：整备详情页)

  public int getTask_id() {
    return task_id;
  }

  public void setTask_id(int task_id) {
    this.task_id = task_id;
  }

  public int getPage_type() {
    return page_type;
  }

  public void setPage_type(int page_type) {
    this.page_type = page_type;
  }

  public int getRepair_status() {
    return repair_status;
  }

  public void setRepair_status(int repair_status) {
    this.repair_status = repair_status;
  }
}
