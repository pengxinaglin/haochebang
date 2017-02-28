package com.haoche51.checker.activity.purchase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.PaymentRecordEntity;
import com.haoche51.checker.entity.PurchaseTaskEntity;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.HCDialogUtil;
import com.haoche51.checker.util.ToastUtil;
import com.haoche51.checker.widget.ClearableEditText;
import com.intsig.ccrengine.CCREngine;
import com.intsig.ccrengine.ISCardScanActivity;
import com.mobsandgeeks.saripaar.annotation.Required;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.math.BigDecimal;

/**
 * 申请付款
 *
 * @author wfx@2016/6/12
 */
public class ApplyPaymentActivity extends CommonTitleBaseActivity implements View.OnClickListener {
    private static final String APP_KEY = "9PAD77V37Pg06Y3UCDXabeL0";//替换您申请的合合信息授权提供的APP_KEY;
    private static final int REQ_CODE_CAPTURE = 100;
    private static final int REQ_CODE_RESULT = 101;
    private static final String TAG = "ApplyPaymentActivity";
    /**
     * 申请付款
     */
    @ViewInject(R.id.btn_positive)
    private Button btn_positive;

    /**
     * 户主姓名
     */
    @ViewInject(R.id.ed_household_name)
    @Required(order = 1, message = "户主姓名不能为空")
    private EditText ed_household_name;

    /**
     * 收款账号
     */
    @ViewInject(R.id.ed_receipt_account)
    @Required(order = 2, message = "收款账号不能为空")
    private ClearableEditText ed_receipt_account;

    /**
     * 开户银行
     */
    @ViewInject(R.id.ed_deposit_bank)
    @Required(order = 3, message = "开户银行不能为空")
    private EditText ed_deposit_bank;

    /**
     * 付款事由
     */
    @ViewInject(R.id.ed_pay_reason)
    @Required(order = 4, message = "付款事由不能为空")
    private EditText ed_pay_reason;


    /**
     * 付款金额
     */
    @ViewInject(R.id.ed_pay_amt)
    @Required(order = 5, message = "付款金额不能为空")
    private EditText ed_pay_amt;

    /**
     * 付款进度
     */
    @ViewInject(R.id.pb_pay_progress)
    private ProgressBar pb_pay_progress;

    /**
     * 支付状态描述
     */
    @ViewInject(R.id.tv_payed_desc)
    private TextView tv_payed_desc;

    @ViewInject(R.id.iv_camera)
    private ImageView iv_camera;

    private PurchaseTaskEntity purchaseTask;


    /**
     * 最高可支付
     */
    private String maxPayAmt;
    @Override
    public View getHCContentView() {
        return View.inflate(this, R.layout.activity_apply_payment, null);
    }

    @Override
    public void initContentView(Bundle saveInstanceState) {
        x.view().inject(this);
        //收款账号点击事件
        iv_camera.setOnClickListener(this);
        btn_positive.setOnClickListener(this);
        purchaseTask = getIntent().getParcelableExtra("purchaseTask");
        ed_receipt_account.setOnClearBtnChangeListener(new ClearableEditText.OnClearBtnChangeListener() {
            @Override
            public void onClearBtnChange(boolean isClearVisible) {

                iv_camera.setVisibility(isClearVisible?View.GONE:View.VISIBLE);
            }
        });
        //展示支付进度
        showPayProgress();
    }


    @Override
    public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
        mTitle.setText(getString(R.string.apply_payment));
        btn_positive.setText(getString(R.string.apply_payment));
    }

    /**
     * 展示支付进度
     */
    private void showPayProgress() {
        if (purchaseTask == null) {
            return;
        }

        BigDecimal unitWan = new BigDecimal(10000);
        int totalBackPrice = Integer.parseInt(purchaseTask.getTotal_back_price());
        int hasPay = Integer.parseInt(purchaseTask.getHas_pay());
        if (totalBackPrice == 0 || totalBackPrice == hasPay) {
            pb_pay_progress.setProgress(10000);
        } else if (hasPay == 0) {
            pb_pay_progress.setProgress(0);
        } else {
            int percent = new BigDecimal(purchaseTask.getHas_pay()).divide(new BigDecimal(purchaseTask.getTotal_back_price()), 10, BigDecimal.ROUND_UP).multiply(unitWan).intValue();
            pb_pay_progress.setProgress(percent);
        }

        String payedAmt = new BigDecimal(purchaseTask.getHas_pay()).divide(unitWan).toString();
        String totalPayAmt = new BigDecimal(purchaseTask.getTotal_back_price()).divide(unitWan).toString();

        maxPayAmt = new BigDecimal(purchaseTask.getTotal_back_price()).subtract(new BigDecimal(purchaseTask.getHas_pay())).divide(unitWan).toString();

        tv_payed_desc.setText("付款进度：总共" + totalPayAmt + "万（已付" + payedAmt + "万）");

        //点击付款金额时弹出框
        ed_pay_amt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    onValidateFailed(ed_pay_amt, "本次最高可支付" + maxPayAmt + "万");
                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_camera://收款账号
                openCamera();
                break;
            case R.id.btn_positive://提交按钮
                validator.validate();
                break;
        }
    }

    /**
     * 打开相机
     */
    private void openCamera() {
        //通过Intent调用SDK中的相机拍摄模块ISCardScanActivity进行识别
        Intent intent = new Intent(this, ISCardScanActivity.class);
        //指定SDK相机模块ISCardScanActivity四边框角线条,检测到银行卡图片后的颜色
        intent.putExtra(ISCardScanActivity.EXTRA_KEY_COLOR_MATCH, 0xffff0000);
        //指定SDK相机模块ISCardScanActivity四边框角线条颜色，正常显示颜色
        intent.putExtra(ISCardScanActivity.EXTRA_KEY_COLOR_NORMAL, 0xff00ff00);
        //指定SDK相机模块ISCardScanActivity提示字符串
        intent.putExtra(ISCardScanActivity.EXTRA_KEY_TIPS, "请将银行卡放在框内识别");
        //合合信息授权提供的APP_KEY
        intent.putExtra(ISCardScanActivity.EXTRA_KEY_APP_KEY, APP_KEY);
        //指定SDK相机模块是否返回银行卡卡号截图
        intent.putExtra(ISCardScanActivity.EXTRA_KEY_GET_NUMBER_IMG, true);
        //指定SDK相机模块银行卡切边图路径
        intent.putExtra(ISCardScanActivity.EXTRA_KEY_GET_TRIMED_IMG, "/sdcard/trimedcard.jpg");
        //指定SDK相机模块银行卡原图路径
        intent.putExtra(ISCardScanActivity.EXTRA_KEY_GET_ORIGINAL_IMG, "/sdcard/origianlcard.jpg");
        startActivityForResult(intent, REQ_CODE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_CODE_CAPTURE) {
                //获取银行卡识别ResultData识别结果
                CCREngine.ResultData result = (CCREngine.ResultData) data.getSerializableExtra(ISCardScanActivity.EXTRA_KEY_RESULT);
                //在RecogResultActivity中显示银行卡识别结果
                Intent intent = new Intent(this, RecogResultActivity.class);
                Bitmap bmp = (Bitmap) data.getParcelableExtra(ISCardScanActivity.EXTRA_KEY_GET_NUMBER_IMG);
                intent.putExtra(ISCardScanActivity.EXTRA_KEY_GET_NUMBER_IMG, bmp);
                intent.putExtra(ISCardScanActivity.EXTRA_KEY_RESULT, result);
                startActivityForResult(intent, REQ_CODE_RESULT);
            } else if (requestCode == REQ_CODE_RESULT) {
                String bank_code = data.getStringExtra(TaskConstants.BINDLE_BANK_CODE);
                ed_receipt_account.setText(bank_code);
            }

        } else if (resultCode == RESULT_CANCELED && requestCode == REQ_CODE_CAPTURE) {
            //识别失败或取消
            Log.d(TAG, "识别失败或取消，请参考返回错误码说明");
            if (data != null) {
                /**
                 * 101 包名错误, 授权APP_KEY与绑定的APP包名不匹配；
                 * 102 appKey错误，传递的APP_KEY填写错误；
                 * 103 超过时间限制，授权的APP_KEY超出使用时间限制；
                 * 104 达到设备上限，授权的APP_KEY使用设备数量达到限制；
                 * 201 签名错误，授权的APP_KEY与绑定的APP签名不匹配；
                 * 202 其他错误，其他未知错误，比如初始化有问题；
                 * 203 服务器错误，第一次联网验证时，因服务器问题，没有验证通过；
                 * 204 网络错误，第一次联网验证时，没有网络连接，导致没有验证通过；
                 * 205 包名/签名错误，授权的APP_KEY与绑定的APP包名和签名都不匹配；
                 */
                int error_code = 0;// data.getIntExtra(ISCardScanActivity.EXTRA_KEY_RESULT_ERROR_CODE, 0);
                Toast.makeText(this, "Error >>> " + error_code, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 校验成功
     */
    @Override
    public void onValidationSucceeded() {
        super.onValidationSucceeded();
        try {
            //提交申请信息
            commitApplyInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 提交申请信息
     */
    private void commitApplyInfo() {
        if (purchaseTask == null) {
            return;
        }
        PaymentRecordEntity paymentRecordEntity = new PaymentRecordEntity();
        paymentRecordEntity.setTask_id(purchaseTask.getTask_id());

        //户主姓名
        String houseHoldName = ed_household_name.getText().toString().trim();
        if (TextUtils.isEmpty(houseHoldName)) {
            onValidateFailed(ed_household_name, "户主姓名不能为空");
            return;
        }
        paymentRecordEntity.setAccount_user(houseHoldName);

        //收款账号
        paymentRecordEntity.setAccount_num(ed_receipt_account.getText().toString().replace(" ", ""));

        //开户银行
        String depositBank = ed_deposit_bank.getText().toString().trim();
        if (TextUtils.isEmpty(depositBank)) {
            onValidateFailed(ed_deposit_bank, "开户银行不能为空");
            return;
        }
        paymentRecordEntity.setAccount_bank(depositBank);

        //付款事由
        String payReason = ed_pay_reason.getText().toString().trim();
        if (TextUtils.isEmpty(payReason)) {
            onValidateFailed(ed_pay_reason, "付款事由不能为空");
            return;
        }
        paymentRecordEntity.setPrice_type(payReason);

        //付款金额
        BigDecimal maxPay = new BigDecimal(purchaseTask.getTotal_back_price()).subtract(new BigDecimal(purchaseTask.getHas_pay()));
        BigDecimal unitWan = new BigDecimal(10000);
        int payAmt = new BigDecimal(ed_pay_amt.getText().toString()).multiply(unitWan).intValue();
        if (new BigDecimal(payAmt).compareTo(maxPay) > 0) {
            onValidateFailed(ed_pay_amt, "本次最高可支付" + maxPayAmt + "万");
            return;
        }
        paymentRecordEntity.setPrice(payAmt);

        //显示对话框
        HCDialogUtil.showProgressDialog(this);
        OKHttpManager.getInstance().post(HCHttpRequestParam.applyInfo(paymentRecordEntity), this, 0);
    }

    /**
     * 显示校验失败消息
     *
     * @param failedView
     * @param message
     */
    private void onValidateFailed(EditText failedView, String message) {
        failedView.requestFocus();
        failedView.setError(message);
    }


    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        HCDialogUtil.dismissProgressDialog();
        if (response == null) {
            ToastUtil.showInfo("响应结果为空！");
            return;
        }

        //申请付款
        if (HttpConstants.ACTION_APPLY_PAY.equals(action)) {
            switch (response.getErrno()) {
                case 0:
                    ToastUtil.showInfo("申请付款成功");
                    finish();
                    break;
                default:
                    ToastUtil.showInfo("申请付款失败：" + response.getErrmsg());
                    break;
            }
        }
    }


    @Override
    protected void onDestroy() {
        //关闭对话框
        HCDialogUtil.dismissProgressDialog();
        super.onDestroy();
    }


}
