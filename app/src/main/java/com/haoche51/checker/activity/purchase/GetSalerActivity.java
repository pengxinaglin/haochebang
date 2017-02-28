package com.haoche51.checker.activity.purchase;

import android.content.Intent;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.evaluate.GetCheckerActivity;
import com.haoche51.checker.entity.LocalCheckerEntity;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.ToastUtil;

import okhttp3.Call;

/**
 * 获取同城地销
 * Created by wufx on 15/11/24.
 */
public class GetSalerActivity extends GetCheckerActivity {

  @Override
  protected Call loadData() {
    mTitle.setText(getString(R.string.saler_list));
    //请求获取本地地销人员
    return OKHttpManager.getInstance().post(HCHttpRequestParam.getSameCitySalerList(), this, 0);
  }

  @Override
  protected void onConfirmClick(LocalCheckerEntity chooseChecker) {
    //判断是否已选择
    if (chooseChecker == null) {
      ToastUtil.showInfo(getString(R.string.hc_choose_saler));
      return;
    }
    //将值返回给发起界面
    Intent localIntent = new Intent();
    localIntent.putExtra("choose_saler_id", chooseChecker.getId());
    localIntent.putExtra("choose_saler_name", chooseChecker.getName());
    setResult(RESULT_OK, localIntent);
    finish();
  }

  @Override
  public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
    super.onHttpComplete(action,requestId,response,error);
    if (action.equals(HttpConstants.ACTION_GET_SAME_CITY_SALER)) {
      responseGetCheckerList(response);
    }
  }


}
