package com.haoche51.checker.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.entity.UserRightShortEntity;

import java.util.ArrayList;

/**
 * 主页数据适配器
 * Created by wfx on 2016/7/11.
 */
public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.ViewHolder> {
    private ArrayList<UserRightShortEntity> mDataList;

    public HomePageAdapter(ArrayList<UserRightShortEntity> dataList) {
        this.mDataList = dataList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_page, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        UserRightShortEntity userRightShort = mDataList.get(position);
        holder.tv_item.setText(userRightShort.getName());
        holder.tv_item.setCompoundDrawablesWithIntrinsicBounds(0, userRightShort.getResId(), 0, 0);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_item;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tv_item = (TextView) itemView.findViewById(R.id.tv_item_home_page);
        }
    }
}
