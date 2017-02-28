package com.haoche51.checker.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.PaymentRecordEntity;
import com.haoche51.checker.util.ControlDisplayUtil;
import com.haoche51.checker.util.DisplayUtils;
import com.haoche51.checker.util.UnixTimeUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * 车源推荐数据解析
 * Created by wfx on 15/12/21.
 */
public class PaymentRecordAdapter extends HCCommonAdapter<PaymentRecordEntity> {

    public PaymentRecordAdapter(Context context, List<PaymentRecordEntity> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void fillViewData(HCCommonViewHolder holder, int position) {
        PaymentRecordEntity paymentRecord = getItem(position);
        if (paymentRecord == null) {
            return;
        }
        ImageView iv_pay = holder.findTheView(R.id.iv_pay_stat);
        TextView tv_pay_stat = holder.findTheView(R.id.tv_pay_stat);
        TextView tv_reject_reason = holder.findTheView(R.id.tv_reject_reason);
        TextView tv_time = holder.findTheView(R.id.tv_time);
        tv_pay_stat.setText(paymentRecord.getStatus_text());
        tv_reject_reason.setText(paymentRecord.getFail_reason());
        if (paymentRecord.getStatus() == TaskConstants.APPLY_PAY_STATUS_APLYING) {
            tv_time.setText("");
        } else {
            tv_time.setText(UnixTimeUtil.format(Long.valueOf(paymentRecord.getPay_time()).intValue(),UnixTimeUtil.DEFAULT_PATTERN_2));
        }

        ControlDisplayUtil.getInstance().changeDisplayStyle(paymentRecord.getStatus(), iv_pay, tv_pay_stat, tv_reject_reason);
        holder.setTextViewText(R.id.tv_pay_money, DisplayUtils.parseMoney(new BigDecimal(paymentRecord.getPrice()), ""));
        holder.setTextViewText(R.id.tv_pay_purpose, paymentRecord.getPrice_type());
        holder.setTextViewText(R.id.tv_pay_account, paymentRecord.getAccount_num());
        holder.setTextViewText(R.id.tv_pay_bank, paymentRecord.getAccount_user().concat(" ").concat(paymentRecord.getAccount_bank()));
    }

}
