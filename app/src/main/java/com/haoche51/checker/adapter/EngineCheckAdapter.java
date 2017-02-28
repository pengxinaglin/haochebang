package com.haoche51.checker.adapter;

import android.content.Context;

import com.haoche51.checker.item.CheckItem;
import com.haoche51.checker.util.StatusInfoUtils;

import java.util.List;

public class EngineCheckAdapter extends CheckAdapter {
    public EngineCheckAdapter(Context context, List<CheckItem> list) {
        super(context, list);
    }


    @Override
    protected void initData(final ViewHolder holder, final int position) {
        holder.mTitle.setText(checkList.get(position).getCheckItem());
        int status = checkList.get(position).getStatus();
        holder.mStatus.setText(StatusInfoUtils.getEngineStatus(status));
        changeStatusStyle(holder, status);
    }
}
