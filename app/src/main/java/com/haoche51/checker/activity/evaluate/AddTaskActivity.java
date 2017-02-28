package com.haoche51.checker.activity.evaluate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.vehicle.VehicleBrandActivity;
import com.haoche51.checker.activity.widget.CommonStateActivity;
import com.haoche51.checker.entity.LocalCheckerEntity;
import com.haoche51.checker.entity.UserRightEntity;
import com.haoche51.checker.entity.VehicleSourceEntity;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.DisplayUtils;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;
import com.haoche51.checker.util.UnixTimeUtil;
import com.mobsandgeeks.saripaar.annotation.Required;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;


/**
 * 添加收车线索
 * Created by pxl on 2016/5/10.
 */
public class AddTaskActivity extends CommonStateActivity implements
		OnGetPoiSearchResultListener, OnGetSuggestionResultListener {

	/**
	 * 车主姓名
	 */
	@ViewInject(R.id.ed_seller_name)
	@Required(order = 1, message = "车主姓名不能为空")
	private EditText ed_seller_name;
	/**
	 * 车主电话
	 */
	@ViewInject(R.id.ed_seller_phone)
	@Required(order = 2, message = "车主电话不能为空")
	private EditText ed_seller_phone;
	/**
	 * 车辆款型
	 */
	@ViewInject(R.id.tv_vehicle_type)
	@Required(order = 3, message = "车辆款型不能为空")
	private TextView tv_vehicle_type;
	/**
	 * 验车城市
	 */
	@ViewInject(R.id.tv_check_city)
	private TextView tv_check_city;
	/**
	 * 验车地点
	 */
	@ViewInject(R.id.ed_check_address)
	@Required(order = 4, message = "验车地点不能为空")
	private AutoCompleteTextView ed_check_address;
	/**
	 * 验车日期
	 */
	@ViewInject(R.id.tv_check_vehicle_date)
	@Required(order = 5, message = "验车日期不能为空")
	private TextView tv_check_vehicle_date;
	/**
	 * 验车备注
	 */
	@ViewInject(R.id.ed_check_note)
	private EditText ed_check_note;
	/**
	 * 评估师
	 */
	@ViewInject(R.id.tv_checker)
	@Required(order = 6, message = "请选择任务评估师")
	private TextView tv_checker;

	@ViewInject(R.id.btn_positive)
	private Button btn_positive;

	private final int REQUEST_CODE_CHOOSE_CHCKER = 123;

	private VehicleSourceEntity vehicleSource;//车辆款型
	private LocalCheckerEntity checkerEntity;//所选评估师
	private PoiSearch mPoiSearch = null;
	private SuggestionSearch mSuggestionSearch = null;
	private ResultAdapter sugAdapter = null;//位置搜索结果
	private int loadIndex = 0;
	private PoiInfo mPoiInfo;//地理位置信息
	private String district = "";//区县
	private List<SuggestionResult.SuggestionInfo> allSuggestions;//所有查询位置集合

	private boolean hasRight = false;

	@Override
	protected int getContentView() {
		return R.layout.activity_check_add_task;
	}

	@Override
	protected void initView() {
		super.initView();
		setScreenTitle(getString(R.string.hc_add_task));
		btn_positive.setText(getString(R.string.hc_add_task_submit));
		tv_check_city.setText(GlobalData.userDataHelper.getChecker().getCity_name());

		new Runnable() {
			@Override
			public void run() {
				//获取用户权限
				List<UserRightEntity> userRights = GlobalData.userDataHelper.getUserRight();
				for (UserRightEntity userRight : userRights) {
					//收车权限
					if (userRight.getCode() == 101) {
						//高于普通权限
						if (userRight.getRight() > 1) {
							hasRight = true;
						}
						break;
					}
				}
			}
		}.run();

		// 初始化搜索模块，注册搜索事件监听
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(this);
		sugAdapter = new ResultAdapter(this, R.layout.item_location_popup, allSuggestions);
		ed_check_address.setAdapter(sugAdapter);
		ed_check_address.setThreshold(1);
		/**
		 * 当输入关键字变化时，动态更新建议列表
		 */
		ed_check_address.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
				if (cs.length() <= 0) {
					return;
				}

				/**
				 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
				 */
				mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(cs.toString()).city(tv_check_city.getText().toString()));
			}
		});
		//设置点击item事件
		ed_check_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				try {
					ed_check_address.setText(allSuggestions.get(i).key);
					ed_check_address.setSelection(allSuggestions.get(i).key.length());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 选择车辆款型
	 */
	@Event(R.id.tv_vehicle_type)
	private void tv_vehicle_type(View v) {
		VehicleSourceEntity vehicleSource = new VehicleSourceEntity();
		vehicleSource.setJump_source(AddTaskActivity.class.getName());
		Intent intent = new Intent();
		intent.putExtra("vehicleSource", vehicleSource);
		intent.setClass(this, VehicleBrandActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		if (getIntent() == null) {
			return;
		}

		vehicleSource = getIntent().getParcelableExtra("vehicleSource");
		if (vehicleSource == null) {
			return;
		}

		String fullName = vehicleSource.getFull_name().trim();
		if (!TextUtils.isEmpty(vehicleSource.getBrand_name()) && !TextUtils.isEmpty(fullName)) {
			tv_vehicle_type.setText(fullName);
		}
	}

	/**
	 * 选择验车日期
	 */
	@Event(R.id.tv_check_vehicle_date)
	private void tv_check_vehicle_date(View v) {
		//初始化时间滚轮
		DisplayUtils.displayTimeWhellYMD(this, tv_check_vehicle_date, R.string.select_time);
	}

	/**
	 * 选择评估师
	 */
	@Event(R.id.tv_checker)
	private void tv_checker(View v) {
		Intent intent = new Intent();
		intent.putExtra(TransmitCheckTaskActivity.INTENT_KEY_ONLY_CHOOSE_CHECKER, true);
		intent.setClass(this, TransmitCheckTaskActivity.class);
		startActivityForResult(intent, REQUEST_CODE_CHOOSE_CHCKER);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null || resultCode != Activity.RESULT_OK) {
			return;
		}

		if (requestCode == REQUEST_CODE_CHOOSE_CHCKER) {
			checkerEntity = (LocalCheckerEntity) data.getSerializableExtra("chooseChecker");
			if (checkerEntity != null)
				tv_checker.setText(checkerEntity.getName());
		}
	}

	@Event(R.id.btn_positive)
	private void btn_positive(View v) {
		if (!hasRight) {
			ToastUtil.showInfo("没有权限！");
			return;
		}
		validator.validate();
	}

	/**
	 * 校验成功
	 */
	@Override
	public void onValidationSucceeded() {
		super.onValidationSucceeded();

		String name = ed_seller_name.getText().toString().trim();
		if (!name.endsWith("先生") && !name.endsWith("女士")) {
			onValidateFailed(ed_seller_name, "格式需为XX先生或女士");
			return;
		}
		if (name.length() > 4) {
			onValidateFailed(ed_seller_name, "姓名长度过长");
			return;
		}
		String sellerPhone = ed_seller_phone.getText().toString().trim();
		if (sellerPhone.length() != 11) {
			onValidateFailed(ed_seller_phone, "请输入正确的手机号");
			return;
		}

		mPoiSearch.searchInCity((new PoiCitySearchOption())
				.city(tv_check_city.getText().toString())
				.keyword(ed_check_address.getText().toString())
				.pageNum(loadIndex));
	}

	/**
	 * 显示校验失败消息
	 *
	 * @param failedView
	 * @param message
	 */
	private void onValidateFailed(TextView failedView, String message) {
		failedView.requestFocus();
		failedView.setError(message);
	}

	@Override
	public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
		if (!isFinishing())
			ProgressDialogUtil.closeProgressDialog();

		if (HttpConstants.ACTION_ADD_CHECK_TASK_FROM_APP.equals(action)) {
			responseAddTask(response);
		}
	}

	/**
	 * 处理请求添加任务
	 */
	private void responseAddTask(HCHttpResponse response) {
		switch (response.getErrno()) {
			case 0://0：表示接口请求成功
				ToastUtil.showInfo("添加成功");
				finish();
				break;
			default://1：发生错误
				ToastUtil.showInfo(response.getErrmsg());
				break;
		}
	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			Toast.makeText(this, "未找到结果", Toast.LENGTH_LONG).show();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			List<PoiInfo> allPoi = result.getAllPoi();
			if (allPoi != null && !allPoi.isEmpty()) {
				try {
					ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
					mPoiInfo = allPoi.get(0);
					district = "";
					if (allSuggestions != null) {
						for (SuggestionResult.SuggestionInfo info : allSuggestions) {
							if (info.pt == null)
								continue;

							if (mPoiInfo.location.latitude == info.pt.latitude && mPoiInfo.location.longitude == info.pt.longitude) {
								if (info.district != null)
									district = info.district;
								break;
							}
						}
					}
					//TODO
					String name = ed_seller_name.getText().toString();
					String phone = ed_seller_phone.getText().toString();
					int city_id = GlobalData.userDataHelper.getChecker().getCity();
					String place = tv_check_city.getText().toString() + district + ed_check_address.getText().toString();
					int time = UnixTimeUtil.getUnixTime(tv_check_vehicle_date.getText().toString(), UnixTimeUtil.YEAR_MONTH_DAY_PATTERN);
					String comment = ed_check_note.getText().toString();
					OKHttpManager.getInstance().post(HCHttpRequestParam.addCheckTask(name, phone, vehicleSource, city_id, place,
							mPoiInfo.location.longitude, mPoiInfo.location.latitude, time, comment, checkerEntity), this, 0);
				} catch (Exception e) {
					e.printStackTrace();
					ProgressDialogUtil.closeProgressDialog();
				}
			}
			return;
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
			// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
			String strInfo = "在";
			for (CityInfo cityInfo : result.getSuggestCityList()) {
				strInfo += cityInfo.city;
				strInfo += ",";
			}
			strInfo += "找到结果，请手动切换城市";
			Toast.makeText(this, strInfo, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {
		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT).show();
		}
	}


	@Override
	public void onGetSuggestionResult(SuggestionResult res) {
		if (res == null || res.getAllSuggestions() == null) {
			return;
		}
		List<SuggestionResult.SuggestionInfo> delete = new ArrayList<>();
		allSuggestions = res.getAllSuggestions();
		for (SuggestionResult.SuggestionInfo info : allSuggestions) {
			if (info.key == null) {
				delete.add(info);
//				suggest.add(info.key);
			}
		}
		allSuggestions.removeAll(delete);
		sugAdapter = new ResultAdapter(this, R.layout.item_location_popup, allSuggestions);
		ed_check_address.setAdapter(sugAdapter);
		sugAdapter.notifyDataSetChanged();
	}

	private class ResultAdapter extends ArrayAdapter<SuggestionResult.SuggestionInfo> {

		public ResultAdapter(Context context, int resource, List<SuggestionResult.SuggestionInfo> objects) {
			super(context, resource, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(AddTaskActivity.this).inflate(R.layout.item_location_popup, parent, false);
				holder = new ViewHolder();
				holder.tv_key = (TextView) convertView.findViewById(R.id.tv_key);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			SuggestionResult.SuggestionInfo item = getItem(position);
			holder.tv_key.setText(item.key);
			return convertView;
		}

		private class ViewHolder {
			private TextView tv_key;
		}
	}
}