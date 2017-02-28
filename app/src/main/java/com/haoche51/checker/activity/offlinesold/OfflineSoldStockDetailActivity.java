package com.haoche51.checker.activity.offlinesold;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.HCWebViewActivity;
import com.haoche51.checker.activity.widget.CommonStateActivity;
import com.haoche51.checker.entity.OfflineSoldTaskEntity;
import com.haoche51.checker.fragment.offlinesold.OfflineSoldStockFragment;
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

import java.util.HashMap;
import java.util.Map;

/**
 * 线下售出——库存中详情页面
 */
public class OfflineSoldStockDetailActivity extends CommonStateActivity {

	private final int OFFLINE_SOLD = 25;
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	@ViewInject(R.id.tv_status)
	private TextView tv_status;
	@ViewInject(R.id.tv_receive_user)
	private TextView tv_receive_user;
	@ViewInject(R.id.tv_receive_user_phone)
	private TextView tv_receive_user_phone;
	@ViewInject(R.id.tv_task_num)
	private TextView tv_task_num;
	@ViewInject(R.id.tv_plate_number)
	private TextView tv_plate_number;
	@ViewInject(R.id.tv_price)
	private TextView tv_price;
	@ViewInject(R.id.tv_cheap_price)
	private TextView tv_cheap_price;
	@ViewInject(R.id.tv_plate_time)
	private TextView tv_plate_time;
	@ViewInject(R.id.tv_mile)
	private TextView tv_mile;
	@ViewInject(R.id.tv_transfer_count)
	private TextView tv_transfer_count;
	private int stockId;//根据任务Id去请求任务详情
	private OfflineSoldTaskEntity mStock;//根据任务Id请求的任务详情

	@Override
	protected int getContentView() {
		return R.layout.activity_offlinesold_stock_detail;
	}

	@Override
	protected void initView() {
		super.initView();
		tv_status.setVisibility(View.GONE);
		setScreenTitle(R.string.hc_vehicle_source_detail);
	}

	@Override
	protected void initData() {
		super.initData();
		//获取查询的任务id
		stockId = getIntent().getIntExtra("id", 0);
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (stockId > 0) {
			ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
			OKHttpManager.getInstance().post(HCHttpRequestParam.getBacksellapiDetail(stockId), this, 0);
		}
	}

	@Override
	public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
		ProgressDialogUtil.closeProgressDialog();

		if (action.equals(HttpConstants.ACTION_BACKSELLAPI_GET_DETAIL)) {
			responseTask(response);
		} else if (action.equals(HttpConstants.ACTION_GET_DETAILSURL)) {
			responseGetDetailsUrl(response);
		}
	}

	/**
	 * 处理请求任务详情
	 */
	private void responseTask(HCHttpResponse response) {
		switch (response.getErrno()) {
			case 0:
//				mStock = new HCJsonParse().parseOfflineSoldTaskEntity(response.getData());
				mStock = JsonParseUtil.fromJsonObject(response.getData(), OfflineSoldTaskEntity.class);
				if (mStock != null) {
					setData(mStock);
				}
				break;
			default:
				ToastUtil.showInfo(response.getErrmsg());
				break;
		}
	}

	/**
	 * 处理查询报告
	 */
	private void responseGetDetailsUrl(HCHttpResponse response) {
		switch (response.getErrno()) {
			case -1:
				ToastUtil.showInfo("用户名密码不正确，请尝试退出重新登录");
				break;
			case 0:
				try {
					JSONObject object = new JSONObject(response.getData());
					String url = object.getString("url");
					if (TextUtils.isEmpty(url)) {
						ToastUtil.showInfo("查询结果为空");
						return;
					}
					Map params = new HashMap();
					params.put(HCWebViewActivity.KEY_INTENT_EXTRA_URL, url);
					HCActionUtil.launchActivity(this, HCWebViewActivity.class, params);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			default:
				ToastUtil.showInfo(response.getErrmsg());
				break;
		}
	}

	/**
	 * 设置UI界面数据
	 */
	public void setData(OfflineSoldTaskEntity data) {

		tv_title.setText(data.getTitle());
		tv_receive_user.setText(data.getReceive_user_name() + "（" + data.getReceive_user_role() + "）");
		tv_receive_user_phone.setText(data.getReceive_user_phone());
		tv_task_num.setText(data.getTask_num());
		tv_plate_number.setText(data.getPlate_number());
		tv_price.setText(data.getPrice() + "元");
		tv_cheap_price.setText(data.getCheap_price() + "元");
		tv_plate_time.setText(data.getPlate_time());
		tv_mile.setText(data.getMile() + "万公里");
		tv_transfer_count.setText(data.getTransfer_count() + "");

	}


	/**
	 * 查看报告
	 */
	@Event(R.id.rb_view_report)
	private void viewReport(View v) {
		if (mStock == null) return;
		if (mStock.getVehicle_source_id() == 0) {
			ToastUtil.showInfo("暂无报告");
			return;
		}

		//查询报告
		ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
		OKHttpManager.getInstance().post(HCHttpRequestParam.getDetailsurl(mStock.getVehicle_source_id()), this, 0);
	}

	/**
	 * 关注度
	 */
	@Event(R.id.rb_attention)
	private void attention(View v) {
		if (mStock == null) return;
		Intent intent = new Intent(this, StockAttentionActivity.class);
		intent.putExtra("stock_id", mStock.getStock_id());
		startActivity(intent);
	}

	/**
	 * 提交线索售出
	 */
	@Event(R.id.btn_offline_sold)
	private void offlineSold(View v) {
		if (mStock == null) return;
		Intent intent = new Intent(this, OfflineSoldCommitActivity.class);
		intent.putExtra("stockId", mStock.getStock_id());
		intent.putExtra("trans_type", mStock.getTrans_type());
		intent.putExtra("task_num", mStock.getTask_num());
		startActivityForResult(intent, OFFLINE_SOLD);
	}


	/**
	 * 提交C2C售出
	 */
	@Event(R.id.btn_c2c_sold)
	private void c2cSold(View v) {
		if (mStock == null) return;
		Intent intent = new Intent(this, C2CSoldActivity.class);
		intent.putExtra("stockId", mStock.getStock_id());
		intent.putExtra("vehicleTitle", mStock.getTitle());
		intent.putExtra("status", mStock.getStatus_text());
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case OFFLINE_SOLD:
					//更新库存中列表，移除本条任务
					Bundle bundle = new Bundle();
					bundle.putBoolean(OfflineSoldStockFragment.BINDLE_OFFLINESOLD_TASK, true);
					HCTasksWatched.getInstance().notifyWatchers(bundle);
					finish();
					Map<String, Object> map = new HashMap<>();
					map.put("id", mStock.getStock_id());
					HCActionUtil.launchActivity(GlobalData.context, OfflineSoldSaleDetailActivity.class, map);
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
