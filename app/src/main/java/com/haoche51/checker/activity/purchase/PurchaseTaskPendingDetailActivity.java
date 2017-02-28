package com.haoche51.checker.activity.purchase;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.HCWebViewActivity;
import com.haoche51.checker.activity.offerrefer.OfferReferActivity;
import com.haoche51.checker.activity.widget.CommonStateActivity;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.PurchaseTaskEntity;
import com.haoche51.checker.entity.RspVinCodeEntity;
import com.haoche51.checker.listener.HCTasksWatched;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.AlertDialogUtil;
import com.haoche51.checker.util.HCActionUtil;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.Map;

/**
 * 待处理收车任务状态的详情页面
 */
public class PurchaseTaskPendingDetailActivity extends CommonStateActivity implements AlertDialogUtil.OnDismissListener {

    private final int TRANSMIT_TASK = 100, TASK_FAILED = 200, TASK_SUCCESS=300;
    @ViewInject(R.id.tv_status)
    private TextView tv_status;
    @ViewInject(R.id.tv_title)
    private TextView tv_title;
    @ViewInject(R.id.tv_seller_name)
    private TextView tv_seller_name;
    @ViewInject(R.id.tv_seller_phone)
    private TextView tv_seller_phone;
    @ViewInject(R.id.tv_clue_add_user_role)
    private TextView tv_clue_add_user_role;//线索提供人角色
    @ViewInject(R.id.tv_clue_add_user_name)
    private TextView tv_clue_add_user_name;//线索提供人姓名
    @ViewInject(R.id.tv_clue_add_user_phone)
    private TextView tv_clue_add_user_phone;//线索提供人电话
    @ViewInject(R.id.tv_purchase_task_id)
    private TextView tv_purchase_task_id;
    @ViewInject(R.id.tv_purchase_vehicle_source)
    private TextView tv_purchase_vehicle_source;
    @ViewInject(R.id.tv_purchase_comment)
    private TextView tv_purchase_comment;
    @ViewInject(R.id.btn_negative)
    private Button btn_negative;
    @ViewInject(R.id.btn_positive)
    private Button btn_positive;
    @ViewInject(R.id.rb_purchase_transmit)
    private RadioButton rb_purchase_transmit;
    @ViewInject(R.id.rb_offer)
    private RadioButton rb_offer;
    @ViewInject(R.id.rl_source)
    private RelativeLayout rl_source;//车源编号
    @ViewInject(R.id.tv_purchase_source_id)
    private TextView tv_purchase_source_id;
    private int taskId;//根据任务Id去请求任务详情
    private PurchaseTaskEntity mTask;//根据任务Id请求的任务详情
    private boolean isAssign;

//    private DataObserver mDataObserver;


    @Override
    protected int getContentView() {
        return R.layout.activity_purchasetask_pending_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        setScreenTitle(R.string.hc_purchasetask_detail_title);
        btn_negative.setText(getString(R.string.hc_purchasetask_failure));
        btn_positive.setText(getString(R.string.hc_purchasetask_buyback));
    }

    @Override
    protected void initData() {
        super.initData();
        //获取查询的任务id
        taskId = getIntent().getIntExtra("id", 0);
        isAssign = getIntent().getBooleanExtra(TaskConstants.BINDLE_IS_ASSIGN, false);
        if (isAssign) {
            rb_purchase_transmit.setText(getString(R.string.hc_assign));
//            btn_negative.setVisibility(View.GONE);
//            btn_positive.setVisibility(View.GONE);
            rb_offer.setVisibility(View.INVISIBLE);
        } else {
            rb_purchase_transmit.setText(getString(R.string.p_transmit));
            btn_negative.setVisibility(View.VISIBLE);
            btn_positive.setVisibility(View.VISIBLE);
//            registDataObserver();
        }
    }

//    private void registDataObserver() {
//        mDataObserver = new DataObserver(new Handler()) {
//            @Override
//            public void onChanged() {
//
//            }
//
//            @Override
//            public void onChanged(Bundle data) {
//                if (data != null && data.getBoolean(TaskConstants.BINDLE_TASK_SUCCESS)) {
//                    Intent intent = new Intent(PurchaseTaskPendingDetailActivity.this, PurchaseBuyBackDetailActivity.class);
//                    intent.putExtra("task_id", mTask.getTask_id());
//                    startActivity(intent);
//                    finish();
//                }
//            }
//        };
//        HCTasksWatched.getInstance().registerDataObserver(mDataObserver);
//    }

    @Override
    protected void onStart() {
        super.onStart();
        if (taskId > 0) {
            ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
            OKHttpManager.getInstance().post(HCHttpRequestParam.getBackTaskDetail(taskId), this, 0);
        }
    }

    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        if (!isFinishing())
            ProgressDialogUtil.closeProgressDialog();

        if (action.equals(HttpConstants.ACTION_GET_BACK_TASK_DETAIL)) {
            responseTask(response);
        } else if (action.equals(HttpConstants.ACTION_BACKTASKAPI_GETCHEJIANDINGREPORT)) {
            responseQueryMaintenance(response);
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
//                mTask = new HCJsonParse().parsePurchaseTask(response.getData());
                mTask = JsonParseUtil.fromJsonObject(response.getData(), PurchaseTaskEntity.class);
                if (mTask != null) {
                    setData(mTask);
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
    public void setData(PurchaseTaskEntity data) {
        tv_title.setText(data.getTitle());
        tv_status.setText(data.getStatus_text());
        tv_seller_name.setText(data.getSeller_name());
        tv_seller_phone.setText(data.getSeller_phone());
        tv_clue_add_user_role.setText(data.getClue_add_user_role() + "[线索人]");
        tv_clue_add_user_name.setText(data.getClue_add_user_name());
        tv_clue_add_user_phone.setText(data.getClue_add_user_phone());
        tv_purchase_task_id.setText(data.getTask_num());
        tv_purchase_vehicle_source.setText(data.getTitle());
        setRemarkUrl(data);
        tv_status.setText(data.getStatus_text());//设置状态文本

        //是内网车源
        rl_source.setVisibility(data.getIs_outside() == 0 ? View.VISIBLE : View.GONE);
        tv_purchase_source_id.setText(data.getVehicle_source_id() + "");

    }

    /**
     * 设置备注链接
     *
     * @param data
     */
    private void setRemarkUrl(final PurchaseTaskEntity data) {
        if (data == null) {
            return;
        }
        if (!TextUtils.isEmpty(data.getUrl())) {
            tv_purchase_comment.setText(Html.fromHtml("<a href=\"\">" + data.getRemark() + "</a>"));
            tv_purchase_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map params = new HashMap();
                    params.put("url", data.getUrl());
                    params.put(HCWebViewActivity.KEY_INTENT_EXTRA_PURCHASE, true);
                    HCActionUtil.launchActivity(PurchaseTaskPendingDetailActivity.this, HCWebViewActivity.class, params);
                }
            });
        } else {
            tv_purchase_comment.setText(data.getRemark());
        }
    }

    /**
     * 查看报告
     */
    @Event(R.id.rb_purchase_query_vinrecord)
    private void rb_purchase_query_vinrecord(View v) {
        if (mTask == null) return;
        if (mTask.getVehicle_source_id() == 0) {
            ToastUtil.showInfo("暂无报告");
            return;
        }

        //查询报告
        ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
        OKHttpManager.getInstance().post(HCHttpRequestParam.getDetailsurl((int) mTask.getVehicle_source_id()), this, 0);
    }

    /**
     * 查看保养记录
     */
    @Event(R.id.rb_purchase_query_maintenance)
    private void rb_purchase_query_maintenance(View v) {
        if (mTask == null) return;
        //存在vin码
        if (!TextUtils.isEmpty(mTask.getVin_code())) {
            ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
            //发起请求查询
            OKHttpManager.getInstance().post(HCHttpRequestParam.getMaintenanceRecords(mTask.getTask_id(), mTask.getVin_code()), this, 0);
            return;
        } else {
            //弹出对话框设置vin
            AlertDialogUtil.createInputVinCodeDialog(this, this);
        }
    }

    /**
     * 转给他人
     */
    @Event(R.id.rb_purchase_transmit)
    private void rb_purchase_transmit(View v) {
        if (mTask == null) return;
        Intent intent = new Intent(this, TransmitPurchaseTaskActivity.class);
        intent.putExtra("taskId", mTask.getTask_id());
        intent.putExtra(TransmitPurchaseTaskActivity.BINDLE_OLD_CRM_USER_ID, mTask.getCrm_user_id());
        intent.putExtra(TransmitPurchaseTaskActivity.BINDLE_OLD_CRM_USER_NAME, mTask.getCrm_user_name());
        intent.putExtra("isAssign", isAssign);
        startActivityForResult(intent, TRANSMIT_TASK);
    }


    /**
     * 回访记录
     */
    @Event(R.id.rb_revisit_record)
    private void revisitRecord(View v) {
        if (mTask == null) return;
        Intent intent = new Intent(this, RevisitRecordActivity.class);
        intent.putExtra("taskId", mTask.getTask_id());
        startActivity(intent);
    }

    /**
     * 报价参考
     */
    @Event(R.id.rb_offer)
    private void offer(View v) {
        if (mTask == null) return;
        Intent intent = new Intent(this, OfferReferActivity.class);
        startActivity(intent);
    }


    /**
     * 处理请求查询保养记录
     */
    private void responseQueryMaintenance(HCHttpResponse response) {
        try {
//            RspVinCodeEntity entity = new HCJsonParse().parseRspVinCodeResult(response.getData());
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
                    AlertDialogUtil.createNotFoundReportDialog(this, vinCode, this);
                    break;
                case 2:
                    //成功拿到报告，去打开url来看看
                    if (!TextUtils.isEmpty(url)) {
                        Map<String, Object> map = new HashMap<>();
                        map.put(HCWebViewActivity.KEY_INTENT_EXTRA_URL, url);
                        map.put(HCWebViewActivity.KEY_INTENT_EXTRA_DOWNLOAD_ENABLE, true);
                        map.put(HCWebViewActivity.KEY_INTENT_EXTRA_DOWNLOAD_URL, pdf_url);
                        map.put(HCWebViewActivity.KEY_INTENT_EXTRA_DOWNLOAD_CHECKER_REPORT_OR_CJD, HCWebViewActivity.KEY_INTENT_EXTRA_DOWNLOAD_CJD);
                        map.put(HCWebViewActivity.KEY_INTENT_EXTRA_ID, mTask.getTask_id() + "");
                        HCActionUtil.launchActivity(this, HCWebViewActivity.class, map);

                        //保存当前查询成功的vinCode
                        mTask.setVin_code(vinCode);
                    }
                    break;
                case 3:
                    //查询中
                    ToastUtil.showInfo(getString(R.string.toast_query_vin, vinCode));
                    break;
                case 4:
                    //不存在 重新修改VIN码提示框
                    AlertDialogUtil.createNotFoundReportDialog(this, vinCode, this);
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
                                //发起请求查询
                                OKHttpManager.getInstance().post(HCHttpRequestParam.getMaintenanceRecords(mTask.getTask_id(), finalVinCode, enginCode), PurchaseTaskPendingDetailActivity.this, 0);
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
     * 任务失败
     */
    @Event(R.id.btn_negative)
    private void failTask(View v) {
        if (mTask == null) return;
        Intent intent = new Intent(this, PurchaseFailedActivity.class);
        intent.putExtra("taskId", mTask.getTask_id());
        startActivityForResult(intent, TASK_FAILED);
    }


    /**
     * 收车成功
     */
    @Event(R.id.btn_positive)
    private void btn_positive(View v) {
        if (mTask == null) return;
        Intent intent = new Intent(this, PurchaseSuccessActivity.class);
        intent.putExtra("purchaseTask", mTask);
        startActivityForResult(intent, TASK_SUCCESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = new Bundle();
            switch (requestCode) {
                case TRANSMIT_TASK://转单
                    bundle.putBoolean(TaskConstants.BINDLE_IS_ASSIGN, isAssign);
                    bundle.putBoolean(TaskConstants.BINDLE_TRANSFER_TO_OTHER, true);
                    HCTasksWatched.getInstance().notifyWatchers(bundle);
                    finish();
                    break;
                case TASK_FAILED://任务失败
                    bundle.putBoolean(TaskConstants.BINDLE_TASK_FAILED, true);
                    HCTasksWatched.getInstance().notifyWatchers(bundle);
                    finish();
                    break;
                case TASK_SUCCESS://收车成功
//                    bundle.putBoolean(TaskConstants.BINDLE_TASK_SUCCESS, true);
//                    HCTasksWatched.getInstance().notifyWatchers(bundle);
//                    Intent intent = new Intent(PurchaseTaskPendingDetailActivity.this, PurchaseBuyBackDetailActivity.class);
//                    intent.putExtra("task_id", mTask.getTask_id());
//                    startActivity(intent);
                    finish();
                    break;
            }
        }
    }


    @Override
    public void onDismiss(Bundle data) {
        if (data != null) {
            String vinCode = data.getString("vinCode");
            boolean determine = data.getBoolean("determine");
            if (!TextUtils.isEmpty(vinCode) && determine) {
                ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
                //发起请求查询
                OKHttpManager.getInstance().post(HCHttpRequestParam.getMaintenanceRecords(mTask.getTask_id(), vinCode), this, 0);
            } else {
                if (!determine)
                    //是否要修改vin对话框
                    AlertDialogUtil.createModifyVinCodeDialog(this, vinCode, false, this);
            }
        }
    }

    @Override
    protected void onDestroy() {
        ProgressDialogUtil.closeProgressDialog();
//        if (mDataObserver != null) {
//            HCTasksWatched.getInstance().UnRegisterDataObserver(mDataObserver);
//            mDataObserver = null;
//        }
        super.onDestroy();
    }
}
