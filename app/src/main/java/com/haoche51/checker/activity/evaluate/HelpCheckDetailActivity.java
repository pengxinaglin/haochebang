package com.haoche51.checker.activity.evaluate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonStateActivity;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.CheckTaskEntity;
import com.haoche51.checker.listener.HCTasksWatched;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;
import com.haoche51.checker.util.UnixTimeUtil;
import com.haoche51.checker.widget.time.ScreenInfo;
import com.haoche51.checker.widget.time.WheelMain;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

import org.xutils.view.annotation.ViewInject;

import java.util.Calendar;

/**
 * 帮检验车任务详情
 */
public class HelpCheckDetailActivity extends CommonStateActivity {

	@ViewInject(R.id.rgp_helpcheck)
	private RadioGroup mRgp;

	@ViewInject(R.id.rel_has_charge)
	private RelativeLayout mChargeRel;

	@ViewInject(R.id.btn_helpcheck_confirm)
	private Button mConfirmBtn;

	@Required(order = 1, message = "未收费请填0")
	@TextRule(order = 2, maxLength = 9, message = "金额过大")
	@ViewInject(R.id.et_has_charge)
	private EditText mHasChargeEt;

	@ViewInject(R.id.linear_success)
	private LinearLayout mSuccessLayout;

	@Required(order = 3, message = "未收服务费请填0")
	@TextRule(order = 4, maxLength = 9, message = "金额过大")
	@ViewInject(R.id.et_commission)
	private EditText mComission; // 服务费

	@Required(order = 5, message = "无定金请填0")
	@TextRule(order = 6, maxLength = 9, message = "金额过大")
	@ViewInject(R.id.et_prepay)
	private EditText mPrepay;

	@Required(order = 6, message = "买家姓名必填")
	@ViewInject(R.id.et_buyername)
	private EditText buyerName;

	@Required(order = 7, message = "买家电话必填")
	@TextRule(order = 8, minLength = 11, message = "号码必须为11位")
	@ViewInject(R.id.et_buyerphone)
	private EditText buyerPhone;

	@Required(order = 9, message = "车主姓名必填")
	@ViewInject(R.id.et_seller_name)
	private EditText sellerName;

	@Required(order = 10, message = "车主电话必填")
	@TextRule(order = 11, minLength = 11, message = "号码必须为11位")
	@ViewInject(R.id.et_seller_phone)
	private EditText sellerPhone;

	@Required(order = 12, message = "过户时间必填")
	@ViewInject(R.id.et_transfertime)
	private EditText transferTime;

	@ViewInject(R.id.btn_time)
	private Button selectTime;

	@Required(order = 13, message = "过户地点必填")
	@ViewInject(R.id.et_transferplace)
	private EditText transferPlace;

	@Required(order = 14, message = "成交价必填")
	@TextRule(order = 15, maxLength = 9, message = "金额过大")
	@ViewInject(R.id.et_price)
	private EditText finalPrice;

	private CheckTaskEntity hTask;

	@Override
	protected int getContentView() {
		return R.layout.activity_helpcheck_detail;
	}

	@Override
	protected void initView() {
		super.initView();
		setScreenTitle("帮检");
		mRgp.setOnCheckedChangeListener(mCheckedChangedListener);
		mConfirmBtn.setOnClickListener(mClickListener);
		transferTime.setOnClickListener(mClickListener);
		selectTime.setOnClickListener(mClickListener);
	}

	@Override
	protected void initData() {
		super.initData();
		int task_id = this.getIntent().getExtras().getInt("taskId");
		if (task_id > 0) {
			ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
			OKHttpManager.getInstance().post(HCHttpRequestParam.getCheckTask(task_id), this, 0);
		}
	}


	private OnCheckedChangeListener mCheckedChangedListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (hTask == null)
				return;
			switch (checkedId) {
				case R.id.rb_fail: // 帮检失败
					mChargeRel.setVisibility(View.GONE);
					mSuccessLayout.setVisibility(View.GONE);
					hTask.setHelp_check_status(TaskConstants.HELP_CHECK_FAILED);
					break;
				case R.id.rb_succ_transfer: // 帮检成功自行过户
					mChargeRel.setVisibility(View.VISIBLE);
					mSuccessLayout.setVisibility(View.GONE);
					hTask.setHelp_check_status(TaskConstants.HELP_CHECK_SUCCESS);
					break;
				case R.id.rb_succ_procedure:// 帮检成功走流程要质保
					mChargeRel.setVisibility(View.GONE);
					mSuccessLayout.setVisibility(View.VISIBLE);
					hTask.setHelp_check_status(TaskConstants.HELP_CHECK_DEAL);
					break;
			}
		}
	};

	private View.OnClickListener mClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.btn_helpcheck_confirm:
					validator.validate();
					break;
				case R.id.btn_time:
					updateTransferTime();
					break;
			}

		}
	};


	private void updateTransferTime() {
		AlertDialog.Builder builder = new AlertDialog.Builder(HelpCheckDetailActivity.this);
		builder.setTitle(getString(R.string.select_transfer_time));
		final View timerView = LayoutInflater.from(HelpCheckDetailActivity.this).inflate(R.layout.timepicker, null);
		ScreenInfo screenInfo = new ScreenInfo(HelpCheckDetailActivity.this);
		final WheelMain wheelMain = new WheelMain(timerView, true);
		wheelMain.screenheight = screenInfo.getHeight();
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		wheelMain.initDateTimePicker(year, month, day, hour, min);
		builder.setView(timerView);
		builder.setNegativeButton(getString(R.string.soft_update_cancel), null);
		builder.setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				transferTime.setText(wheelMain.getTime());
			}
		});
		builder.show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				this.finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	// 校验完成， 发送请求到服务器
	@Override
	public void onValidationSucceeded() {
		updateHelpCheckResult();
		super.onValidationSucceeded();
	}

	@Override
	public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
		if (action.equals(HttpConstants.ACTION_GET_CHECKTASK)) {
			responseTask(response);
		} else if (action.equals(HttpConstants.ACTION_COMPLETE_HELPCHECK)) { //帮检完成
			if (hTask == null)
				return;
			switch (response.getErrno()) {
				case 0:
					switch (hTask.getHelp_check_status()) {
						case TaskConstants.HELP_CHECK_FAILED://帮检失败
						case TaskConstants.HELP_CHECK_SUCCESS://帮检成功自行过户
							HCTasksWatched.getInstance().notifyWatchers();//通知列表删除这条数据
							break;
					}
					setResult(RESULT_OK);
					finish();
					break;
				default:
					Toast.makeText(HelpCheckDetailActivity.this, response.getErrmsg(), Toast.LENGTH_SHORT).show();
					break;

			}
		}
		ProgressDialogUtil.closeProgressDialog();
	}

	/**
	 * 处理请求任务详情
	 */
	private void responseTask(HCHttpResponse response) {
		switch (response.getErrno()) {
			case 0:
//				hTask = new HCJsonParse().parseGetCheckTask(response.getData());
				hTask = JsonParseUtil.fromJsonObject(response.getData(), CheckTaskEntity.class);
				break;
			default:
				ToastUtil.showInfo(response.getErrmsg());
				break;
		}
	}

	/**
	 * 上传帮检信息
	 */
	private void updateHelpCheckResult() {
		if (hTask == null)
			return;

		ProgressDialogUtil.showProgressDialog(HelpCheckDetailActivity.this, null);
		int commission = 0;
		String seller_name = "";
		String seller_phone = "";
		String buyer_name = "";
		String buyer_phone = "";
		int transfer_time = 0;
		String transfer_place = "";
		int price = 0;
		int prepay = 0;
		//TODO 在任务中添加  买家电话， 过户时间地点
		switch (hTask.getHelp_check_status()) {
			case TaskConstants.HELP_CHECK_SUCCESS: // 只帮检
				commission = Integer.parseInt(mHasChargeEt.getText().toString());
				break;
			case TaskConstants.HELP_CHECK_DEAL: // 成交
				commission = Integer.parseInt(mComission.getText().toString());
				seller_name = sellerName.getText().toString();
				seller_phone = sellerPhone.getText().toString();
				buyer_name = buyerName.getText().toString();
				buyer_phone = buyerPhone.getText().toString();
				transfer_time = UnixTimeUtil.getUnixTime(transferTime.getText().toString());
				transfer_place = transferPlace.getText().toString();
				price = Integer.parseInt(finalPrice.getText().toString());
				prepay = Integer.parseInt(mPrepay.getText().toString());
				break;
			default: // 默认失败 不做处理
				hTask.setHelp_check_status(1);
				break;

		}
		OKHttpManager.getInstance().post(HCHttpRequestParam.helpCheckComplete(hTask, commission, seller_name, seller_phone, buyer_name, buyer_phone, transfer_time, transfer_place, price, prepay), this, 0);
	}


}
