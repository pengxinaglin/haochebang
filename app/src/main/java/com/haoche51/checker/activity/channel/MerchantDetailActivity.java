package com.haoche51.checker.activity.channel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.MerchantDetailEntity;
import com.haoche51.checker.fragment.channel.BusinessListFragment;
import com.haoche51.checker.listener.HCTasksWatched;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.HCActionUtil;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 商家详情界面
 * Created by wufx on 2016/3/1.
 */
public class MerchantDetailActivity extends CommonTitleBaseActivity {
	/**
	 * 经销商名字
	 */
	@ViewInject(R.id.tv_vehicle_name)
	private TextView tv_merchant_name;

	/**
	 * 经理姓名
	 */
	@ViewInject(R.id.tv_manager_name)
	private TextView tv_manager_name;

	/**
	 * 经理电话
	 */
	@ViewInject(R.id.tv_manager_phone)
	private TextView tv_manager_phone;

	/**
	 * 商家地址
	 */
	@ViewInject(R.id.tv_merchant_address)
	private TextView tv_merchant_address;

	/**
	 * 上次维护
	 */
	@ViewInject(R.id.tv_last_maintain)
	private TextView tv_last_maintain;

	/**
	 * 在售车源
	 */
	@ViewInject(R.id.tv_online_vehicle)
	private TextView tv_online_vehicle;

	/**
	 * 维护人员
	 */
	@ViewInject(R.id.tv_maintainer)
	private TextView tv_maintainer;

	/**
	 * 维护备注
	 */
	@ViewInject(R.id.tv_maintain_comment)
	private TextView tv_maintain_comment;

	/**
	 * 我已维护
	 */
	@ViewInject(R.id.btn_negative)
	private Button btn_maintain;

	/**
	 * 添加车源
	 */
	@ViewInject(R.id.btn_positive)
	private Button btn_add_vehicle;

	/**
	 * 商家id
	 */
	private int merchantId;
	private Call mCall;
	private MerchantDetailEntity merchantDetailEntity;

	private final int REQUEST_MODIFY_DEALER = 190;

	@Override
	public View getHCContentView() {
		return View.inflate(this, R.layout.activity_merchant_detail, null);
	}

  @Override
  public void initContentView(Bundle saveInstanceState) {
	  x.view().inject(this);
    //获取商家id
    merchantId = getIntent().getIntExtra("merchantId", 0);
    initData();
  }

	@Override
	public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
		mTitle.setText(getString(R.string.hc_merchant_detail));
		btn_maintain.setText(getString(R.string.hc_maintained));
		btn_maintain.setVisibility(View.VISIBLE);
		btn_add_vehicle.setText(getString(R.string.hc_add_vehicle));
		btn_add_vehicle.setVisibility(View.VISIBLE);
	}


	/**
	 * 修改信息
	 */
	@Event(R.id.rb_modify_info)
	private void modifyInfo(View view) {
		if(merchantDetailEntity==null){
			return;
		}
		Intent intent = new Intent(this, AddDealerActivity.class);
		intent.putExtra(AddDealerActivity.INTENT_ADD_DEALER, false);
		intent.putExtra(AddDealerActivity.INTENT_MODIFY_DEALER, merchantDetailEntity);
		startActivityForResult(intent, REQUEST_MODIFY_DEALER);
	}

  /**
   * 变更维护
   */
  @Event(R.id.rb_modify_maintain)
  private void modifyMainTain(View view){
    if(GlobalData.userDataHelper.getChannelRight()<=1){
      ToastUtil.showInfo("很抱歉，您没有变更维护的权限");
      return;
    }

    if(merchantDetailEntity==null){
      return;
    }

    Intent intent = new Intent(MerchantDetailActivity.this, UpdateMaintainerActivity.class);
    intent.putExtra("merchantDetail", merchantDetailEntity);
    startActivityForResult(intent, TaskConstants.REQUEST_UPDATE_MERCHANT);
  }

	/**
	 * 查看车源
	 */
	@Event(R.id.rb_check_vehicle)
	private void checkVehicle(View view) {
		if(merchantDetailEntity==null){
			return;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("id", merchantDetailEntity.getId());
		map.put("name", merchantDetailEntity.getName());
		map.put("sell_car", merchantDetailEntity.getSell_car());
		HCActionUtil.launchActivity(GlobalData.context, MerchantVehicleSourceActivity.class, map);
		finish();
	}

	/**
	 * 我已维护
	 */
	@Event(R.id.btn_negative)
	private void maintain(View view) {
		OKHttpManager.getInstance().post(HCHttpRequestParam.maintainMerchant(merchantId), this, 0);
	}

	/**
	 * 添加车源
	 */
	@Event(R.id.btn_positive)
	private void addVehicle(View view) {
		if(merchantDetailEntity==null){
			return;
		}
		Intent intent = new Intent(this, AddVehicleSourceActivity.class);
		intent.putExtra("dealer_id", merchantDetailEntity.getId());
		startActivity(intent);
	}

  private void initData() {
    ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
	  mCall = OKHttpManager.getInstance().post(HCHttpRequestParam.getMerchantDetail(merchantId), this, 0);
  }

	@Override
	public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
		ProgressDialogUtil.closeProgressDialog();
		if (HttpConstants.ACTION_GET_MERCHANT_DETAIL.equals(action)) {
			switch (response.getErrno()) {
				case 0://0：表示接口请求成功
					parseResultData(response.getData());
					break;
				default://1：发生错误
					ToastUtil.showInfo(response.getErrmsg());
					break;
			}
		} else if (HttpConstants.ACTION_MAINTAIN_MERCHANT.equals(action)) {
			switch (response.getErrno()) {
				case 0://0：表示接口请求成功
					ToastUtil.showInfo(getString(R.string.successful));
					//刷新界面
					initData();
					//刷新商家列表维护时间
					Bundle bundle  = new Bundle();
					bundle.putInt("id", merchantDetailEntity.getId());
					try {
						bundle.putString(BusinessListFragment.BUNDLE_DELAER_MAINTAIN, new JSONObject(response.getData()).getString("maintain_time"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					HCTasksWatched.getInstance().notifyWatchers(bundle);
					break;
				default://1：发生错误
					ToastUtil.showInfo(response.getErrmsg());
					break;
			}
		}
	}

	/**
	 * 解析结果数据
	 *
	 * @param result 结果
	 */
	private void parseResultData(String result) {
//		merchantDetailEntity = new HCJsonParse().parseMerchantDetail(result);
		merchantDetailEntity = JsonParseUtil.fromJsonObject(result, MerchantDetailEntity.class);
		if (merchantDetailEntity == null) {
			return;
		}
		tv_merchant_name.setText(merchantDetailEntity.getName());
		tv_manager_name.setText(merchantDetailEntity.getContact_name());
		tv_manager_phone.setText(merchantDetailEntity.getContact_phone());
		tv_merchant_address.setText(merchantDetailEntity.getAddress());
		tv_last_maintain.setText(merchantDetailEntity.getMaintain_time());
		tv_online_vehicle.setText(String.valueOf(merchantDetailEntity.getSell_car()));
		tv_maintainer.setText(merchantDetailEntity.getCrm_user_name());
		tv_maintain_comment.setText(merchantDetailEntity.getRemark());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			//修改商家信息
			if (requestCode == REQUEST_MODIFY_DEALER) {
				Bundle bundle = new Bundle();
				bundle.putInt("id", merchantDetailEntity.getId());
				bundle.putString("name", data.getStringExtra("name"));
				bundle.putString("address", data.getStringExtra("address"));
				bundle.putString("contact_name", data.getStringExtra("contact_name"));
				bundle.putString("contact_phone", data.getStringExtra("contact_phone"));
				bundle.putBoolean(BusinessListFragment.BUNDLE_MODIFY_DELAER_INFO, true);
				HCTasksWatched.getInstance().notifyWatchers(bundle);
			}
			//刷新界面
			initData();
		}
	}

	@Override
	protected void onDestroy() {
		ProgressDialogUtil.closeProgressDialog();
		//如果请求还没完成，界面就被关闭了，那么就取消请求
		OKHttpManager.getInstance().cancelRequest(mCall);
		super.onDestroy();
	}
}
