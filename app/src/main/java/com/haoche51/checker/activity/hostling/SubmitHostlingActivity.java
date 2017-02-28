package com.haoche51.checker.activity.hostling;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonStateActivity;
import com.haoche51.checker.adapter.HostlingProjectAdapter;
import com.haoche51.checker.entity.HostlingTaskEntity;
import com.haoche51.checker.entity.PhotoEntity;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.QiNiuUploadUtil;
import com.haoche51.checker.util.ToastUtil;
import com.haoche51.checker.util.UnixTimeUtil;
import com.haoche51.checker.widget.time.ScreenInfo;
import com.haoche51.checker.widget.time.WheelMain;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.iwf.photopicker.PhotoPickerActivity;


/**
 * 提交整备
 * Created by mac on 16/01/20.
 */
public class SubmitHostlingActivity extends CommonStateActivity implements AdapterView.OnItemClickListener {

	public static final String INTENT_MODIFY_REPAIR = "intent_modify_repair", INTENTY_REPAIR = "intent_repair",
			INTENT_RESORTING = "resorting";//再次整备
	public static final int SELECT_PHOTO = 100;
	public int currentClick;
	@ViewInject(R.id.mListView)
	private ListView mListView;
	@ViewInject(R.id.tv_repair_pick_up_time)
	private TextView tv_repair_pick_up_time;
	@ViewInject(R.id.btn_positive)
	private Button btn_positive;
	private HostlingTaskEntity mTask;
	private HostlingProjectAdapter mAdatper;
	private int repair_pick_up_time;
	/**
	 * 上传图片到七牛
	 */
	private QiNiuUploadUtil qiniuUploadUtil;

	@Override
	protected int getContentView() {
		return R.layout.activity_submit_hostling;
	}

	@Override
	protected void initView() {
		super.initView();
		mTask = (HostlingTaskEntity) getIntent().getSerializableExtra(INTENTY_REPAIR);
		if (getIntent().getBooleanExtra(INTENT_MODIFY_REPAIR, false)) {
			repair_pick_up_time = mTask.getPick_up_time();
			tv_repair_pick_up_time.setText(UnixTimeUtil.formatYearMonthDay(mTask.getPick_up_time()));
			setScreenTitle(R.string.hc_hostling_modify_repair);
		} else if (getIntent().getBooleanExtra(INTENT_RESORTING, false)) {
			setScreenTitle(R.string.hc_hostling_resorting);
		} else {
			setScreenTitle(R.string.hc_hostling_add_repair);
		}
		mListView.setOnItemClickListener(this);
		setData(mTask);
		btn_positive.setText(getString(R.string.hc_hostling_commit));
	}

	@Override
	protected void initData() {
		super.initData();
	}

	@Override
	public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
		if (!isFinishing())
			ProgressDialogUtil.closeProgressDialog();
		if (action.equals(HttpConstants.ACTION_ADD_BACK_REPAIR)) {
			responseSubmit(response);
		}
	}

	/**
	 * 处理请求提交/修改整备
	 */
	private void responseSubmit(HCHttpResponse response) {
		switch (response.getErrno()) {
			case 0:
				ToastUtil.showInfo(getString(R.string.successful));
				Intent intent = new Intent();
				int fee = 0;
				try {
					List<HostlingTaskEntity.ProjectEntity> project = mTask.getProject();
					for (int i = 0; i < project.size(); i++) {
						HostlingTaskEntity.ProjectEntity lastProject = project.get(i);
						fee += Integer.parseInt(lastProject.getExpect_price());
					}
				} catch (Exception e) {
				}
				intent.putExtra("servicing_fee", fee);
				intent.putExtra("pick_up_time", mTask.getPick_up_time());
				setResult(RESULT_OK, intent);
				finish();
				break;
			default:
				ToastUtil.showInfo(response.getErrmsg());
				break;
		}
	}

	/**
	 * 设置UI界面数据
	 */
	public void setData(HostlingTaskEntity data) {
		if (data.getProject() != null && !data.getProject().isEmpty()) {
			data.getProject().clear();
		}
		//提交整备时为空项目，默认给出两个
//		if (data.getProject() == null || data.getProject().isEmpty()) {
			if (data.getProject() == null) {
				data.setProject(new ArrayList<HostlingTaskEntity.ProjectEntity>());
			}
			//默认给出两个项目
			for (int i = 0; i < 2; i++) {
				HostlingTaskEntity.ProjectEntity entity = new HostlingTaskEntity.ProjectEntity();
				entity.setRepair_id(data.getProject().size() + 1);
				entity.setPre_image(new ArrayList<String>());
				data.getProject().add(entity);
			}
//		}

		if (mAdatper == null) {
			//保存整备过的项目
			List<HostlingTaskEntity.ProjectEntity> needRemoveProjects = new ArrayList<>();
			for (HostlingTaskEntity.ProjectEntity projectEntity : mTask.getProject()) {
				//已经整备过
				if (projectEntity.getIs_over() == 1) {
					needRemoveProjects.add(projectEntity);
				}
			}
			//移除整备过的项目
			mTask.getProject().removeAll(needRemoveProjects);
			mAdatper = new HostlingProjectAdapter(this, data.getProject(), needRemoveProjects.size(), R.layout.layout_hostling_add_item_detail);
			mListView.setAdapter(mAdatper);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
		if (position != mTask.getProject().size()) return;
		if (checkInfo(false)) {
			List<HostlingTaskEntity.ProjectEntity> project = mTask.getProject();
			HostlingTaskEntity.ProjectEntity entity = new HostlingTaskEntity.ProjectEntity();
			entity.setRepair_id(project.size() + 1);
			entity.setPre_image(new ArrayList<String>());
			project.add(entity);
			mAdatper.notifyDataSetChanged();
		}
	}

	/**
	 * 检查已有的项目信息是否完整
	 */
	private boolean checkInfo(boolean isSubmit) {
		List<HostlingTaskEntity.ProjectEntity> project = mTask.getProject();
		for (int i = 0; i < project.size(); i++) {
			HostlingTaskEntity.ProjectEntity lastProject = project.get(i);
			//最后一个项目允许不填，但不允许填不完整（只有项目名称、价格、照片其中一项）
			if (i == project.size() - 1 && isSubmit) {
				if ((lastProject.getPre_image() == null || lastProject.getPre_image().isEmpty()) && TextUtils.isEmpty(lastProject.getName()) && TextUtils.isEmpty(lastProject.getExpect_price())) {
					//删除这个无用项目
					project.remove(i);
					mAdatper.notifyDataSetChanged();
					return true;
				}
			}
			if (lastProject.getPre_image() == null || lastProject.getPre_image().isEmpty()) {
				ToastUtil.showInfo("请先选择项目编号" + lastProject.getRepair_id() + "的整备前图片");
				return false;
			} else if (TextUtils.isEmpty(lastProject.getName())) {
				ToastUtil.showInfo("请先填写项目编号" + lastProject.getRepair_id() + "的项目名称");
				return false;
			} else if (TextUtils.isEmpty(lastProject.getExpect_price())) {
				ToastUtil.showInfo("请先填写项目编号" + lastProject.getRepair_id() + "的整备金额");
				return false;
			}
		}
		return true;
	}

	/**
	 * 提交/修改整备
	 */
	@Event(R.id.btn_positive)
	private void btn_positive(View v) {
		if (mTask == null) return;
		if (checkInfo(true)) {
			if (TextUtils.isEmpty(tv_repair_pick_up_time.getText()) || repair_pick_up_time == 0) {
				ToastUtil.showInfo("请选择提车时间");
				return;
			}

			ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
			//修改整备或再次发起整备
			if (getIntent().getBooleanExtra(INTENT_MODIFY_REPAIR, false) || getIntent().getBooleanExtra(INTENT_RESORTING, false)) {
				//修改或再次发起
				OKHttpManager.getInstance().post(HCHttpRequestParam.addBackRepair(mTask.getStock_id(), 1, repair_pick_up_time, mTask.getProject()), this, 0);
			} else {
				//添加
				OKHttpManager.getInstance().post(HCHttpRequestParam.addBackRepair(mTask.getStock_id(), 0, repair_pick_up_time, mTask.getProject()), this, 0);
			}
		}
	}


	private void uploadImageToQiniu(List<String> imagePathList) {

		qiniuUploadUtil = new QiNiuUploadUtil(this, null, imagePathList);
		qiniuUploadUtil.startUpload(new QiNiuUploadUtil.QiniuUploadListener() {
			@Override
			public void onSuccess(List<String> photoUrlList, List<PhotoEntity> photoList) {
				//上传成功
				List<HostlingTaskEntity.ProjectEntity> project = mTask.getProject();
				for (String key : photoUrlList) {
					project.get(currentClick).getPre_image().add(key);
					mAdatper.notifyDataSetChanged();
				}
			}
		});
	}

	/**
	 * 初始化时间滚轮
	 */
	@Event(R.id.tv_repair_pick_up_time)
	private void tv_repair_pick_up_time(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.select_tc_time));
		final View timerView = LayoutInflater.from(this).inflate(R.layout.timepicker, null);
		ScreenInfo screenInfo = new ScreenInfo(this);
		final WheelMain wheelMain = new WheelMain(timerView, true);
		wheelMain.screenheight = screenInfo.getHeight();
		final Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		wheelMain.initDateTimePicker(year, month, day, 0, 0, true);
		builder.setView(timerView);
		builder.setNegativeButton(getString(R.string.soft_update_cancel), null);
		builder.setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				int transferTime = UnixTimeUtil.getUnixTime(wheelMain.getTime());
				repair_pick_up_time = transferTime;
				if (transferTime < calendar.getTime().getTime() / 1000L) {
					ToastUtil.showInfo(getString(R.string.no_choose));
				}else {
					tv_repair_pick_up_time.setText(UnixTimeUtil.formatYearMonthDay(transferTime));
					mTask.setPick_up_time(transferTime);
				}
			}
		});
		builder.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		List<String> photos = null;
		switch (requestCode) {
			case SELECT_PHOTO: // 选择图片返回
				if (data != null)
					photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
				if (photos != null) {
					//上传本张图片
					uploadImageToQiniu(photos);
				}
				break;
		}
	}

	@Override
	protected void onDestroy() {
		//关闭对话框
		ProgressDialogUtil.closeProgressDialog();
		if (qiniuUploadUtil != null) {
			qiniuUploadUtil.stopUpload();
			qiniuUploadUtil = null;
		}
		super.onDestroy();
	}
}