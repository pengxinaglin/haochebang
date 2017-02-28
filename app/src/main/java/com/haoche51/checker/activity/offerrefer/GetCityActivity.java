package com.haoche51.checker.activity.offerrefer;

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
 * 获取城市
 * Created by wufx on 16/8/8.
 */
public class GetCityActivity extends GetCheckerActivity {

    @Override
    protected Call loadData() {
        mTitle.setText(getString(R.string.city_list));
        search_box.setQueryHint("城市");
        //请求获取城市
        return OKHttpManager.getInstance().post(HCHttpRequestParam.geCityList(), this, 0);
    }

    @Override
    protected void onConfirmClick(LocalCheckerEntity chooseChecker) {
        //判断是否已选择
        if (chooseChecker == null) {
            ToastUtil.showInfo(getString(R.string.choose_city));
            return;
        }
        //将值返回给发起界面
        Intent localIntent = new Intent();
        localIntent.putExtra("city_id", chooseChecker.getId());
        localIntent.putExtra("city_name", chooseChecker.getName());
        setResult(RESULT_OK, localIntent);
        finish();
    }

    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        super.onHttpComplete(action, requestId, response, error);
        if (action.equals(HttpConstants.ACTION_GET_CITY_LIST)) {
            responseGetCheckerList(response);
        }
    }


}
