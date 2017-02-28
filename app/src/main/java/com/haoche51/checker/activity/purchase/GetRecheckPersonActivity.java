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
 * 获取评估师
 * Created by wufx on 15/11/24.
 */
public class GetRecheckPersonActivity extends GetCheckerActivity {

  @Override
  protected Call loadData() {
    mTitle.setText(getString(R.string.checker_list));
    //请求获取本地评估师
    return OKHttpManager.getInstance().post(HCHttpRequestParam.getCheckerList(0, false), this, 0);
  }

  @Override
  protected void onConfirmClick(LocalCheckerEntity chooseChecker) {
    //判断是否已选择
    if (chooseChecker == null) {
      ToastUtil.showInfo(getString(R.string.hc_rechecker_person));
      return;
    }
    //将值返回给发起界面
    Intent localIntent = new Intent();
    localIntent.putExtra("choose_checker_id", chooseChecker.getId());
    localIntent.putExtra("choose_checker_name", chooseChecker.getName());
    setResult(RESULT_OK, localIntent);
    finish();
  }

  @Override
  public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
    super.onHttpComplete(action,requestId,response,error);
    if (action.equals(HttpConstants.ACTION_GET_CHECKERLIST)) {
      responseGetCheckerList(response);
    }
  }


}
