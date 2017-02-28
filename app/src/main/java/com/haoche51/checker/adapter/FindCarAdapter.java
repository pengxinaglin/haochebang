package com.haoche51.checker.adapter;

import android.content.Context;
import android.widget.TextView;

import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.FindCarEntity;

import java.util.List;

/**
 * 找车需求
 * Created by wfx on 16/11/2.
 */
public class FindCarAdapter extends HCCommonAdapter<FindCarEntity> {

    public FindCarAdapter(Context context, List<FindCarEntity> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void fillViewData(HCCommonViewHolder holder, int position) {
        FindCarEntity item = getItem(position);
        holder.setTextViewText(R.id.tv_buyer_name, item.getBuyer_name());
        holder.setTextViewText(R.id.tv_buyer_phone, item.getBuyer_phone());
        TextView tv_status = holder.findTheView(R.id.tv_status);
        tv_status.setText(item.getStatus_text());
        if (item.getStatus() == TaskConstants.FIND_CAR_STATUS_MATCHED) {
            tv_status.setTextColor(GlobalData.mContext.getResources().getColor(R.color.hc_self_gray));
        } else {
            tv_status.setTextColor(GlobalData.mContext.getResources().getColor(R.color.hc_self_red));
        }
        StringBuilder sb = new StringBuilder();
        sb.append(item.getVehicle_text()).append(" | ").append(item.getPrice_text()).append(" | ");
        sb.append(item.getAge_text()).append(" | ").append(item.getGearbox_text()).append(" | ");
        sb.append(item.getColor_text()).append(" | ").append(item.getOther_text());
        holder.setTextViewText(R.id.tv_vehicle, sb.toString());
    }

}
