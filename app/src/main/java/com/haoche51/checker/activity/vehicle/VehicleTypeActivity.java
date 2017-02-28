package com.haoche51.checker.activity.vehicle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.adapter.VehicleTypeListAdapter;
import com.haoche51.checker.entity.VehicleSourceEntity;
import com.haoche51.checker.entity.VehicleTypeEntity;
import com.haoche51.checker.helper.BrandLogoHelper;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;

import org.xutils.x;

import java.util.List;

import okhttp3.Call;

/**
 * 车型界面
 * Created by wufx on 2016/1/21.
 */
public class VehicleTypeActivity extends CommonTitleBaseActivity implements AdapterView.OnItemClickListener {
	/**
	 * 年款集合适配器
	 */
	private VehicleTypeListAdapter typeListAdapter;
	/**
	 * 车型ListView
	 */
	private ListView mListView;
	/**
	 * 车型List
	 */
	private List<VehicleTypeEntity> typeList = null;

	/**
	 * 品牌名称TextView
	 */
	private TextView mBrandNameTv;

	/**
	 * 品牌Logo ImgeView
	 */
	private ImageView mBrandIv;

	/**
	 * 车系名称TextView
	 */
	private TextView mClassNameTv;

	/**
	 * 年款名称TextView
	 */
	private TextView mYearNameTv;

	private VehicleSourceEntity vehicleSource;

	private Call mCall;

	@Override
	public View getHCContentView() {
		View view = View.inflate(this, R.layout.activity_vehicle_type, null);
		mListView = (ListView) view.findViewById(R.id.lv_vehicle_year);
		mListView.setOnItemClickListener(this);
		mListView.setVerticalScrollBarEnabled(true);
		mBrandNameTv = (TextView) view.findViewById(R.id.tv_brand_name);
		mBrandIv = (ImageView) view.findViewById(R.id.iv_barnd_logo);
		mClassNameTv = (TextView) view.findViewById(R.id.tv_class_name);
		mYearNameTv = (TextView) view.findViewById(R.id.tv_year_name);
		return view;
	}

	@Override
	public void initContentView(Bundle saveInstanceState) {
		x.view().inject(this);
	}

	@Override
	public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
		mTitle.setText(getString(R.string.hc_choose_type));
	}

	@Override
	protected void onStart() {
		super.onStart();
		loadData();
	}

	/**
	 * 加载数据
	 */
	public void loadData() {
		if (getIntent() == null) {
			return;
		}
		vehicleSource = getIntent().getParcelableExtra("vehicleSource");
		if (vehicleSource == null) {
			return;
		}
		mBrandNameTv.setText(vehicleSource.getBrand_name());
		boolean isContains = BrandLogoHelper.BRAND_LOGO.containsKey(vehicleSource.getBrand_id());
		int resId = isContains ? BrandLogoHelper.BRAND_LOGO.get(vehicleSource.getBrand_id()) : R.drawable.b100;
		mBrandIv.setBackgroundResource(resId);
		mClassNameTv.setText(vehicleSource.getSeries_name());
		mYearNameTv.setText(vehicleSource.getYear());
		//请求网络设置备注
		ProgressDialogUtil.showProgressDialog(VehicleTypeActivity.this, getString(R.string.later));
		mCall = OKHttpManager.getInstance().post(HCHttpRequestParam.getTypeByYear(vehicleSource.getSeries_id(), vehicleSource.getYear()), this, 0);
	}

	@Override
	public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
		ProgressDialogUtil.closeProgressDialog();
		if (HttpConstants.ACTION_GET_TYPE_BY_YEAR.equals(action)) {
			switch (response.getErrno()) {
				case 0://0：表示接口请求成功
					doRequestSuccess(response.getData());
					break;
				default://1：发生错误
					ToastUtil.showInfo(response.getErrmsg());
					break;
			}
		}
	}

	/**
	 * 请求成功的处理
	 *
	 * @param jsonStr json串
	 */
	private void doRequestSuccess(String jsonStr) {
//		typeList = new HCJsonParse().parseVehicleTypeList(jsonStr);
		typeList = JsonParseUtil.fromJsonArray(jsonStr,VehicleTypeEntity.class);
		if (typeList == null) {
			return;
		}

		if (typeListAdapter == null) {
			typeListAdapter = new VehicleTypeListAdapter(this, typeList, R.layout.vehicle_subcribe_series_list_item_layout);
			mListView.setAdapter(typeListAdapter);
		} else {
			typeListAdapter.setmList(typeList);
			typeListAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onDestroy() {
		ProgressDialogUtil.closeProgressDialog();
		//如果请求还没完成，界面就被关闭了，那么就取消请求
		OKHttpManager.getInstance().cancelRequest(mCall);
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (typeList == null || typeList.size() == 0 || vehicleSource == null) {
			return;
		}
		VehicleTypeEntity vehicleType = typeList.get(position);
		vehicleSource.setModel_id(vehicleType.getId());
		vehicleSource.setModel_name(vehicleType.getName());
		vehicleSource.setFull_name(vehicleType.getFullname());

		Intent intent = new Intent();
		intent.putExtra("vehicleSource", vehicleSource);
		try{
			intent.setClassName(getPackageName(),vehicleSource.getJump_source());
			startActivity(intent);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			finish();
		}
	}

}
