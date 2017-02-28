package com.haoche51.checker.adapter;

import android.content.Context;

import com.haoche51.checker.item.CheckItem;
import com.haoche51.checker.util.StatusInfoUtils;

import java.util.List;

public class StartupAdapter extends CheckAdapter {
    public StartupAdapter(Context context, List<CheckItem> list) {
        super(context, list);
    }

    @Override
    protected void initData(ViewHolder holder, int position) {
        holder.mTitle.setText(checkList.get(position).getCheckItem());
        int status = checkList.get(position).getStatus();
        holder.mStatus.setText(StatusInfoUtils.getStartupStatus(status));
        changeStatusStyle(holder, status);
    }


}
