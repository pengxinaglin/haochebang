package com.haoche51.checker.activity.purchase;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.HCWebViewActivity;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.PurchaseTaskEntity;
import com.haoche51.checker.listener.HCTasksWatched;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.AlertDialogUtil;
import com.haoche51.checker.util.ControlDisplayUtil;
import com.haoche51.checker.util.HCActionUtil;
import com.haoche51.checker.util.HCArithUtil;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 收车回购中列表对应的详情页面
 *
 * @author wfx@2016/6/8
 */
public class PurchaseBuyBackDetailActivity extends CommonTitleBaseActivity {
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

    @ViewInject(R.id.tv_purchase_task_time)
    private TextView tv_purchase_task_time;

    @ViewInject(R.id.tv_purchase_task_address)
    private TextView tv_purchase_task_address;

    @ViewInject(R.id.tv_purchase_vehicle_source)
    private TextView tv_purchase_vehicle_source;

    @ViewInject(R.id.tv_purchase_comment)
    private TextView tv_purchase_comment;

    @ViewInject(R.id.tv_back_price)
    private TextView tv_back_price;

    @ViewInject(R.id.tv_transfer_free)
    private TextView tv_transfer_free;

    @ViewInject(R.id.tv_fail_reason)
    private TextView tv_fail_reason;

    @ViewInject(R.id.tv_hope_price)
    private TextView tv_hope_price;

    @ViewInject(R.id.tv_our_price)
    private TextView tv_our_price;

    @ViewInject(R.id.tv_peer_price)
    private TextView tv_peer_price;

    @ViewInject(R.id.tv_introduce_fee)
    private TextView tv_introduce_fee;

    @ViewInject(R.id.tv_sell_cycle)
    private TextView tv_sell_cycle;

    @ViewInject(R.id.ll_failed_info)
    private LinearLayout ll_failed_info;

    @ViewInject(R.id.ll_break_contract)
    private LinearLayout ll_break_contract;

    @ViewInject(R.id.tv_back_money)
    private TextView tv_back_money;

    @ViewInject(R.id.tv_real_back_money)
    private TextView tv_real_back_money;

    @ViewInject(R.id.tv_break_remark)
    private TextView tv_break_remark;


    @ViewInject(R.id.rl_source)
    private RelativeLayout rl_source;

    @ViewInject(R.id.tv_purchase_source_id)
    private TextView tv_purchase_source_id;

    @ViewInject(R.id.tv_pay_progress)
    private TextView tv_pay_progress;

    @ViewInject(R.id.pb_pay_progress)
    private ProgressBar pb_pay_progress;

    @ViewInject(R.id.iv_pay_stat)
    private ImageView iv_pay_stat;

    @ViewInject(R.id.tv_pay_stat)
    private TextView tv_pay_stat;

    @ViewInject(R.id.tv_reject_reason)
    private TextView tv_reject_reason;


    @ViewInject(R.id.ll_operate_btn)
    private LinearLayout ll_operate_btn;

    @ViewInject(R.id.ll_purchase_progress)
    private LinearLayout ll_purchase_progress;

    @ViewInject(R.id.ll_purchase_amt)
    private LinearLayout ll_purchase_amt;


    /**
     * 申请付款
     */
    @ViewInject(R.id.btn_positive)
    private Button btn_positive;

    private int taskId;//根据任务Id去请求任务详情
    private PurchaseTaskEntity mTask;//根据任务Id请求的任务详情


    @Override
    public View getHCContentView() {
        return View.inflate(this, R.layout.activity_purchase_buyback_detail, null);
    }

    @Override
    public void initContentView(Bundle saveInstanceState) {
        //获取查询的任务id
        taskId = getIntent().getIntExtra("task_id", 0);
        x.view().inject(this);
        //起初先隐藏所有按钮
        btn_positive.setVisibility(View.GONE);
    }

    @Override
    public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
        mTitle.setText(getString(R.string.hc_purchasetask_detail_title));
    }

    @Override
    protected void onStart() {
        if (taskId > 0) {
            ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
            OKHttpManager.getInstance().post(HCHttpRequestParam.getBackTaskDetail(taskId), this, 0);
        }
        super.onStart();
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
        tv_purchase_task_time.setText(data.getAppoint_time());
        tv_purchase_task_address.setText(data.getAppoint_address());
        tv_purchase_vehicle_source.setText(data.getTitle());
        setRemarkUrl(data);
        //收购价
        double purchaseAmt = HCArithUtil.div(Double.valueOf(data.getBack_price()), 10000);
        tv_back_price.setText(purchaseAmt + "万元");
        tv_transfer_free.setText(data.getTransfer_free() + "元");
        tv_introduce_fee.setText(data.getIntroduce_free() + "元");
        tv_fail_reason.setText(data.getFail_reason());
        tv_hope_price.setText(data.getHope_price() + "元");
        tv_our_price.setText(data.getOur_price() + "元");
        tv_peer_price.setText(data.getPeer_price() + "元");
        tv_sell_cycle.setText(data.getSell_cycle());
        tv_back_money.setText(String.valueOf(data.getNeed_back_amount()).concat("元"));
        tv_real_back_money.setText(String.valueOf(data.getReal_back_amount()).concat("元"));
        tv_break_remark.setText(data.getBreak_remark());


        BigDecimal unitWan = new BigDecimal(10000);
        int totalBackPrice = Integer.parseInt(data.getTotal_back_price());
        int hasPay = Integer.parseInt(data.getHas_pay());
        if (totalBackPrice == 0 || totalBackPrice == hasPay) {
            pb_pay_progress.setProgress(10000);
        } else if (hasPay == 0) {
            pb_pay_progress.setProgress(0);
        } else {
            int percent = new BigDecimal(data.getHas_pay()).divide(new BigDecimal(data.getTotal_back_price()), 10, BigDecimal.ROUND_UP).multiply(unitWan).intValue();
            pb_pay_progress.setProgress(percent);
        }
        BigDecimal payedAmt = new BigDecimal(data.getHas_pay()).divide(unitWan);
        BigDecimal totalPayAmt = new BigDecimal(data.getTotal_back_price()).divide(unitWan);
        tv_pay_progress.setText("付款进度：总共".concat(String.valueOf(totalPayAmt)).concat("万（已付").concat(String.valueOf(payedAmt)).concat("万）"));
        tv_pay_stat.setText(data.getLast_pay_status_text());
        tv_reject_reason.setText(data.getLast_pay_fail_reason());
        ll_purchase_progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PurchaseBuyBackDetailActivity.this, PaymentRecordActivity.class);
                intent.putExtra("taskId", taskId);
                startActivity(intent);
            }
        });

        ControlDisplayUtil.getInstance().changeDisplayStyle(data.getLast_pay_status(), iv_pay_stat, tv_pay_stat, tv_reject_reason);
        //是内网车源
        rl_source.setVisibility(data.getIs_outside() == 0 ? View.VISIBLE : View.GONE);
        tv_purchase_source_id.setText(data.getVehicle_source_id() + "");

        //起初先隐藏所有按钮
        ll_operate_btn.setVisibility(View.GONE);
        ll_break_contract.setVisibility(View.GONE);
        ll_failed_info.setVisibility(View.GONE);

        switch (mTask.getStatus()) {
            case TaskConstants.TASK_STATUS_CHECKED://已审核
            case TaskConstants.TASK_STATUS_HAS_STOCK://已入库
            case TaskConstants.TASK_STATUS_PURCHASE_COMPLETE://收购完成
                ll_operate_btn.setVisibility(View.VISIBLE);
                if (data.getLast_pay_status() != TaskConstants.APPLY_PAY_STATUS_APLYING &&
                        (data.getPay_status() == TaskConstants.STOCK_PAY_STATUS_UNPAY || data.getPay_status() == TaskConstants.STOCK_PAY_STATUS_PART)) {
                    btn_positive.setVisibility(View.VISIBLE);
                    btn_positive.setText(getString(R.string.apply_payment));
                }
                break;
            case TaskConstants.TASK_STATUS_FAIL://任务失败
                btn_positive.setVisibility(View.VISIBLE);
                btn_positive.setText(getString(R.string.hc_purchasetask_revive));
                ll_failed_info.setVisibility(View.VISIBLE);
                ll_purchase_progress.setVisibility(View.GONE);
                ll_purchase_amt.setVisibility(View.GONE);
                break;
            case TaskConstants.TASK_STATUS_BREAK_CONTRACT_TO_CONFIRM://毁约待确认
            case TaskConstants.TASK_STATUS_BREAK_CONTRACT_PASS://毁约通过
                ll_break_contract.setVisibility(View.VISIBLE);
                break;
        }

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
                    HCActionUtil.launchActivity(PurchaseBuyBackDetailActivity.this, HCWebViewActivity.class, params);
                }
            });
        } else {
            tv_purchase_comment.setText(data.getRemark());
        }
    }


    /**
     * 毁约退款
     */
    @Event(R.id.rb_break_contract)
    private void breakContract(View v) {
        if (mTask == null) return;
        Intent intent = new Intent(this, BreakContractActivity.class);
        intent.putExtra("purchaseTask", mTask);
        startActivity(intent);
    }

    /**
     * 上架车源
     */
    @Event(R.id.rb_car_online)
    private void carOnline(View v) {
        if (mTask == null) return;
        //上架车源
        if (TextUtils.isEmpty(mTask.getLaunch_check_msg())) {
            Intent intent = new Intent(this, VehicleOnlineActivity.class);
            intent.putExtra("purchaseTask", mTask);
            startActivity(intent);
        } else {
            ToastUtil.showInfo(mTask.getLaunch_check_msg());
        }
    }


    /**
     * 付款记录
     */
    @Event(R.id.rb_pay_records)
    private void payRecords(View v) {
        if (mTask == null) return;
        Intent intent = new Intent(this, PaymentRecordActivity.class);
        intent.putExtra("taskId", mTask.getTask_id());
        startActivity(intent);
    }


    /**
     * 申请付款、复活任务
     */
    @Event(R.id.btn_positive)
    private void btn_positive(View v) {
        if (mTask == null) return;
        //失败任务 点击为复活任务
        if (mTask.getStatus() == TaskConstants.TASK_STATUS_FAIL) {

            AlertDialogUtil.showStandardTitleMessageDialog(this, "复活任务", "复活后的任务在待跟进列表中", "取消", "确定", new AlertDialogUtil.OnDismissListener() {
                @Override
                public void onDismiss(Bundle data) {
                    ProgressDialogUtil.showProgressDialog(PurchaseBuyBackDetailActivity.this, getString(R.string.later));
                    OKHttpManager.getInstance().post(HCHttpRequestParam.backLifeTask(taskId), PurchaseBuyBackDetailActivity.this, 0);
                }
            });
        }
        //申请付款
        else {
            Intent intent = new Intent(this, ApplyPaymentActivity.class);
            intent.putExtra("purchaseTask", mTask);
            startActivity(intent);
        }
    }

    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        ProgressDialogUtil.closeProgressDialog();
        if (action.equals(HttpConstants.ACTION_GET_BACK_TASK_DETAIL)) {
            responseTask(response);
        } else if (action.equals(HttpConstants.ACTION_BACKTASK_BACKLIFE)) {
            responseBackLife(response);
        }
    }

    /**
     * 处理请求复活任务
     */
    private void responseBackLife(HCHttpResponse response) {
        switch (response.getErrno()) {
            case 0:
                ToastUtil.showInfo("操作成功");
                //更新列表状态
                Bundle bundle = new Bundle();
                bundle.putBoolean(TaskConstants.BINDLE_BACK_LIFE, true);
                bundle.putParcelable(TaskConstants.BINDLE_NEW_TASK, mTask);
                HCTasksWatched.getInstance().notifyWatchers(bundle);
                finish();
                break;
            default:
                ToastUtil.showInfo(response.getErrmsg());
                break;
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

    @Override
    protected void onDestroy() {
        ProgressDialogUtil.closeProgressDialog();
        super.onDestroy();
    }

}