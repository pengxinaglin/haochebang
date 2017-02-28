package com.haoche51.checker.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.PurchaseTaskShortEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by mac on 16/1/12.
 */
public class PurchaseCompleteAdapter extends HCCommonAdapter<PurchaseTaskShortEntity> {


    public PurchaseCompleteAdapter(Context context, List<PurchaseTaskShortEntity> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void fillViewData(HCCommonViewHolder holder, int position) {
        PurchaseTaskShortEntity item = getItem(position);
        holder.setTextViewText(R.id.tv_title, item.getTitle());
        holder.setTextViewText(R.id.tv_status, item.getStatus_text());
        holder.setTextViewText(R.id.tv_seller_name, item.getSeller_name());
        holder.setTextViewText(R.id.tv_seller_phone, item.getSeller_phone());
        TextView tv_is_outside = holder.findTheView(R.id.tv_is_outside);
        //是否外网。0：否、1：是
        if (item.getIs_outside() == 1) {
            tv_is_outside.setText("外网");
            tv_is_outside.setVisibility(View.VISIBLE);
        } else {
            tv_is_outside.setVisibility(View.GONE);
        }

        int totalBackPrice = Integer.parseInt(item.getTotal_back_price());
        int hasPay = Integer.parseInt(item.getHas_pay());
        //若款未付完，则显示进度条
        LinearLayout ll_pay_progress = holder.findTheView(R.id.ll_pay_progress);
        RelativeLayout rl_pay_desc = holder.findTheView(R.id.rl_pay_desc);
        if (hasPay == totalBackPrice || item.getStatus() == TaskConstants.TASK_STATUS_BREAK_CONTRACT_PASS
                || item.getStatus() == TaskConstants.TASK_STATUS_FAIL) {
            ll_pay_progress.setVisibility(View.GONE);
            rl_pay_desc.setVisibility(View.GONE);
            return;
        } else {
            ll_pay_progress.setVisibility(View.VISIBLE);
            rl_pay_desc.setVisibility(View.VISIBLE);
        }

        BigDecimal unitWan = new BigDecimal(10000);
        ProgressBar pb_pay_progress = holder.findTheView(R.id.pb_pay_progress);
        if (totalBackPrice == 0 || totalBackPrice == hasPay) {
            pb_pay_progress.setProgress(10000);
        } else if (hasPay == 0) {
            pb_pay_progress.setProgress(0);
        } else {
            int percent = new BigDecimal(item.getHas_pay()).divide(new BigDecimal(item.getTotal_back_price()), 10, BigDecimal.ROUND_UP).multiply(unitWan).intValue();
            pb_pay_progress.setProgress(percent);
        }
        String payedAmt = new BigDecimal(item.getHas_pay()).divide(unitWan).toString();
        String totalPayAmt = new BigDecimal(item.getTotal_back_price()).divide(unitWan).toString();
        holder.setTextViewText(R.id.tv_pay_desc, "付款进度：总共" + totalPayAmt + "万（已付" + payedAmt + "万）");
        Button btnPayStat = holder.findTheView(R.id.btn_pay_stat);
        btnPayStat.setText(item.getLast_pay_status_text());
        changeBtnBackground(item.getLast_pay_status(), btnPayStat);

    }

    /**
     * 根据最后一次付款状态改变按钮背景色
     *
     * @param status
     * @param btnPay
     */
    private void changeBtnBackground(int status, Button btnPay) {
        switch (status) {
            case TaskConstants.APPLY_PAY_STATUS_APLYING:
                btnPay.setVisibility(View.VISIBLE);
                btnPay.setBackgroundResource(R.drawable.shape_blue_oval);
                break;
            case TaskConstants.APPLY_PAY_STATUS_REJECT:
                btnPay.setVisibility(View.VISIBLE);
                btnPay.setBackgroundResource(R.drawable.shape_red_oval);
                break;
            case TaskConstants.APPLY_PAY_STATUS_PAYED:
                btnPay.setVisibility(View.VISIBLE);
                btnPay.setBackgroundResource(R.drawable.shape_green_oval);
                break;
            default:
                btnPay.setVisibility(View.GONE);
                break;
        }
    }
}
