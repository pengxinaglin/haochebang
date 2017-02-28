package com.haoche51.checker.adapter;

import android.content.Context;

import com.haoche51.checker.R;
import com.haoche51.checker.entity.PurchaseTaskShortEntity;

import java.util.List;

/**
 * 回购审批数据适配器
 * Created by wufx on 16/7/26.
 */
public class RepurchaseApproveAdapter extends HCCommonAdapter<PurchaseTaskShortEntity> {

    public RepurchaseApproveAdapter(Context context, List<PurchaseTaskShortEntity> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void fillViewData(HCCommonViewHolder holder, int position) {
        PurchaseTaskShortEntity item = getItem(position);
        holder.setTextViewText(R.id.tv_title, item.getTask_num() + "  " + item.getTitle());
        holder.setTextViewText(R.id.tv_status, item.getStatus_text());
    }


}
