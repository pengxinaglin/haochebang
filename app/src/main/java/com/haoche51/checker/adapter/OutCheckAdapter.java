package com.haoche51.checker.adapter;

import android.content.Context;

import com.haoche51.checker.item.CheckItem;
import com.haoche51.checker.util.StatusInfoUtils;

import java.util.List;

public class OutCheckAdapter extends CheckAdapter {


    public OutCheckAdapter(Context context, List<CheckItem> list) {
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
        holder.mStatus.setText(StatusInfoUtils.getOuterStatus(status));
        changeStatusStyle(holder, status);
    }


}
