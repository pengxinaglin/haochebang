package com.haoche51.checker.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.entity.PurchaseTaskShortEntity;

import java.util.List;

/**
 * Created by mac on 16/1/12.
 */
public class PurchaseTaskAdapter extends HCCommonAdapter<PurchaseTaskShortEntity> {


    public PurchaseTaskAdapter(Context context, List<PurchaseTaskShortEntity> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void fillViewData(HCCommonViewHolder holder, int position) {
        PurchaseTaskShortEntity item = getItem(position);
        holder.setTextViewText(R.id.tv_title, item.getTitle());
        TextView tv_status = holder.findTheView(R.id.tv_status);
        if ("待处理".equals(item.getStatus_text())) {
            tv_status.setTextColor(GlobalData.mContext.getResources().getColor(R.color.ic_orange));
        } else {
            tv_status.setTextColor(GlobalData.mContext.getResources().getColor(R.color.hc_self_red));
        }
        tv_status.setText(item.getStatus_text());
        holder.setTextViewText(R.id.tv_seller_name, item.getSeller_name());
        holder.setTextViewText(R.id.tv_seller_phone, item.getSeller_phone());
        holder.setTextViewText(R.id.tv_remark, item.getRemark());
        TextView tv_is_outside = holder.findTheView(R.id.tv_is_outside);
        //是否外网。0：否、1：是
        if (item.getIs_outside() == 1) {
            tv_is_outside.setText("外网");
            tv_is_outside.setVisibility(View.VISIBLE);
        } else {
            tv_is_outside.setVisibility(View.GONE);
        }
        TextView tvRevisitDate = holder.findTheView(R.id.tv_revisit_date);
        View vv_divider = holder.findTheView(R.id.vv_divider);
        if (!TextUtils.isEmpty(item.getNeed_visit())) {
            tvRevisitDate.setText(item.getNeed_visit());
            tvRevisitDate.setVisibility(View.VISIBLE);
            vv_divider.setVisibility(View.VISIBLE);
        } else {
            tvRevisitDate.setVisibility(View.GONE);
            vv_divider.setVisibility(View.GONE);
        }

    }
}
