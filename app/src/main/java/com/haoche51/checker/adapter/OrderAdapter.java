package com.haoche51.checker.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haoche51.checker.R;

/**
 * 排序数据适配器
 * Created by wfx on 2016/8/4.
 */
public class OrderAdapter extends BaseAdapter {
    private Context mContext;
    private String[] mOrders;

    public OrderAdapter(Context context, String[] orders) {
        this.mContext = context;
        this.mOrders = orders;
    }

    @Override
    public int getCount() {
        return mOrders == null ? 0 : mOrders.length;
    }

    @Override
    public Object getItem(int position) {
        return mOrders == null ? null : mOrders[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = View.inflate(mContext, R.layout.item_second_hand_car_order, null);
            holder = new ViewHolder();
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        String orderName = mOrders[position];
        holder.tv_order_name = (TextView) view.findViewById(R.id.tv_order_name);
        holder.tv_order_name.setText(orderName);
        return view;
    }

    private static class ViewHolder {
        TextView tv_order_name;
    }
}
