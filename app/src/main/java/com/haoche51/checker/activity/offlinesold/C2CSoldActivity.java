package com.haoche51.checker.activity.offlinesold;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.purchase.GetSalerActivity;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.OfflineSoldEntity;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;
import com.mobsandgeeks.saripaar.annotation.Required;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * C2C售出
 * Created by wufx on 2016/1/12.
 */
public class C2CSoldActivity extends CommonTitleBaseActivity {
	/**
	 * 买家姓名
	 */
	@ViewInject(R.id.ed_buyer_name)
	@Required(order = 1, message = "买家姓名不能为空")
	private EditText ed_buyer_name;

	/**
	 * 买家电话
	 */
	@ViewInject(R.id.ed_buyer_phone)
	@Required(order = 2, message = "买家电话不能为空")
	private EditText ed_buyer_phone;

	/**
	 * 负责地销
	 */
	@ViewInject(R.id.tv_seller)
	@Required(order = 3, message = "负责地销不能为空")
	private TextView tv_seller;

	/**
	 * 车源标题
	 */
	@ViewInject(R.id.tv_title)
	private TextView tv_title;

	/**
	 * 评估师id
	 */
	private int choose_saler_id;

	/**
	 * 库存id
	 */
	private int stockId;

	@Override
	public View getHCContentView() {
		return View.inflate(this, R.layout.activity_commit_c2c_sold, null);
	}

	@Override
	public void initContentView(Bundle saveInstanceState) {
		x.view().inject(this);

	}


	@Override
	public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
		stockId = getIntent().getIntExtra("stockId", 0);
		mTitle.setText(getString(R.string.hc_c2c_sold));
		String vehicleTitle = getIntent().getStringExtra("vehicleTitle");
		tv_title.setText(vehicleTitle);
	}

	@Event(R.id.tv_seller)
	private void getSeller(View v) {
		Intent intent = new Intent(C2CSoldActivity.this, GetSalerActivity.class);
		startActivityForResult(intent, TaskConstants.REQUEST_GET_SELLER_PERSON);
	}

	/**
	 * 提交C2C售出
	 */
	@Event(R.id.btn_commit)
	private void commit(View v) {
		validator.validate();
	}

	@Override
	public void onValidationSucceeded() {
		super.onValidationSucceeded();
		String buyer_phone = ed_buyer_phone.getText().toString().trim();
//		if (!PhoneNumberUtil.isPhoneNumberValid(buyer_phone)) {
//			onValidateFailed(ed_buyer_phone, "买家电话不正确");
//			return;
//		}

		if (buyer_phone.length() != 11) {
			onValidateFailed(ed_buyer_phone, "买家电话必须为11位");
			return;
		}

		OfflineSoldEntity offlineSoldEntity = new OfflineSoldEntity();
		offlineSoldEntity.setStock_id(stockId);
		offlineSoldEntity.setBuyer_name(ed_buyer_name.getText().toString().trim());
		offlineSoldEntity.setBuyer_phone(ed_buyer_phone.getText().toString().trim());
		offlineSoldEntity.setSaler_name(tv_seller.getText().toString().trim());
		offlineSoldEntity.setSaler_id(choose_saler_id);
		ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
		OKHttpManager.getInstance().post(HCHttpRequestParam.c2cSold(offlineSoldEntity), this, 0);
	}

	/**
	 * 显示校验失败消息
	 *
	 * @param failedView
	 * @param message
	 */
	private void onValidateFailed(EditText failedView, String message) {
		failedView.requestFocus();
		failedView.setError(message);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null || resultCode != RESULT_OK || requestCode != TaskConstants.REQUEST_GET_SELLER_PERSON) {
			return;
		}

		choose_saler_id = data.getIntExtra("choose_saler_id", 0);
		tv_seller.setText(data.getStringExtra("choose_saler_name"));
	}

	@Override
	public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
		ProgressDialogUtil.closeProgressDialog();

		if (HttpConstants.ACTION_C2C_SOLD.equals(action)) {
			switch (response.getErrno()) {
				case 0://0：表示接口请求成功
					ToastUtil.showInfo("提交C2C售出成功！");
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