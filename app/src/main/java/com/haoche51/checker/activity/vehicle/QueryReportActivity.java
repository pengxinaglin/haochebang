package com.haoche51.checker.activity.vehicle;

import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.HCWebViewActivity;
import com.haoche51.checker.activity.widget.CommonStateActivity;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.ACache;
import com.haoche51.checker.util.HCActionUtil;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * 根据车源编号/vin码查询保养记录
 * Created by PengXianglin on 16/5/12.
 */
public class QueryReportActivity extends CommonStateActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, TextView.OnEditorActionListener {

	@ViewInject(R.id.spn_query_type)
	private Spinner spn_query_type;

	@ViewInject(R.id.et_content)
	private EditText et_content;

	@ViewInject(R.id.btn_action)
	private Button btn_action;

	@ViewInject(R.id.lv_history)
	private ListView lv_history;

	private ACache mACache;
	private ArrayAdapter<String> mAdapter;
	private List<String> historyList = new ArrayList<>();

	@Override
	protected int getContentView() {
		return R.layout.activity_query_report;
	}

	@Override
	protected void initView() {
		super.initView();
		x.view().inject(this);

		setScreenTitle(R.string.hc_query_title);
		spn_query_type.setOnItemSelectedListener(this);
		btn_action.setText(getString(R.string.hc_query_submit));
		et_content.setOnEditorActionListener(this);

		//读取查询历史记录
		mACache = ACache.get(this);
		String json = mACache.getAsString(TaskConstants.ACACHE_MAINTENANCE_RECORDS);
//		List<String> tempList = new HCJsonParse().parseVehicleReportHistory(json);
		List<String> tempList = JsonParseUtil.fromJsonArray(json, String.class);
		if (tempList != null)
			historyList.addAll(tempList);
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, historyList);
		lv_history.setAdapter(mAdapter);
		lv_history.setOnItemClickListener(this);
	}


	/**
	 * 查询
	 */
	@Event(R.id.btn_action)
	private void search(View v) {
		String search = et_content.getText().toString();
		if (TextUtils.isEmpty(search)) {
//			ToastUtil.showInfo(getString(R.string.hc_query_hint));
			ToastUtil.showInfo(et_content.getHint().toString());
			return;
		}

		//添加历史记录
		historyList.add(0, search);
		//保存前30条记录到本地
//		String history = new HCJsonParse().parseVehicleReportHistoryListToJson(historyList.size() > 30 ?
//				historyList.subList(0, 30) : historyList);
		String history = JsonParseUtil.toJsonArray(historyList.size() > 30 ?
				historyList.subList(0, 30) : historyList);
		mACache.put(TaskConstants.ACACHE_MAINTENANCE_RECORDS, history);
		mAdapter.notifyDataSetChanged();

		try {
			//查询报告
			ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
			//查询保养记录
			if (spn_query_type.getSelectedItemPosition() == 0)
				OKHttpManager.getInstance().post(HCHttpRequestParam.searchReport(search), this, 0);
			else
				OKHttpManager.getInstance().post(HCHttpRequestParam.getDetailsurl(Integer.parseInt(search)), this, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
		if (!isFinishing())
			ProgressDialogUtil.closeProgressDialog();

		if (action.equals(HttpConstants.ACTION_SEARCH_MAINTAINRECORD)) {
			responseSearch(response);
		} else if (action.equals(HttpConstants.ACTION_GET_DETAILSURL)) {
			responseGetDetailsUrl(response);
		}
	}

	/**
	 * 处理发起搜索
	 */
	private void responseSearch(HCHttpResponse response) {
		switch (response.getErrno()) {
			case 0:
				if (response.getData() == null) {
					ToastUtil.showInfo("返回参数错误");
					return;
				}

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

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
		try {
			String str = historyList.get(i);
			//查询保养记录
			if (spn_query_type.getSelectedItemPosition() == 0) {
				et_content.setText(str);
				et_content.setSelection(str.length());
			} else {
				Pattern pattern = Pattern.compile("[0-9]*");
				//是数字
				if (pattern.matcher(str).matches()) {
					et_content.setText(str);
					et_content.setSelection(str.length());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
		switch (i) {
			case 0:
				et_content.setHint(getString(R.string.hc_query_hint));
				et_content.setInputType(InputType.TYPE_CLASS_TEXT);
				//vin码最大长度17位数
				et_content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(17)});
				break;
			case 1:
				et_content.setText("");
				et_content.setHint(getString(R.string.hc_query_hint2));
				et_content.setInputType(InputType.TYPE_CLASS_NUMBER);
				//车源编号最大长度7位数
				et_content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
				break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView) {

	}

	@Override
	public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
		if (actionId == EditorInfo.IME_ACTION_SEARCH || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
			//查询
			search(null);
			return true;
		}
		return false;
	}
}
