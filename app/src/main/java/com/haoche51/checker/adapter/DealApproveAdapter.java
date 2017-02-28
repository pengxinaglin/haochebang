package com.haoche51.checker.adapter;

import android.content.Context;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.entity.DealApproveEntity;

import java.util.List;

/**
 * 交易审批数据适配器
 * Created by wufx on 16/7/26.
 */
public class DealApproveAdapter extends HCCommonAdapter<DealApproveEntity> {
    private Context mContext;

    public DealApproveAdapter(Context context, List<DealApproveEntity> data, int layoutId) {
        super(context, data, layoutId);
        this.mContext = context;
    }

    @Override
    public void fillViewData(HCCommonViewHolder holder, int position) {
        DealApproveEntity item = getItem(position);
        holder.setTextViewText(R.id.tv_title, item.getOrder_id() + "  " + item.getClass_name());
        TextView tv_status = holder.findTheView(R.id.tv_status);
        tv_status.setText(item.getAudit_desc());
        if ("1".equals(item.getMy_audit())) {
            tv_status.setTextColor(mContext.getResources().getColor(R.color.red));
        } else {
            tv_status.setTextColor(mContext.getResources().getColor(R.color.hc_self_black));
        }
    }


}
