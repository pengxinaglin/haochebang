package com.haoche51.checker.activity.purchase;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.entity.PurchaseTaskEntity;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.ToastUtil;
import com.mobsandgeeks.saripaar.annotation.Required;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.math.BigDecimal;

/**
 * 任务失败
 * Created by wufx on 2016/1/13.
 */
public class PurchaseFailedActivity extends CommonTitleBaseActivity {

  /**
   * 心里价位
   */
  @ViewInject(R.id.ed_heart_price)
  @Required(order = 1, message = "心里价位不能为空")
  private EditText ed_heart_price;

  /**
   * 我方出价
   */
  @ViewInject(R.id.ed_our_price)
  @Required(order = 2, message = "我方出价不能为空")
  private EditText ed_our_price;

  /**
   * 同行出价
   */
  @ViewInject(R.id.ed_trade_price)
  @Required(order = 3, message = "同行出价不能为空")
  private EditText ed_trade_price;

  /**
   * 卖车周期
   */
  @ViewInject(R.id.ed_sell_car_cycle)
  @Required(order = 4, message = "卖车周期不能为空")
  private EditText ed_sell_car_cycle;

  /**
   * 失败原因
   */
  @ViewInject(R.id.ed_fail_reason)
  @Required(order = 5, message = "失败原因不能为空")
  private EditText ed_fail_reason;

  /**
   * 任务id，待处理详情界面传过来的
   */
  private int taskId;


  @Override
  public View getHCContentView() {
    return View.inflate(this, R.layout.activity_task_failed, null);
  }

  @Override
  public void initContentView(Bundle saveInstanceState) {
    taskId = getIntent().getIntExtra("taskId", 0);
    x.view().inject(this);
    ed_heart_price.requestFocus();
  }

  @Override
  public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
    mTitle.setText(getString(R.string.hc_task_failed));
  }

  /**
   * 添加任务失败信息
   */
  @Event(R.id.btn_commit)
  private void commit(View v) {
    try{
      validator.validate();
    }catch (Exception e){
      e.printStackTrace();
    }

  }

  /**
   * 校验成功
   */
  @Override
  public void onValidationSucceeded() {
    super.onValidationSucceeded();
    PurchaseTaskEntity purchaseTask = new PurchaseTaskEntity();
    purchaseTask.setTask_id(taskId);

    //心里价位
    BigDecimal unitWan = new BigDecimal(10000);
    String heart_price_str = ed_heart_price.getText().toString().trim();
    if (!TextUtils.isEmpty(heart_price_str)) {
      purchaseTask.setHope_price(new BigDecimal(heart_price_str).multiply(unitWan).intValue());
    }

    //我方出价
    String our_price_str = ed_our_price.getText().toString().trim();
    if (!TextUtils.isEmpty(our_price_str)) {
      purchaseTask.setOur_price(new BigDecimal(our_price_str).multiply(unitWan).intValue());
    }

    //同行出价
    String trade_price_str = ed_trade_price.getText().toString().trim();
    if (!TextUtils.isEmpty(trade_price_str)) {
      purchaseTask.setPeer_price(new BigDecimal(trade_price_str).multiply(unitWan).intValue());
    }

    //卖车周期
    String sell_car_cycle_str = ed_sell_car_cycle.getText().toString().trim();
    if (!TextUtils.isEmpty(sell_car_cycle_str)) {
      purchaseTask.setSell_cycle(sell_car_cycle_str);
    }

    //失败原因
    String fail_reason_str = ed_fail_reason.getText().toString().trim();
    if (!TextUtils.isEmpty(fail_reason_str)) {
      purchaseTask.setFail_reason(fail_reason_str);
    }

    OKHttpManager.getInstance().post(HCHttpRequestParam.addPurchaseFailed(purchaseTask), this, 0);

  }


  public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
    if (HttpConstants.ACTION_FAIL_TASK.equals(action)) {
      switch (response.getErrno()) {
        case 0://0：表示接口请求成功
          ToastUtil.showInfo("提交成功");
          setResult(RESULT_OK);
          finish();
          break;
        default://1：发生错误
          ToastUtil.showInfo(response.getErrmsg());
          break;
      }
    }
  }

}
