package com.haoche51.checker.activity.evaluate;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.haoche51.checker.DAO.CheckReportDAO;
import com.haoche51.checker.DAO.DataObserver;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.HCWebViewActivity;
import com.haoche51.checker.activity.widget.CommonStateActivity;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.CheckReportEntity;
import com.haoche51.checker.entity.CheckTaskEntity;
import com.haoche51.checker.entity.RspVinCodeEntity;
import com.haoche51.checker.entity.UploadCheckTaskEntity;
import com.haoche51.checker.helper.UploadServiceHelper;
import com.haoche51.checker.listener.HCTasksWatched;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.AlertDialogUtil;
import com.haoche51.checker.util.HCActionUtil;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.util.NetInfoUtil;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;
import com.haoche51.checker.util.UnixTimeUtil;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class UploadTaskDetailActivity extends CommonStateActivity {

    @ViewInject(R.id.tv_vehicle_name)
    private TextView tv_vehicle_name;

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

    @ViewInject(R.id.pb_progress)
    private ProgressBar pb_progress;

    @ViewInject(R.id.tv_upload_progress)
    private TextView tv_upload_progress;

    @ViewInject(R.id.tv_upload_max)
    private TextView tv_upload_max;

    @ViewInject(R.id.tv_upload_status)
    private TextView tv_upload_status;

    @ViewInject(R.id.tv_upload_speed)
    private TextView tv_upload_speed;

    @ViewInject(R.id.tv_upload_used_times)
    private TextView tv_upload_used_times;

    private int taskId;//根据任务Id去请求任务详情
    private CheckTaskEntity mTask;
    private CheckReportEntity mReport;
    private String comment;
    private long lastTotalTxBytes = 0;
    private long lastTimeStamp = 0;
    private Timer mTimer;
    private UploadCheckTaskEntity uploadCheckTask;
    private Drawable blueProgress;
    private Drawable redProgress;
    private int blackColor;//黑色
    private int redColor;//红色
    /**
     * 当前的时间戳
     */
    private Long nowMills;

    /**
     * 上传中的任务在列表中的位置
     */
    private int position;

    /**
     * 时间差：现在-之前的=nowMills-lastMills
     */
    private Long totalMills;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == TaskConstants.UPLOAD_NET_SPEED) {
                //网速
                tv_upload_speed.setText((String) msg.obj);
                if (uploadCheckTask == null) {
                    return;
                }
                //总耗时
                totalMills = nowMills - uploadCheckTask.getStartMills();
                nowMills = System.currentTimeMillis();
                tv_upload_used_times.setText(UnixTimeUtil.timeStampToDate(totalMills, ""));
            }
        }
    };

    private DataObserver mDataObserver = new DataObserver(new Handler()) {

        @Override
        public void onChanged() {

        }

        @Override
        public void onChanged(Bundle data) {
            if (data != null && data.getBoolean(TaskConstants.BINDLE_DETAIL_TASK, false) && uploadCheckTask != null) {
                UploadCheckTaskEntity tempTaskEntity = data.getParcelable(TaskConstants.BINDLE_UPLOAD_TASK);
                if (tempTaskEntity != null && tempTaskEntity.getCheckTaskId() == uploadCheckTask.getCheckTaskId()) {
                    pb_progress.setProgress(uploadCheckTask.getProgress());
                    pb_progress.setMax(uploadCheckTask.getMax());
                    tv_upload_max.setText(String.valueOf(uploadCheckTask.getMax()));
                    tv_upload_status.setText(uploadCheckTask.getUploadStatus());
                    tv_upload_progress.setText(String.valueOf(uploadCheckTask.getProgress()));
                }
            }
        }
    };
    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            showNetUploadSpeed();
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_uploadtask_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        lastTotalTxBytes = NetInfoUtil.getTotalTxBytes();
        lastTimeStamp = System.currentTimeMillis();
        setScreenTitle(R.string.hc_check_task_title);
    }

    @Override
    protected void initData() {
        super.initData();
        //获取查询的任务id
        taskId = getIntent().getIntExtra(TaskConstants.INTENT_EXTRA_ID, 0);
        if (taskId > 0) {
            //获取该条记录在上传中列表的哪个位置
            position = getIntent().getIntExtra(TaskConstants.INTENT_EXTRA_POSITION, 0);
            //根据任务id查询本地报告
            mReport = CheckReportDAO.getInstance().getByTaskId(taskId);
            // 1s后启动任务，每1s执行一次
            mTimer = new Timer();
            mTimer.schedule(mTimerTask, 1000, 1000);
        }
    }


    /**
     * 初始化上传进度相关
     */
    private void initUploadProgress() {
        this.blueProgress = getResources().getDrawable(R.drawable.progressbar_blue);
        //进度条颜色 红色
        this.redProgress = getResources().getDrawable(R.drawable.progressbar_red);
        this.blackColor = getResources().getColor(R.color.hc_self_black);
        this.redColor = getResources().getColor(R.color.hc_self_red);

        //任务列表中所有任务均已上传完成
        if (UploadServiceHelper.mUploadList == null || UploadServiceHelper.mUploadList.size() == 0
                || UploadServiceHelper.mUploadList.size() <= position) {
            showUploadSuccess();
            return;
        }

        uploadCheckTask = UploadServiceHelper.mUploadList.get(position);
        //对应不上：说明该任务已经上传成功，被移除了
        if (uploadCheckTask.getCheckTaskId() != taskId) {
            showUploadSuccess();
            return;
        }

        //任务处于上传中时，注册监听器
        HCTasksWatched.getInstance().registerDataObserver(mDataObserver);

        //任务上传状态：上传中或者中断或者排队中
        pb_progress.setProgress(uploadCheckTask.getProgress());
        pb_progress.setMax(uploadCheckTask.getMax());
        tv_upload_status.setText(uploadCheckTask.getUploadStatus());
        tv_upload_progress.setText(String.valueOf(uploadCheckTask.getProgress()));
        tv_upload_max.setText(String.valueOf(uploadCheckTask.getMax()));
        nowMills = System.currentTimeMillis();
        //总耗时
        totalMills = nowMills - uploadCheckTask.getStartMills();
        tv_upload_used_times.setText(UnixTimeUtil.timeStampToDate(totalMills, ""));
        showDisplayStyle();
    }

    /**
     * 展示上传成功的进度条布局
     */
    private void showUploadSuccess() {
        pb_progress.setMax(100);
        pb_progress.setProgress(100);
        //进度条蓝色
        pb_progress.setProgressDrawable(blueProgress);
        tv_upload_status.setText(TaskConstants.UPLOAD_STATUS_REPORT_FINISHED);//已完成
        //字体黑色
        tv_upload_status.setTextColor(blackColor);
        tv_upload_progress.setText(String.valueOf(100));
        tv_upload_max.setText(String.valueOf(100));
    }

    /**
     * 展示不同状态下的显示风格
     */
    private void showDisplayStyle() {
        //非中断：排队中或者上传中
        if (!TaskConstants.UPLOAD_STATUS_STOP.equals(uploadCheckTask.getUploadStatus())) {
            pb_progress.setProgressDrawable(blueProgress);
            //字体黑色
            tv_upload_status.setTextColor(blackColor);
        } else {
            //进度条颜色 红色
            pb_progress.setProgressDrawable(redProgress);
            //字体红色
            tv_upload_status.setTextColor(redColor);
        }
    }

    /**
     * 显示网络上传速度
     * 思路：每隔一个时间段就去获取这个时间段获取到的网络数据的大小，
     * 然后除以花费的时间，即获得当前时间段的网速值
     */
    private void showNetUploadSpeed() {
        //获取上传总流量，单位:KB
        long nowTotalTxBytes = NetInfoUtil.getTotalTxBytes();
        long nowTimeStamp = System.currentTimeMillis();
        long speed = ((nowTotalTxBytes - lastTotalTxBytes) * 1000 / (nowTimeStamp - lastTimeStamp));//毫秒转换

        lastTimeStamp = nowTimeStamp;
        lastTotalTxBytes = nowTotalTxBytes;

        Message msg = mHandler.obtainMessage();
        msg.what = TaskConstants.UPLOAD_NET_SPEED;
        msg.obj = String.valueOf(speed).concat("kb/s");
        mHandler.sendMessage(msg);//更新界面
    }


    /**
     * 关闭定时器
     */
    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    @Override
    protected void onDestroy() {
        ProgressDialogUtil.closeProgressDialog();
        //停止获取网速
        stopTimer();
        //移除数据监听器
        if (mDataObserver != null) {
            HCTasksWatched.getInstance().UnRegisterDataObserver(mDataObserver);
            mDataObserver = null;
        }
        uploadCheckTask = null;
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        if (taskId > 0) {
            initUploadProgress();
            ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
            OKHttpManager.getInstance().post(HCHttpRequestParam.getCheckTask(taskId), this, 0);
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        ProgressDialogUtil.closeProgressDialog();
        super.onStop();
    }

    /**
     * 处理请求任务详情
     */
    private void responseTask(HCHttpResponse response) {
        switch (response.getErrno()) {
            case 0:
//        mTask = new HCJsonParse().parseGetCheckTask(response.getData());
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
        }
    }


    /**
     * 设置UI界面数据
     */
    public void setData(CheckTaskEntity data) {
        tv_vehicle_name.setText(data.getVehicle_name());
        tv_check_seller_name.setText(data.getSeller_name());
        tv_check_seller_phone.setText(data.getSeller_phone());
        tv_check_task_id.setText(String.valueOf(data.getId()));
        tv_check_appoint_time.setText(UnixTimeUtil.format(data.getAppointment_starttime()));
        tv_check_app_add.setText(data.getAppointment_place());
        tv_check_comment.setText(data.getComment());
        tv_checker_comment.setText(data.getChecker_comment());
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
                    OKHttpManager.getInstance().post(HCHttpRequestParam.addVehiclecheckComment(mTask.getId(), comment), UploadTaskDetailActivity.this, 0);
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
                data.putBoolean("finishtask", mTask.getCheck_status() == TaskConstants.CHECK_STATUS_SUCCESS
                        || mTask.getCheck_status() == TaskConstants.CHECK_STATUS_CANCEL);
                data.putString("checker_comment", comment);
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
//      RspVinCodeEntity entity = new HCJsonParse().parseRspVinCodeResult(response.getData());
            RspVinCodeEntity entity = JsonParseUtil.fromJsonObject(response.getData(), RspVinCodeEntity.class);
            String vinCode = "", url = "", pdf_url = "";
            if (entity != null) {
                vinCode = entity.getVin_code();
                url = entity.getReport_url();
                pdf_url = entity.getReport_pdf();
            }

            //0：成功 -1：服务器异常 1：用户输入错误、2：已有报告、返回报告url、3：有vin码在查询中、返回正在查询的vin_code、4：没有查到
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
                    if (!TextUtils.isEmpty(url) && mTask != null) {
                        Map<String, Object> map = new HashMap<>();
                        map.put(HCWebViewActivity.KEY_INTENT_EXTRA_ID, String.valueOf(mTask.getId()));
                        map.put(HCWebViewActivity.KEY_INTENT_EXTRA_URL, url);
                        map.put(HCWebViewActivity.KEY_INTENT_EXTRA_DOWNLOAD_ENABLE, true);
                        map.put(HCWebViewActivity.KEY_INTENT_EXTRA_DOWNLOAD_URL, pdf_url);
                        map.put(HCWebViewActivity.KEY_INTENT_EXTRA_DOWNLOAD_CHECKER_REPORT_OR_CJD, HCWebViewActivity.KEY_INTENT_EXTRA_DOWNLOAD_CJD);
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
                            if (data != null) {
                                String enginCode = data.getString("engine");
                                //发起请求
                                OKHttpManager.getInstance().post(HCHttpRequestParam.getCjdReport(mTask.getId(), finalVinCode, enginCode), UploadTaskDetailActivity.this, 0);
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

    @Override
    public void finish() {
        ProgressDialogUtil.closeProgressDialog();
        super.finish();
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
                        OKHttpManager.getInstance().post(HCHttpRequestParam.getCjdReport(mTask.getId(), vinCode), UploadTaskDetailActivity.this, 0);
                    }
                } else {
                    if (!determine)
                        //是否要修改vin对话框
                        AlertDialogUtil.createModifyVinCodeDialog(UploadTaskDetailActivity.this, vinCode, false, this);
                }
            }
        }
    }


}
