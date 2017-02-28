package com.haoche51.checker.adapter;

import android.content.Context;

import com.haoche51.checker.item.CheckItem;
import com.haoche51.checker.util.StatusInfoUtils;

import java.util.List;

public class GlassCheckAdapter extends CheckAdapter {

    public GlassCheckAdapter(Context context, List<CheckItem> list) {
        super(context, list);
    }

    /**
     * 设置数据
     *
     * @param holder
     * @param position
     */
    @Override
    protected void initData(final ViewHolder holder, final int position) {
        holder.mTitle.setText(checkList.get(position).getCheckItem());
        int status = checkList.get(position).getStatus();
        holder.mStatus.setText(StatusInfoUtils.getGlassStatus(status));
        changeStatusStyle(holder, status);
//        if (status != OutlookConstants.GLASS_NORMAL) {
//            holder.mTitle.setTextColor(GlobalData.mContext.getResources().getColor(R.color.self_black));
//            holder.mTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
//            holder.mStatus.setTextColor(GlobalData.mContext.getResources().getColor(R.color.hc_indicator));
//        } else {
//            holder.mTitle.setTextColor(GlobalData.mContext.getResources().getColor(R.color.hc_self_gray_hint));
//            holder.mTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
//            holder.mStatus.setTextColor(GlobalData.mContext.getResources().getColor(R.color.self_black));
//        }
    }
}
