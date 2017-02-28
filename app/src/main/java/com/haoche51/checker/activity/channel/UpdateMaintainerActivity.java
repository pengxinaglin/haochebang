package com.haoche51.checker.activity.channel;

import android.content.Intent;
import android.os.Bundle;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.evaluate.GetCheckerActivity;
import com.haoche51.checker.entity.LocalCheckerEntity;
import com.haoche51.checker.entity.MerchantDetailEntity;
import com.haoche51.checker.fragment.channel.BusinessListFragment;
import com.haoche51.checker.listener.HCTasksWatched;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.AlertDialogUtil;
import com.haoche51.checker.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * 变更维护人
 * Created by wufx on 2016/3/2.
 */
public class UpdateMaintainerActivity extends GetCheckerActivity {
	private MerchantDetailEntity merchantDetail;
	public static final String INTENT_KEY_ONLY_CHOOSE_MAINTAINER = "only_choose_maintainer",//只选择人员 不执行更变任务
		KEY_INTENT_EXTRA_MAINTAINER = "extra_maintainer";//传递变更人员

	@Override
	protected Call loadData() {
		mTitle.setText(getString(R.string.channeler_list));
		return OKHttpManager.getInstance().post(HCHttpRequestParam.getSameCityChannelUser(), this, 0);
	}

	@Override
	protected void onConfirmClick(final LocalCheckerEntity chooseChecker) {
		//判断是否已选择
		if (chooseChecker == null) {
			ToastUtil.showInfo(getString(R.string.choose_maintainer));

			setResult(RESULT_OK);
			return;
		}

		//只选择人员
		if (getIntent().getBooleanExtra(INTENT_KEY_ONLY_CHOOSE_MAINTAINER, false)) {
			Intent intent = new Intent();
			intent.putExtra(KEY_INTENT_EXTRA_MAINTAINER, chooseChecker);
			setResult(RESULT_OK, intent);
			finish();
			return;
		}

		merchantDetail = getIntent().getParcelableExtra("merchantDetail");
		//二次确认
		AlertDialogUtil.showStandardTitleMessageDialog(UpdateMaintainerActivity.this, getString(R.string.hc_modify_maintain),
			getString(R.string.update_maintainer, chooseChecker.getName()), getString(R.string.transmit_cancel),
			getString(R.string.transmit_confirm), new AlertDialogUtil.OnDismissListener() {
				@Override
				public void onDismiss(Bundle data) {
					//确定转单
					OKHttpManager.getInstance().post(HCHttpRequestParam.updateMaintainer(merchantDetail, chooseChecker), UpdateMaintainerActivity.this, 0);
				}
			});
	}


	@Override
	public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
		super.onHttpComplete(action, requestId, response, error);
		if (action.equals(HttpConstants.ACTION_GET_SAME_CITY_CHANNEL_USER)) {
			responseGetCheckerList(response);
		} else if (HttpConstants.ACTION_UPDATE_MAINTAINER.equals(action)) {
			responseOperate(response);
		}

	}


	/**
	 * 响应操作
	 *
	 * @param response
	 */
	protected void responseOperate(HCHttpResponse response) {
		switch (response.getErrno()) {
			case 0:
				ToastUtil.showInfo(getString(R.string.successful));
				//更新商家列表维护人员
				Bundle bundle = new Bundle();
				bundle.putBoolean(BusinessListFragment.BUNDLE_DELAER_CRM_USER, true);
				bundle.putInt("id", merchantDetail.getId());
				try {
					bundle.putString("crm_user_name", new JSONObject(response.getData()).getString("name"));
					bundle.putString("crm_user_phone", new JSONObject(response.getData()).getString("phone"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				HCTasksWatched.getInstance().notifyWatchers(bundle);
				setResult(RESULT_OK);
				finish();
				break;
			default:
				ToastUtil.showInfo(response.getErrmsg());
				break;
		}
	}

}
