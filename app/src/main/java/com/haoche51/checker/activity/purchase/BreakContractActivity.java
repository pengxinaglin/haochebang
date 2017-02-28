package com.haoche51.checker.activity.purchase;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.haoche51.checker.Checker;
import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.PurchaseTaskEntity;
import com.haoche51.checker.listener.HCTasksWatched;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.AlertDialogUtil;
import com.haoche51.checker.util.HCDialogUtil;
import com.haoche51.checker.util.ToastUtil;
import com.haoche51.settlement.cashiers.SettlementIntent;
import com.mobsandgeeks.saripaar.annotation.Required;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 收车毁约
 */
public class BreakContractActivity extends CommonTitleBaseActivity {
    /**
     * 应退金额
     */
    @ViewInject(R.id.tv_refundable)
    @Required(order = 1, message = "应退金额不能为空")
    private TextView tv_refundable;

    /**
     * 实退金额
     */
    @ViewInject(R.id.ed_real_back)
    @Required(order = 2, message = "实退金额不能为空")
    private EditText ed_real_back;

    /**
     * 退款备注
     */
    @Required(order = 3, message = "退款备注不能为空")
    private EditText ed_transfer_remark;

    private PurchaseTaskEntity purchaseTaskEntity;
    private AlertDialog alertDialog;

    @Override
    public View getHCContentView() {
        View contentView = View.inflate(this, R.layout.activity_break_contract, null);
        TextView tv_transfer_label = (TextView) contentView.findViewById(R.id.tv_transfer_label);
        tv_transfer_label.setText(getString(R.string.hc_break_remark));
        ed_transfer_remark = (EditText) contentView.findViewById(R.id.ed_transfer_remark);
        ed_transfer_remark.setHint(getString(R.string.hc_break_contract_content));
        return contentView;
    }

    @Override
    public void initContentView(Bundle saveInstanceState) {
        x.view().inject(this);
        purchaseTaskEntity = getIntent().getParcelableExtra("purchaseTask");
        if (purchaseTaskEntity == null) {
            return;
        }
        tv_refundable.setText(Integer.valueOf(purchaseTaskEntity.getNeed_back_amount()).toString());
    }

    @Override
    public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
        mTitle.setText(getString(R.string.hc_break_contract));
    }

    /**
     * 确认转账
     */
    @Event(R.id.btn_commit)
    private void commit(View v) {
        validator.validate();
    }

    /**
     * 去结算
     */
    private void settleAccount() {
        //调用结算
        SettlementIntent settlementIntent = new SettlementIntent(BreakContractActivity.this);
        Checker checker = GlobalData.userDataHelper.getChecker();
        settlementIntent.setAppToken(checker.getApp_token());    //必传，网络请求的appToken
        settlementIntent.setCrmUserId(String.valueOf(checker.getId()));      // 必传，crm_user_id
        settlementIntent.setCrmUserName(checker.getName());                      //必传，crm_user_name
        settlementIntent.setCustomerName(purchaseTaskEntity.getSeller_name());      //必传 客户电话号码
        settlementIntent.setCustomerPhone(purchaseTaskEntity.getSeller_phone());             //必传 客户电话号码
        settlementIntent.setPrice(String.valueOf(purchaseTaskEntity.getReal_back_amount()));  // 应收金额，单位元
        settlementIntent.setTaskId(String.valueOf(purchaseTaskEntity.getTask_id()));    // 非必传 业务订单号，没有不传
        settlementIntent.setTaskType(purchaseTaskEntity.getTrans_type());          //任务类型，1c2c交易 2回购 3金融
        settlementIntent.setFromBusiness(true);   //是否从业务方调用
        settlementIntent.setCashEnable(true);     //是否可以使用现金收款，不传默认false，不使用现金
        settlementIntent.setComment(purchaseTaskEntity.getBreak_remark()); //设置备注
        startActivityForResult(settlementIntent, TaskConstants.REQUEST_BREAK_CONTRACT);
    }

    /**
     * 校验成功
     */
    @Override
    public void onValidationSucceeded() {
        super.onValidationSucceeded();
        //去结算
        goSettleAccount();
    }


    /**
     * 提交毁约退款信息
     */
    private void commitBreakContract() {
        if (purchaseTaskEntity == null) {
            return;
        }
        //显示对话框
        HCDialogUtil.showProgressDialog(this);
        OKHttpManager.getInstance().post(HCHttpRequestParam.breakContract(purchaseTaskEntity), this, 0);
    }


    /**
     * 去结算
     */
    private void goSettleAccount() {
        if (purchaseTaskEntity == null) {
            return;
        }

        //实退金额
        if (!TextUtils.isEmpty(ed_real_back.getText())) {
            try {
                purchaseTaskEntity.setReal_back_amount(Integer.parseInt(ed_real_back.getText().toString()));
            } catch (Exception e) {
                purchaseTaskEntity.setReal_back_amount(0);
            }
        }

        //退款转账备注
        if (!TextUtils.isEmpty(ed_transfer_remark.getText())) {
            purchaseTaskEntity.setBreak_remark(ed_transfer_remark.getText().toString().trim());
        }

        //再次确认毁约退款金额
        AlertDialogUtil.showNeedBackAmtDialog(this, purchaseTaskEntity.getNeed_back_amount(), purchaseTaskEntity.getReal_back_amount(), getString(R.string.again_input), getString(R.string.confirm_no_error), new AlertDialogUtil.OnDismissListener() {
            @Override
            public void onDismiss(Bundle data) {
                settleAccount();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TaskConstants.REQUEST_BREAK_CONTRACT && resultCode == RESULT_OK) {
            //提交毁约退款
            commitBreakContract();
        }
    }

    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        HCDialogUtil.dismissProgressDialog();
        if (response == null) {
            ToastUtil.showInfo("响应结果为空！");
            return;
        }

        //提交毁约退款
        if (HttpConstants.ACTION_BREAK_APPOINT.equals(action)) {
            switch (response.getErrno()) {
                case 0:
                    ToastUtil.showInfo("提交毁约退款成功");
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(TaskConstants.BINDLE_BREAK_CONTRACT, true);
                    HCTasksWatched.getInstance().notifyWatchers(bundle);
                    finish();
                    break;
                default:
                    ToastUtil.showInfo("提交毁约退款失败：" + response.getErrmsg());
                    break;
            }
        }
    }


    /**
     * 关闭对话框
     */
    public void disAlertDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        //关闭对话框
        HCDialogUtil.dismissProgressDialog();
        disAlertDialog();
        super.onDestroy();
    }

}
