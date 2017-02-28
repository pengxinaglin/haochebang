package com.haoche51.checker.activity.channel;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonStateActivity;
import com.haoche51.checker.entity.MerchantDetailEntity;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 添加/修改商家
 * Created by PengXianglin on 16/3/1.
 */
public class AddDealerActivity extends CommonStateActivity {

	@ViewInject(R.id.et_name)
	private EditText et_name;

	@ViewInject(R.id.et_address)
	private EditText et_address;

	@ViewInject(R.id.et_contact_name)
	private EditText et_contact_name;

	@ViewInject(R.id.et_contact_phone)
	private EditText et_contact_phone;

	@ViewInject(R.id.tv_city)
	private TextView tv_city;

	@ViewInject(R.id.btn_positive)
	private Button btn_positive;

	private MerchantDetailEntity merchantDetailEntity;

	public static final String INTENT_ADD_DEALER = "intentkey_add_dealer", INTENT_MODIFY_DEALER = "intentkey_modify_dealer";

	@Override
	protected int getContentView() {
		return R.layout.activity_add_dealer;
	}

	@Override
	protected void initView() {
		super.initView();

		if (getIntent().getBooleanExtra(INTENT_ADD_DEALER, false)) {
			setScreenTitle(R.string.hc_channel_add_business);
			btn_positive.setText(R.string.hc_channel_add_business);
		} else {
			merchantDetailEntity = getIntent().getParcelableExtra(INTENT_MODIFY_DEALER);
			if (merchantDetailEntity != null) {
				et_name.setText(merchantDetailEntity.getName());
				et_name.setSelection(merchantDetailEntity.getName().length());
				et_address.setText(merchantDetailEntity.getAddress());
				et_contact_name.setText(merchantDetailEntity.getContact_name());
				et_contact_phone.setText(merchantDetailEntity.getContact_phone());
			}
			setScreenTitle(R.string.hc_channel_modify_businessinfo);
			btn_positive.setText("保存修改");
		}
		String city = GlobalData.userDataHelper.getChecker().getCity_name();
		tv_city.setText((TextUtils.isEmpty(city) || "0".equals(city)) ? "未知" : city);
	}

	private String name, address, contact_name, contact_phone;

	/**
	 * 保存
	 */
	@Event(R.id.btn_positive)
	private void saveEvent(View v) {
		int id = merchantDetailEntity == null ? 0 : merchantDetailEntity.getId();
		name = et_name.getText().toString();
		address = et_address.getText().toString();
		contact_name = et_contact_name.getText().toString();


		if (TextUtils.isEmpty(name)) {
			ToastUtil.showInfo("商家名称不能为空");
			return;
		}
		if (TextUtils.isEmpty(address)) {
			ToastUtil.showInfo("商家地址不能为空");
			return;
		}
		if (TextUtils.isEmpty(contact_name)) {
			ToastUtil.showInfo("联系人不能为空");
			return;
		}
		if (TextUtils.isEmpty(et_contact_phone.getText())) {
			ToastUtil.showInfo("联系电话不能为空");
			return;
		}
		contact_phone = et_contact_phone.getText().toString().trim();
//		if (!PhoneNumberUtil.isPhoneNumberValid(contact_phone)){
//			ToastUtil.showInfo("请输入正确格式的联系电话");
//			return;
//		}

		if (contact_phone.length()!=11){
			ToastUtil.showInfo("联系电话必须为11位");
			return;
		}

		ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
		OKHttpManager.getInstance().post(HCHttpRequestParam.addDealer(id, name, address, contact_name, contact_phone), this, 0);
	}

	@Override
	public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
		if (!isFinishing())
			ProgressDialogUtil.closeProgressDialog();

		if (action.equals(HttpConstants.ACTION_DEALERAPI_ADD)) {
			responseAddDealer(response);
		}
	}

	/**
	 * 处理发起添加/修改商家信息请求
	 */
	private void responseAddDealer(HCHttpResponse response) {
		switch (response.getErrno()) {
			case 0:
				ToastUtil.showInfo("请求成功！");
				Intent intent = new Intent();
				intent.putExtra("name", name);
				intent.putExtra("address", address);
				intent.putExtra("contact_name", contact_name);
				intent.putExtra("contact_phone", contact_phone);
				setResult(RESULT_OK, intent);
				finish();
				break;
			default:
				ToastUtil.showInfo(response.getErrmsg());
				break;
		}
	}
}
