package com.haoche51.checker.adapter;

import android.content.Context;
import android.util.TypedValue;

import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.constants.CompreConstants;
import com.haoche51.checker.item.CheckItem;
import com.haoche51.checker.util.StatusInfoUtils;

import java.util.List;

public class CompreCheckAdapter extends CheckAdapter {
    public CompreCheckAdapter(Context context, List<CheckItem> list) {
        super(context, list);
    }


    @Override
    protected void initData(final ViewHolder holder, final int position) {
        holder.mTitle.setText(checkList.get(position).getCheckItem());
        int status = checkList.get(position).getStatus();
        holder.mStatus.setText(StatusInfoUtils.getCompreStatus(status));
        if (status == CompreConstants.COMPRE_OK) {
            holder.mTitle.setTextColor(GlobalData.mContext.getResources().getColor(R.color.self_black));
            holder.mTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            holder.mStatus.setTextColor(GlobalData.mContext.getResources().getColor(R.color.hc_indicator));
        } else {
            holder.mTitle.setTextColor(GlobalData.mContext.getResources().getColor(R.color.hc_self_gray_hint));
            holder.mTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            holder.mStatus.setTextColor(GlobalData.mContext.getResources().getColor(R.color.self_black));
        }
    }
}
