package com.haoche51.checker.activity.evaluate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonBaseActivity;
import com.haoche51.checker.adapter.CancleTaskPhotoAdapter;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.listener.HCTasksWatched;
import com.haoche51.checker.net.HCHttpCallback;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.QiniuUploadImageUtil;
import com.haoche51.checker.util.ToastUtil;
import com.haoche51.checker.util.UnixTimeUtil;
import com.haoche51.checker.widget.time.ScreenInfo;
import com.haoche51.checker.widget.time.WheelMain;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

public class CancelTaskActivity extends CommonBaseActivity implements OnClickListener, HCHttpCallback {

    private final static String TAG = "HCCancelTaskActivity";

    //	private Context mContext;
    private static final int SELECT_PHOTO = 100;
    /**
     * 取消原因
     */
    private Spinner cancelReasonSpn;
    /**
     * 取消说明
     */
    private Spinner cancelDescriptSpn;
    /**
     * 自行预约时间
     */
    private int mTransferTime;
    private RadioGroup mGroup;
    private TextView warning, mChangedTimeTv;
    private LinearLayout mLayout, photoLayout, mChangedTimeLayout, hasReachDoorLayout, mChangedTimeReasonLayout;//reasonLayout
    private Button confirm_btn, cancel_btn;
    private ImageView iv_photo;
    private GridView gv_photo;
    private EditText edt_remark, mchangedTimeReasonEt;
    private List<String> imagePathList = new ArrayList<>();
    private CancleTaskPhotoAdapter adapter = null;
    private int mTaskId;
    private String reason, remark;
    private int onsite;// 是否上门: 0-未上门 1-上门.事故车 2-上门.其他

    /**
     * 标识自行预约时间是否合法
     */
    private boolean isChangedTimeValid = true;
    private int maxSelectCount = 5;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_cancel_task);
        registerTitleBack();
        setScreenTitle(R.string.p_canceltask);
//		mContext = this;
        mGroup = (RadioGroup) findViewById(R.id.rg_onsite);
        mLayout = (LinearLayout) findViewById(R.id.ll_second_desc);
        cancelReasonSpn = (Spinner) findViewById(R.id.spn_cancel_reason);
        cancelDescriptSpn = (Spinner) findViewById(R.id.spn_cancel_desc);
        warning = (TextView) findViewById(R.id.tv_warning);
        photoLayout = (LinearLayout) findViewById(R.id.linearLayout3);
        confirm_btn = (Button) findViewById(R.id.bt_left);
        cancel_btn = (Button) findViewById(R.id.bt_right);
        iv_photo = (ImageView) findViewById(R.id.iv_photo);
        gv_photo = (GridView) findViewById(R.id.gv_photo);
        edt_remark = (EditText) findViewById(R.id.edt_remark);

        hasReachDoorLayout = (LinearLayout) findViewById(R.id.linearLayout1);
        mChangedTimeLayout = (LinearLayout) findViewById(R.id.linear_changedtime);
        mChangedTimeTv = (TextView) findViewById(R.id.tv_changedtime);
        mChangedTimeTv.setOnClickListener(this);

        mChangedTimeReasonLayout = (LinearLayout) findViewById(R.id.linear_changedtime_reason);
        mchangedTimeReasonEt = (EditText) findViewById(R.id.et_changedtime_reason);
        confirm_btn.setText(getString(R.string.action_ok));
        cancel_btn.setText(getString(R.string.p_cancel));
        confirm_btn.setVisibility(View.VISIBLE);
        cancel_btn.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        super.initData();
        initClick();
        mTaskId = getIntent().getIntExtra("id", 0);
        Log.d(TAG, "mTaskId = " + mTaskId);
        adapter = new CancleTaskPhotoAdapter(this, imagePathList, R.layout.activity_cancel_task_gvitem);
        gv_photo.setAdapter(adapter);
    }

    private void initClick() {
        cancelReasonSpn.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                warning.setVisibility(View.GONE);
                if (position == 2) {//不符合验车标准
                    mLayout.setVisibility(View.VISIBLE);
                } else {
                    mLayout.setVisibility(View.GONE);
                }
                if (position == 1) {//事故车
                    photoLayout.setVisibility(View.VISIBLE);
                    mGroup.check(R.id.rb_onsite);
                } else {
                    photoLayout.setVisibility(View.GONE);
                }
                if (position == 5) {// 自行预约
                    mChangedTimeLayout.setVisibility(View.VISIBLE);
                    mChangedTimeReasonLayout.setVisibility(View.VISIBLE);
                    hasReachDoorLayout.setVisibility(View.GONE);
                } else {
                    mChangedTimeLayout.setVisibility(View.GONE);
                    mChangedTimeReasonLayout.setVisibility(View.GONE);
                    hasReachDoorLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        confirm_btn.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);
        iv_photo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_left:
                submitData();
                break;
            case R.id.bt_right:
                finish();
                break;
            case R.id.iv_photo:
                openActivity();
                break;
            case R.id.tv_changedtime:
                showTimeWhell();
                break;
        }
    }

    /**
     * 打开选择相片Activity
     */
    private void openActivity() {
        int targetCount = maxSelectCount - this.imagePathList.size();
        if (targetCount != 0) {
            PhotoPickerIntent intent = new PhotoPickerIntent(this);
            intent.setPhotoCount(targetCount);//最多只能选5张图
            intent.setShowCamera(false);
            startActivityForResult(intent, SELECT_PHOTO);
        } else
            ToastUtil.showInfo(getString(R.string.select_maxcount));
    }


    private void submitData() {
        if (mChangedTimeLayout.getVisibility() == View.VISIBLE) {
            if (!isChangedTimeValid || TextUtils.isEmpty(mChangedTimeTv.getText())) {
                ToastUtil.showText(R.string.hc_day_invalid);
                return;
            }
            if (TextUtils.isEmpty(mchangedTimeReasonEt.getText())) {
                ToastUtil.showText(R.string.hc_please_input_reservation);
                mchangedTimeReasonEt.requestFocus();
                return;
            }
            doChangeCheckTime();
            return;
        }
        reason = getCancelReason();
        // 事故车说明
        remark = edt_remark.getText().toString();

        if (getCancelReason() != null) {
            if (cancelReasonSpn.getSelectedItemPosition() == 1) {
                if (mGroup.getCheckedRadioButtonId() == R.id.rb_onsite) {
                    onsite = TaskConstants.CHECK_ONSITE_ACCIDENT;//事故车上门
                } else {
                    onsite = TaskConstants.CHECK_ONSITE_ISNO;//未上门
                }
                if (imagePathList.size() < 3) {
                    ToastUtil.showInfo(getString(R.string.select_mincount));
                    return;
                }
                if (TextUtils.isEmpty(remark)) {
                    edt_remark.requestFocus();
                    ToastUtil.showInfo(getString(R.string.hint_remark));
                    return;
                }
                //上传图片
                uploadImageToQiniu();
            } else {
                if (mGroup.getCheckedRadioButtonId() == R.id.rb_onsite) {
                    onsite = TaskConstants.CHECK_ONSITE_OTHER;//上门其他
                } else {
                    onsite = TaskConstants.CHECK_ONSITE_ISNO;//未上门
                }
                ProgressDialogUtil.showProgressDialog(this, null);
                cancleTask(null);
            }
        }
    }

    /**
     * 上传图片到七牛
     */
    private void uploadImageToQiniu() {
        QiniuUploadImageUtil qiniuUploadImageUtil = new QiniuUploadImageUtil(this, this.imagePathList);
        qiniuUploadImageUtil.startUpload(new QiniuUploadImageUtil.QiniuUploadListener() {
            @Override
            public void onSuccess(List<String> keys) {
                //上传完成，调用取消任务
                cancleTask(keys);
            }

            @Override
            public void onFailed() {
            }
        });
    }


    /**
     * 请求server取消任务
     */
    private void cancleTask(List<String> keys) {
        OKHttpManager.getInstance().post(HCHttpRequestParam.cancelCheckTask(mTaskId, reason, onsite, keys, remark), this, 0);
    }

    /**
     * 处理自行重新预约
     */
    private void doChangeCheckTime() {
        String mReason = mchangedTimeReasonEt.getText().toString();
        ProgressDialogUtil.showProgressDialog(this, null);
        OKHttpManager.getInstance().post(HCHttpRequestParam.changeCheckTime(mTaskId, mTransferTime, mReason), this, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<String> photos = null;
        switch (requestCode) {
            case SELECT_PHOTO: // 选择图片返回
                if (data != null)
                    photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                if (photos != null) {
                    imagePathList.addAll(photos);
                    adapter.setmList(imagePathList);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    // 获取取消原因
    public String getCancelReason() {
        if (cancelReasonSpn.getSelectedItemPosition() == 0) {
            warning.setVisibility(View.VISIBLE);
            warning.setText(getString(R.string.hint_cancel_reason));
            return null;
        }
        if (cancelReasonSpn.getSelectedItemPosition() == 2) {
            if (cancelDescriptSpn.getSelectedItemPosition() == 0) {
                warning.setVisibility(View.VISIBLE);
                warning.setText(getString(R.string.p_select_cancel_desc));
                return null;
            }
            return cancelDescriptSpn.getSelectedItem().toString();
        }
        return cancelReasonSpn.getSelectedItem().toString();
    }

    /**
     * 取消进度对话框
     */
    private void cancelDialog() {
        if (!isFinishing()) {
            ProgressDialogUtil.closeProgressDialog();
        }
    }

    // 点击EditText其他地方隐藏输入法
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            imm.hideSoftInputFromWindow(edt_remark.getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("CancelTaskPage");
        MobclickAgent.onResume(getApplicationContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("CancelTaskPage");
        MobclickAgent.onPause(getApplicationContext());
    }

    private void showTimeWhell() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.hc_re_reservation);
        final View timerView = LayoutInflater.from(this).inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo = new ScreenInfo(CancelTaskActivity.this);
        final WheelMain wheelMain = new WheelMain(timerView, true);
        wheelMain.screenheight = screenInfo.getHeight();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, day, 0, 0, 0);
        wheelMain.initDateTimePicker(year, month, day, 0, 0, true);
        final long nowSecond = calendar.getTimeInMillis() / 1000;
        final long daySecond = 24 * 60 * 60;
        final long endTime = daySecond * 7 + nowSecond;
        builder.setView(timerView);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String returnTime = wheelMain.getTime();
                mTransferTime = UnixTimeUtil.getUnixTime(returnTime);
                isChangedTimeValid = mTransferTime > nowSecond && mTransferTime <= endTime;
                if (isChangedTimeValid) {
                    long diff = (mTransferTime - nowSecond) / daySecond;
                    ToastUtil.showText(getString(R.string.hc_after_day, diff));
                    // 判断返回的时间是否为今天以后的时间
                    mChangedTimeTv.setText(UnixTimeUtil.formatYearMonthDay(mTransferTime));
                } else {
                    ToastUtil.showText(R.string.hc_day_invalid);
                }
            }
        });
        builder.show();
    }


    @Override
    public void onHttpStart(String action, int requestId) {
    }

    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        if (response.getErrno() == 0) {
            ToastUtil.showInfo(getString(R.string.cancel_task_success));
            HCTasksWatched.getInstance().notifyWatchers();
            cancelDialog();
            setResult(RESULT_OK);
            finish();
        } else {
            ToastUtil.showInfo(response.getErrmsg());
            cancelDialog();
        }
    }

    @Override
    public void onHttpProgress(String action, int requestId, long bytesWritten, long totalSize) {
    }

    @Override
    public void onHttpRetry(String action, int requestId, int retryNo) {
    }

    @Override
    public void onHttpFinish(String action, int requestId) {
    }

    @Override
    protected void onDestroy() {
        ProgressDialogUtil.closeProgressDialog();
        super.onDestroy();
    }
}
