package com.haoche51.checker.activity.evaluate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.haoche51.checker.CheckerApplication;
import com.haoche51.checker.DAO.CheckReportDAO;
import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.HCWebViewActivity;
import com.haoche51.checker.activity.media.AudioRecordActivity;
import com.haoche51.checker.activity.purchase.CheckerAddClueActivity;
import com.haoche51.checker.activity.widget.CommonStateActivity;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.CheckReportEntity;
import com.haoche51.checker.entity.CheckTaskEntity;
import com.haoche51.checker.entity.RspVinCodeEntity;
import com.haoche51.checker.fragment.evaluate.CheckTaskFragment;
import com.haoche51.checker.listener.HCTasksWatched;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.AlertDialogUtil;
import com.haoche51.checker.util.ControlDisplayUtil;
import com.haoche51.checker.util.HCActionUtil;
import com.haoche51.checker.util.IntentExtraMap;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;
import com.haoche51.checker.util.UnixTimeUtil;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.Map;


public class CheckTaskDetailActivity extends CommonStateActivity {

	private final int TRANSMIT_TASK = 100, CANCLE_SUCCESS = 110, MODIFY_ADDRESS = 120;

	@ViewInject(R.id.tv_title)
	private TextView tv_vehicle_name;

	@ViewInject(R.id.tv_status)
	private TextView tv_status;

	@ViewInject(R.id.tv_check_seller_name)
	private TextView tv_check_seller_name;
	@ViewInject(R.id.tv_check_seller_phone)
	private TextView tv_check_seller_phone;
	@ViewInject(R.id.tv_check_task_id)
	private TextView tv_check_task_id;
	@ViewInject(R.id.tv_check_appoint_time)
	private TextView tv_check_appoint_time;
	@ViewInject(R.id.tv_check_app_add)
	private TextView tv_check_app_add;
	@ViewInject(R.id.tv_check_comment)
	private TextView tv_check_comment;
	@ViewInject(R.id.tv_checker_comment)
	private TextView tv_checker_comment;
	@ViewInject(R.id.btn_negative)
	private Button btn_negative;
	@ViewInject(R.id.btn_cancel)
	private Button btn_cancel;
	@ViewInject(R.id.btn_positive)
	private Button btn_positive;
	private int taskId;//根据任务Id去请求任务详情
	private CheckTaskEntity mTask;
	private CheckReportEntity mReport;
	private String comment;

	@Override
	protected int getContentView() {
		return R.layout.activity_checktask_detail;
	}

	@Override
	protected void initView() {
		super.initView();
		setScreenTitle(R.string.hc_check_task_title);
		btn_negative.setText("");
		btn_positive.setText("");
	}

	@Override
	protected void initData() {
		super.initData();
		//获取查询的任务id
		taskId = getIntent().getIntExtra("id", 0);
		//根据任务id查询本地报告
		mReport = CheckReportDAO.getInstance().getByTaskId(taskId);
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (taskId > 0) {
			ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
			OKHttpManager.getInstance().post(HCHttpRequestParam.getCheckTask(taskId), this, 0);
		}
	}

	/**
	 * 处理请求任务详情
	 */
	private void responseTask(HCHttpResponse response) {
		switch (response.getErrno()) {
			case 0:
//				mTask = new HCJsonParse().parseGetCheckTask(response.getData());
				mTask = JsonParseUtil.fromJsonObject(response.getData(), CheckTaskEntity.class);
				if (mTask != null) {
					setData(mTask);
				}
				break;
			default:
				ToastUtil.showInfo(response.getErrmsg());
				break;
		}
	}

	@Override
	public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
		if (!isFinishing())
			ProgressDialogUtil.closeProgressDialog();

		if (action.equals(HttpConstants.ACTION_GET_CHECKTASK)) {
			responseTask(response);
		} else if (action.equals(HttpConstants.ACTION_ADD_VEHICLECHECK_COMMENT)) {
			responseAddComment(response);
		} else if (action.equals(HttpConstants.ACTION_GET_CJD_REPORT)) {
			responseVin(response);
		} else if (action.equals(HttpConstants.ACTION_START_CHECK_TASK)) {
			responseStartTask(response);
		}
	}


	/**
	 * 设置UI界面数据
	 */
	public void setData(CheckTaskEntity data) {
		tv_vehicle_name.setText(data.getVehicle_name());
		ControlDisplayUtil.getInstance().setCheckStatus(tv_status, data.getCheck_status());
		tv_check_seller_name.setText(data.getSeller_name());
		tv_check_seller_phone.setText(data.getSeller_phone());
		tv_check_task_id.setText(data.getId() + "");
		tv_check_appoint_time.setText(UnixTimeUtil.formatYearMonthDay(mTask.getAppointment_starttime()) + " " + mTask.getStarttime_comment());
		tv_check_app_add.setText(data.getAppointment_place());
		tv_check_comment.setText(data.getComment());
		tv_checker_comment.setText(data.getChecker_comment());
		//设置底部按钮状态
		ControlDisplayUtil.getInstance().setCheckBtnStatus(btn_negative,btn_cancel, btn_positive, mTask.getCheck_source(), mTask.getCheck_status(), mTask.getHelp_check_status());
	}

	/**
	 * 取消验车
	 */
	@Event(R.id.btn_cancel)
	private void btn_cancel(View v){
		//取消验车
		Intent intent = new Intent(CheckTaskDetailActivity.this, CancelTaskActivity.class);
		intent.putExtra("id", mTask.getId());
		startActivityForResult(intent, CANCLE_SUCCESS);
	}

	/**
	 * 修改报告、取消验车
	 */
	@Event(R.id.btn_negative)
	private void btn_negative(View v) {
		if (mTask == null)
			return;
		//待处理或验车中
		if (mTask.getCheck_status() == TaskConstants.CHECK_STATUS_PENDING || mTask.getCheck_status() == TaskConstants.CHECK_STATUS_ONGOING) {
			//取消验车
			Intent intent = new Intent(CheckTaskDetailActivity.this, CancelTaskActivity.class);
			intent.putExtra("id", mTask.getId());
			startActivityForResult(intent, CANCLE_SUCCESS);
		} else {
			//检查报告是否本地存在
			if (mReport == null)
				//不存在，让评估师去重新写报告
				mReport = CheckReportEntity.createReport(mTask);
			//跳转到写报告页面
			if (mReport != null)
				HCActionUtil.launchActivity(CheckTaskDetailActivity.this, FillExamReportActivity.class, IntentExtraMap.putId(mReport.getId()));
		}
	}

	/**
	 * 上传报告、开始验车、继续填写报告
	 */
	@Event(R.id.btn_positive)
	private void btn_positive(View v) {
		if (mTask == null)
			return;
		//帮检 并且是待检
		if ((mTask.getCheck_source() == TaskConstants.CHECK_TASK_ASSISTANCE) &&
				(mTask.getHelp_check_status() == TaskConstants.HELP_CHECK_PENDING) &&
				(mTask.getCheck_status()) != TaskConstants.CHECK_STATUS_CANCEL) {
			//进入帮检
			Intent helpCheckIntent = new Intent(CheckTaskDetailActivity.this, HelpCheckDetailActivity.class);
			helpCheckIntent.putExtra("taskId", mTask.getId());
			startActivityForResult(helpCheckIntent, TaskConstants.HELP_CHECK);
		}
		//待上传
		else if (mTask.getCheck_status() == TaskConstants.CHECK_STATUS_TOUPLOAD) {
			//检查报告是否本地存在 并且 信息完整
			if (mReport != null && mReport.getComplete_check() == TaskConstants.CHECK_TASK_ACHIEVE) {
				//存在
				HCActionUtil.launchActivity(GlobalData.context, ImageUploadActivity.class, IntentExtraMap.putId(mReport.getId()));
			} else {
				//不存在，让评估师去重新写报告
				mReport = CheckReportEntity.createReport(mTask);
				if (mReport != null)
					HCActionUtil.launchActivity(CheckTaskDetailActivity.this, FillExamReportActivity.class, IntentExtraMap.putId(mReport.getId()));
			}
		}
		//继续填写报告
		else if (mTask.getCheck_status() == TaskConstants.CHECK_STATUS_ONGOING) {
			//检查报告是否本地存在
			if (mReport == null)
				//不存在，让评估师去重新写报告
				mReport = CheckReportEntity.createReport(mTask);
			//跳转到写报告页面
			if (mReport != null)
				HCActionUtil.launchActivity(CheckTaskDetailActivity.this, FillExamReportActivity.class, IntentExtraMap.putId(mReport.getId()));
		} else {
			//请求服务器 更改任务状态为检验中
			ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
			OKHttpManager.getInstance().post(HCHttpRequestParam.startCheckTask(mTask.getId()), this, 0);
		}
	}

	/**
	 * 处理请求网络开始验车
	 */
	private void responseStartTask(HCHttpResponse response) {
		switch (response.getErrno()) {
			case 0:
				Bundle data = new Bundle();
				data.putBoolean(CheckTaskFragment.ONCHANGED_STATRCHECK, true);
				//更新任务状态
				HCTasksWatched.getInstance().notifyWatchers(data);
				//跳转到验车
				mReport = CheckReportEntity.createReport(mTask);
				if (mReport != null)
					HCActionUtil.launchActivity(CheckTaskDetailActivity.this, FillExamReportActivity.class, IntentExtraMap.putId(mReport.getId()));
				break;
			default:
				ToastUtil.showInfo(response.getErrmsg());
				break;
		}
	}


	/**
	 * 修改我（评估师）的备注
	 */
	@Event(R.id.rb_check_comment)
	private void rb_check_comment(View v) {
		if (mTask == null)
			return;
		AlertDialogUtil.createCheckerCommentDialog(this, true, tv_checker_comment.getText().toString(), new AlertDialogUtil.OnDismissListener() {
			@Override
			public void onDismiss(Bundle data) {
				if (data != null) {
					comment = data.getString("comment");
					//请求网络设置备注
					OKHttpManager.getInstance().post(HCHttpRequestParam.addVehiclecheckComment(mTask.getId(), comment), CheckTaskDetailActivity.this, 0);
				}
			}
		});
	}

	/**
	 * 处理添加备注
	 */
	private void responseAddComment(HCHttpResponse response) {
		switch (response.getErrno()) {
			case 0:
				mTask.setChecker_comment(comment);
				//更新界面显示
				tv_checker_comment.setText(comment);
				Bundle data = new Bundle();
				data.putBoolean(CheckTaskFragment.ONCHANGED_FINISHTASK, mTask.getCheck_status() == TaskConstants.CHECK_STATUS_SUCCESS
						|| mTask.getCheck_status() == TaskConstants.CHECK_STATUS_CANCEL);
				data.putString(CheckTaskFragment.ONCHANGED_CHECKER_COMMENT, comment);
				HCTasksWatched.getInstance().notifyWatchers(data);
				ToastUtil.showInfo(getString(R.string.successful));
				break;
			default:
				ToastUtil.showInfo(response.getErrmsg());
				break;
		}
	}

	/**
	 * 听录音
	 */
	@Event(R.id.rb_check_listen_record)
	private void rb_check_listen_record(View v) {
		if (mTask == null)
			return;
		//有录音才跳转
		Map<String, Object> map = new HashMap<>();
		map.put("saler_phone", mTask.getSeller_phone());
		map.put(PhoneRecorListdActivity.KEY_TASK_TYPE, TaskConstants.MESSAGE_CHECK_TASK);
		HCActionUtil.launchActivity(this, PhoneRecorListdActivity.class, map);
	}

	/**
	 * 查保养记录
	 */
	@Event(R.id.rb_check_query_vinrecord)
	private void rb_check_query_vinrecord(View v) {
		if (mTask == null)
			return;

		if (mReport == null) {
			mReport = CheckReportEntity.createReport(mTask);
		}
		if (mReport != null) {
			//没有设置过vin
			if (TextUtils.isEmpty(mReport.getVin_code())) {
				//弹出对话框设置vin
				AlertDialogUtil.createInputVinCodeDialog(this, new OnDismissListener());
			} else {
				//发起请求
				OKHttpManager.getInstance().post(HCHttpRequestParam.getCjdReport(mTask.getId(), mReport.getVin_code()), this, 0);
			}
		}
	}

	/**
	 * 处理请求VIN码车鉴定报告
	 */
	private void responseVin(HCHttpResponse response) {
		try {
//			RspVinCodeEntity entity = new HCJsonParse().parseRspVinCodeResult(response.getData());
			RspVinCodeEntity entity = JsonParseUtil.fromJsonObject(response.getData(), RspVinCodeEntity.class);
			String vinCode = "", url = "", pdf_url = "";
			if (entity != null) {
				vinCode = entity.getVin_code();
				url = entity.getReport_url();
				pdf_url = entity.getReport_pdf();
			}

			/*
			 * 0：成功 -1：服务器异常 1：用户输入错误、2：已有报告、返回报告url、
			 * 3：有vin码在查询中、返回正在查询的vin_code、4：没有查到、5：需要发动机编号
			 */
			switch (response.getErrno()) {
				case -1:
					ToastUtil.showInfo(getString(R.string.server_no_response));
					break;
				case 0:
					//请求成功
					AlertDialogUtil.createRequestVinSuccessAutoDismissDialog(this);
					break;
				case 1:
					//用户可能输入的vin不正确
					ToastUtil.showInfo(response.getErrmsg());//查看错误信息
					//重新修改VIN码提示框
					AlertDialogUtil.createNotFoundReportDialog(this, vinCode, new OnDismissListener());
					break;
				case 2:
					//成功拿到报告，去打开url来看看
					if (!TextUtils.isEmpty(url)) {
						Map<String, Object> map = new HashMap<>();
						map.put(HCWebViewActivity.KEY_INTENT_EXTRA_URL, url);
						map.put(HCWebViewActivity.KEY_INTENT_EXTRA_DOWNLOAD_ENABLE, true);
						map.put(HCWebViewActivity.KEY_INTENT_EXTRA_DOWNLOAD_URL, pdf_url);
						map.put(HCWebViewActivity.KEY_INTENT_EXTRA_DOWNLOAD_CHECKER_REPORT_OR_CJD, HCWebViewActivity.KEY_INTENT_EXTRA_DOWNLOAD_CJD);
						map.put(HCWebViewActivity.KEY_INTENT_EXTRA_ID, mTask.getId() + "");
						HCActionUtil.launchActivity(this, HCWebViewActivity.class, map);
					}
					break;
				case 3:
					//查询中
					if (mReport == null)
						mReport = CheckReportEntity.createReport(mTask);
					if (mReport != null) {
						mReport.setVin_code(vinCode);
						CheckReportDAO.getInstance().update(mReport.getId(), mReport);
						ToastUtil.showInfo(getString(R.string.toast_query_vin, vinCode));
					}
					break;
				case 4:
					//不存在 重新修改VIN码提示框
					AlertDialogUtil.createNotFoundReportDialog(this, vinCode, new OnDismissListener());
					break;

				/**待确认  start */
				case 5:
					//需要提供发动机号以查询保养记录
					final String finalVinCode = vinCode;
					AlertDialogUtil.createInputEngineCodeDialog(this, new AlertDialogUtil.OnDismissListener() {
						@Override
						public void onDismiss(Bundle data) {
                           if(data!=null){
							   String enginCode=data.getString("engine");
							   //发起请求
							   OKHttpManager.getInstance().post(HCHttpRequestParam.getCjdReport(mTask.getId(), finalVinCode, enginCode), CheckTaskDetailActivity.this, 0);
						   }
						}
					});
					break;
				/**待确认   end */
				default:
					ToastUtil.showInfo(response.getErrmsg());
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 转单
	 */
	@Event(R.id.rb_check_transmit)
	private void rb_check_transmit(View v) {
		if (mTask == null || mTask.getCheck_status() == TaskConstants.CHECK_STATUS_CANCEL
				|| mTask.getCheck_status() == TaskConstants.CHECK_STATUS_SUCCESS || mTask.getHelp_check_status() == TaskConstants.HELP_CHECK_FAILED
				|| mTask.getHelp_check_status() == TaskConstants.HELP_CHECK_SUCCESS)//任务已经取消了、已经转给他人了、任务已经完成了/帮检取消、完成了
			return;
		Intent helpCheckIntent = new Intent(CheckTaskDetailActivity.this, TransmitCheckTaskActivity.class);
		helpCheckIntent.putExtra("taskId", mTask.getId());
		startActivityForResult(helpCheckIntent, TRANSMIT_TASK);
	}

	/**
	 * 修改地点
	 */
	@Event(R.id.rb_check_modify_address)
	private void rb_check_modify_address(View v) {
		if (mTask == null)
			return;
		Intent helpCheckIntent = new Intent(this, ModifyCheckAddressActivity.class);
		helpCheckIntent.putExtra("taskId", mTask.getId());
		startActivityForResult(helpCheckIntent, MODIFY_ADDRESS);
	}

	/**
	 * 任务导航
	 */
	@Event(R.id.rb_check_navigation)
	private void rb_check_navigation(View v) {
		if (mTask == null)
			return;

		//拿到地理信息
		BDLocation location = ((CheckerApplication) getApplication()).getLocation();
		String address = null;
		double mLat1 = 0.0;
		double mLon1 = 0.0;
		//当前位置(起点)
		if (location != null) {
			mLat1 = location.getLatitude();
			mLon1 = location.getLongitude();
			address = location.getBuildingName();
		}
		// 目的地坐标(终点)
		double mLat2 = mTask.getPlace_lat();
		double mLon2 = mTask.getPlace_lng();

		LatLng pt1 = new LatLng(mLat1, mLon1);
		LatLng pt2 = new LatLng(mLat2, mLon2);

		// 构建 导航参数
		NaviParaOption para = new NaviParaOption().startPoint(pt1).endPoint(pt2).startName(address).endName(mTask.getAppointment_place());
		try {
			try{
				BaiduMapNavigation.finish(this);
			}catch (Exception e){
				e.printStackTrace();
			}

			BaiduMapNavigation.openBaiduMapNavi(para, this);
		} catch (BaiduMapAppNotSupportNaviException e) {
			e.printStackTrace();
			showDialog();
		}
	}

	/**
	 * 录音
	 */
	@Event(R.id.rb_record)
	private void record(View view) {
		Intent intent = new Intent(this, AudioRecordActivity.class);
		startActivity(intent);
	}

	/**
	 * 创建收车
	 */
	@Event(R.id.rb_creat_recycling)
	private void rb_creat_recycling(View view) {
		if (mTask == null)
			return;

		Intent intent = new Intent(this, CheckerAddClueActivity.class);
		intent.putExtra(CheckerAddClueActivity.INTENT_KEY_CREAT_RECYCLING, mTask);
		startActivity(intent);
	}

	/**
	 * 提示未安装百度地图app或app版本过低
	 */
	public void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				OpenClientUtil.getLatestBaiduMapApp(CheckTaskDetailActivity.this);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}


	@Override
	protected void onDestroy() {
		ProgressDialogUtil.closeProgressDialog();
		try{
			BaiduMapNavigation.finish(this);
		}catch (Exception e){
			e.printStackTrace();
		}

		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case CANCLE_SUCCESS:
				case TRANSMIT_TASK:
					finish();
					break;
				case MODIFY_ADDRESS:
					//更新界面
					mTask.setAppointment_place(data.getStringExtra("appointment_place"));
					mTask.setPlace_lat(data.getDoubleExtra("place_lat", 0.0));
					mTask.setPlace_lng(data.getDoubleExtra("place_lng", 0.0));
					setData(mTask);
					//更新列表
					Bundle bundle = new Bundle();
					bundle.putBoolean(CheckTaskFragment.ONCHANGED_APPOINTMENT_PLACE, true);
					bundle.putString("appointment_place", data.getStringExtra("appointment_place"));
					HCTasksWatched.getInstance().notifyWatchers(bundle);
					break;
			}
		}
	}

	private class OnDismissListener implements AlertDialogUtil.OnDismissListener {
		@Override
		public void onDismiss(Bundle data) {
			if (data != null) {
				String vinCode = data.getString("vinCode");
				boolean determine = data.getBoolean("determine");
				if (!TextUtils.isEmpty(vinCode) && determine) {
					if (mReport == null)
						mReport = CheckReportEntity.createReport(mTask);
					if (mReport != null) {
						mReport.setVin_code(vinCode);
						CheckReportDAO.getInstance().update(mReport.getId(), mReport);
						//发起请求
						OKHttpManager.getInstance().post(HCHttpRequestParam.getCjdReport(mTask.getId(), vinCode), CheckTaskDetailActivity.this, 0);
					}
				} else {
					if (!determine)
						//弹出是否要修改vin对话框
						AlertDialogUtil.createModifyVinCodeDialog(CheckTaskDetailActivity.this, vinCode, false, this);
				}
			}
		}
	}
}
