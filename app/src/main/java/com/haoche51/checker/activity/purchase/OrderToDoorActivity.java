package com.haoche51.checker.activity.purchase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.entity.PurchaseTaskEntity;
import com.haoche51.checker.entity.PurchaseTaskShortEntity;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.DisplayUtils;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;
import com.haoche51.checker.util.UnixTimeUtil;
import com.mobsandgeeks.saripaar.annotation.Required;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 预约上门
 * Created by wufx on 2016/1/13.
 */
public class OrderToDoorActivity extends CommonTitleBaseActivity {
  public static final String INTENT_MODIFY_ORDER_TO_DOOR_KEY = "is_modify_appointment";
  /**
   * 预约时间
   */
  @ViewInject(R.id.tv_order_time)
  @Required(order = 1, message = "预约时间不能为空")
  private TextView tv_order_time;
  /**
   * 预约地点
   */
  @ViewInject(R.id.ed_order_place)
  @Required(order = 2, message = "预约地点不能为空")
  private EditText ed_order_place;
  @ViewInject(R.id.btn_order)
  private Button btn_order;
  /**
   * 任务id，待处理详情界面传过来的
   */
  private int taskId;

  @Override
  public View getHCContentView() {
    return View.inflate(this, R.layout.activity_order_to_door, null);
  }

  @Override
  public void initContentView(Bundle saveInstanceState) {
    taskId = getIntent().getIntExtra("taskId", 0);
    x.view().inject(this);
    if (getIntent().getBooleanExtra(INTENT_MODIFY_ORDER_TO_DOOR_KEY, false)) {
      btn_order.setText(getString(R.string.hc_purchasetask_midify_appointment));
      PurchaseTaskEntity mTask =  getIntent().getParcelableExtra("mTask");
      if (mTask != null) {
        tv_order_time.setText(UnixTimeUtil.format(mTask.getAppoint_unix()));
        ed_order_place.setText(mTask.getAppoint_address());
        ed_order_place.setSelection(mTask.getAppoint_address().length());
      }
    }
  }

  @Override
  public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
    mTitle.setText(getString(R.string.hc_order_to_door));
  }


  /**
   * 预约上门按钮点击事件
   */
  @Event(R.id.btn_order)
  private void commitOrder(View v) {
    //检查必输项
    validator.validate();
  }


  /**
   * 预约时间点击事件
   */
  @Event(R.id.tv_order_time)
  private void clickTimeWhell(View v) {
    //初始化时间滚轮
    DisplayUtils.displayTimeWhell(this, tv_order_time, R.string.select_order_time);
  }


  /**
   * 校验成功
   */
  @Override
  public void onValidationSucceeded() {
    super.onValidationSucceeded();
    PurchaseTaskShortEntity taskShortEntity = new PurchaseTaskShortEntity();
    taskShortEntity.setTask_id(taskId);

    //预约时间
    taskShortEntity.setAppoint_time(tv_order_time.getText().toString().trim());

    //预约地点
    taskShortEntity.setAppoint_address(ed_order_place.getText().toString().trim());
    ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
    //提交给服务器
    OKHttpManager.getInstance().post(HCHttpRequestParam.addOrderToDoor(taskShortEntity), this, 0);
  }


  @Override
  public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
    ProgressDialogUtil.closeProgressDialog();
    if (HttpConstants.ACTION_APPOINT_TASK.equals(action)) {
      switch (response.getErrno()) {
        case 0://0：表示接口请求成功
          ToastUtil.showInfo("预约成功！");
          if (getIntent().getBooleanExtra(INTENT_MODIFY_ORDER_TO_DOOR_KEY, false)) {
            Intent intent = new Intent();
            intent.putExtra("appoint_time", tv_order_time.getText().toString());
            intent.putExtra("appoint_address", ed_order_place.getText().toString());
            setResult(RESULT_OK, intent);
          } else {
            setResult(RESULT_OK);
          }
          finish();
          break;
        default://1：发生错误
          ToastUtil.showInfo(response.getErrmsg());
          break;
      }
    }
  }

  @Override
  protected void onDestroy() {
    ProgressDialogUtil.closeProgressDialog();
    super.onDestroy();
  }
}
