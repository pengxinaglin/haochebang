package com.haoche51.checker.activity.purchase;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.RevisitRecordEntity;
import com.haoche51.checker.listener.HCTasksWatched;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.math.BigDecimal;

/**
 * 添加回访记录
 * Created by wufx on 2016/7/19.
 */
public class AddRevisitRecordActivity extends CommonTitleBaseActivity {

    /**
     * 下次回访时间
     */
    @ViewInject(R.id.tv_next_revisit_time)
    private TextView tv_next_revisit_time;

    /**
     * 车主卖车原因
     */
    @ViewInject(R.id.ed_sell_reason)
    private EditText ed_sell_reason;

    /**
     * 预期卖车时间
     */
    @ViewInject(R.id.ed_sell_time)
    private EditText ed_sell_time;

    /**
     * 卖车决定人
     */
    @ViewInject(R.id.ed_sell_decider)
    private EditText ed_sell_decider;

    /**
     * 车主预期售价
     */
    @ViewInject(R.id.ed_heart_price)
    private EditText ed_heart_price;

    /**
     * 同行最高报价
     */
    @ViewInject(R.id.ed_max_offer_price)
    private EditText ed_max_offer_price;

    /**
     * 其他
     */
    @ViewInject(R.id.ed_other)
    private EditText ed_other;

    @ViewInject(R.id.btn_positive)
    private Button btn_positive;

    private int taskId;
    private int mRevisitDay = 0;

    @Override
    public View getHCContentView() {
        return View.inflate(this, R.layout.activity_add_revisit_record, null);
    }

    @Override
    public void initContentView(Bundle saveInstanceState) {
        x.view().inject(this);
    }

    @Override
    public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
        mTitle.setText(getString(R.string.next_revisit_time));
        btn_positive.setText(getString(R.string.hc_common_save));
        taskId = getIntent().getIntExtra("taskId", 0);
    }

    @Event(R.id.tv_next_revisit_time)
    private void selectRevisitTime(View view) {
        showRevisitDialog();
    }

    private void showRevisitDialog() {
        final Dialog dialog = new Dialog(this, R.style.shareDialog);
        dialog.setCancelable(true);
        //当触摸对话框外部时，关闭对话框
        dialog.setCanceledOnTouchOutside(true);
        View contentView = View.inflate(this, R.layout.dialog_select_revisit_date, null);
        final RadioGroup rg_revisit_date = (RadioGroup) contentView.findViewById(R.id.rg_revisit_date);
        switch (mRevisitDay) {
            case 1:
                rg_revisit_date.check(R.id.rb_one_day);
                break;
            case 2:
                rg_revisit_date.check(R.id.rb_two_day);
                break;
            case 3:
                rg_revisit_date.check(R.id.rb_three_day);
                break;
            case 5:
                rg_revisit_date.check(R.id.rb_five_day);
                break;
            default:
                break;
        }
        Button btnCancel = (Button) contentView.findViewById(R.id.btn_layout_vehicle_sub_cancle);
        btnCancel.setText(getResources().getString(R.string.soft_update_cancel));
        Button btnCommit = (Button) contentView.findViewById(R.id.btn_layout_vehicle_sub_save);
        btnCommit.setText(getResources().getString(R.string.button_ok));
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (rg_revisit_date.getCheckedRadioButtonId()) {
                    case -1:
                        ToastUtil.showInfo("请选择回访时间");
                        return;
                    case R.id.rb_one_day:
                        mRevisitDay = 1;
                        tv_next_revisit_time.setText("1天后");
                        break;
                    case R.id.rb_two_day:
                        mRevisitDay = 2;
                        tv_next_revisit_time.setText("2天后");
                        break;
                    case R.id.rb_three_day:
                        mRevisitDay = 3;
                        tv_next_revisit_time.setText("3天后");
                        break;
                    case R.id.rb_five_day:
                        mRevisitDay = 5;
                        tv_next_revisit_time.setText("5天后");
                        break;
                }
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        dialog.setContentView(contentView);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = (int) (this.getWindowManager().getDefaultDisplay().getWidth() * 0.85);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
    }


    /**
     * 提交回访记录
     */
    @Event(R.id.btn_positive)
    private void commit(View v) {
        try{
            commitInfo();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 提交信息
     */
    private void commitInfo(){
        if (TextUtils.isEmpty(tv_next_revisit_time.getText().toString())) {
            onValidateFailed(tv_next_revisit_time, "下次回访时间不能为空");
            return;
        }

        if (!TextUtils.isEmpty(ed_heart_price.getText())) {
            BigDecimal heartPrice = new BigDecimal(ed_heart_price.getText().toString());
            if (heartPrice.compareTo(new BigDecimal(0)) == 0) {
                ToastUtil.showInfo("车主预期售价不能为0");
                return;
            }
        }

        if (!TextUtils.isEmpty(ed_max_offer_price.getText())) {
            BigDecimal maxOfferPrice = new BigDecimal(ed_max_offer_price.getText().toString());
            if (maxOfferPrice.compareTo(new BigDecimal(0)) == 0) {
                ToastUtil.showInfo("同行最高报价不能为0");
                return;
            }
        }

        if (!checkIfOneFilled()) {
            ToastUtil.showInfo("除了下次回访时间外，至少再填写一项才可以提交");
            return;
        }

        RevisitRecordEntity revisitRecordEntity = new RevisitRecordEntity();
        revisitRecordEntity.setTask_id(taskId);
        revisitRecordEntity.setNext_visit(mRevisitDay);

        if (!TextUtils.isEmpty(ed_sell_reason.getText())) {
            revisitRecordEntity.setReason(ed_sell_reason.getText().toString().trim());
        }

        if (!TextUtils.isEmpty(ed_sell_time.getText())) {
            revisitRecordEntity.setSold_time(ed_sell_time.getText().toString().trim());
        }

        if (!TextUtils.isEmpty(ed_sell_decider.getText())) {
            revisitRecordEntity.setDecide_user(ed_sell_decider.getText().toString().trim());
        }

        //预期售价 单位“元”
        BigDecimal unitWan = new BigDecimal(10000);
        if (!TextUtils.isEmpty(ed_heart_price.getText())) {
            int heartPrice = new BigDecimal(ed_heart_price.getText().toString()).multiply(unitWan).intValue();
            revisitRecordEntity.setExpect_price(heartPrice);
        }

        //同行最高报价 单位“元”
        if (!TextUtils.isEmpty(ed_max_offer_price.getText())) {
            int maxPrice = new BigDecimal(ed_max_offer_price.getText().toString()).multiply(unitWan).intValue();
            revisitRecordEntity.setPeer_price(maxPrice);
        }

        if (!TextUtils.isEmpty(ed_other.getText())) {
            revisitRecordEntity.setOther(ed_other.getText().toString().trim());
        }

        ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
        OKHttpManager.getInstance().post(HCHttpRequestParam.addRevisitRecord(revisitRecordEntity), this, 0);
    }


    /**
     * 检查是否有一项已填写
     * 满足的话：返回true
     * 不满足：返回false
     *
     * @return
     */
    private boolean checkIfOneFilled() {
        if (!TextUtils.isEmpty(ed_sell_reason.getText()) && !TextUtils.isEmpty(ed_sell_reason.getText().toString().trim())) {
            return true;
        }

        if (!TextUtils.isEmpty(ed_sell_time.getText()) && !TextUtils.isEmpty(ed_sell_time.getText().toString().trim())) {
            return true;
        }

        if (!TextUtils.isEmpty(ed_sell_decider.getText()) && !TextUtils.isEmpty(ed_sell_decider.getText().toString().trim())) {
            return true;
        }

        if (!TextUtils.isEmpty(ed_heart_price.getText())) {
            BigDecimal heartPrice = new BigDecimal(ed_heart_price.getText().toString());
            if (heartPrice.compareTo(new BigDecimal(0)) > 0) {
                return true;
            }
        }

        if (!TextUtils.isEmpty(ed_max_offer_price.getText())) {
            BigDecimal maxOfferPrice = new BigDecimal(ed_max_offer_price.getText().toString());
            if (maxOfferPrice.compareTo(new BigDecimal(0)) > 0) {
                return true;
            }
        }


        return !TextUtils.isEmpty(ed_other.getText()) && !TextUtils.isEmpty(ed_other.getText().toString().trim());

    }


    /**
     * 显示校验失败消息
     *
     * @param failedView
     * @param message
     */
    private void onValidateFailed(TextView failedView, String message) {
        failedView.requestFocus();
        failedView.setError(message);
    }


    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        if (!isFinishing())
            ProgressDialogUtil.closeProgressDialog();

        if (HttpConstants.ACTION_ADD_REVISIT_RECORD.equals(action)) {
            switch (response.getErrno()) {
                case 0://0：表示接口请求成功
                    ToastUtil.showInfo("添加回访记录成功！");
                    //刷新待跟进列表
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(TaskConstants.BINDLE_ADD_REVISIT_RECORD, true);
                    HCTasksWatched.getInstance().notifyWatchers(bundle);
                    finish();
                    break;
                default://1：发生错误
                    ToastUtil.showInfo(response.getErrmsg());
                    break;
            }
        }
    }


    @Override
    protected void onDestroy() {
        ProgressDialogUtil.closeProgressDialog();
        super.onDestroy();
    }
}