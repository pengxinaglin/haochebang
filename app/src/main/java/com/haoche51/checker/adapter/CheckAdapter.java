package com.haoche51.checker.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.item.CheckItem;

import java.util.List;

public class CheckAdapter extends BaseAdapter {
    protected Context mContext;
    protected List<CheckItem> checkList = null;
    protected LayoutInflater mInflater = null;

    public CheckAdapter(Context context, List<CheckItem> list) {
        mContext = context;
        checkList = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return checkList.size();
    }

    @Override
    public Object getItem(int position) {
        return checkList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_engine, parent, false);
            mHolder.mTitle = (TextView) convertView.findViewById(R.id.engine_label);
            mHolder.mStatus = (TextView) convertView.findViewById(R.id.engine_item_status);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        initData(mHolder, position);
        return convertView;
    }

    /**
     * 设置数据
     *
     * @param holder
     * @param position
     */
    protected void initData(final ViewHolder holder, final int position) {

    }

    /**
     * 改变文本和状态的样式
     * @param holder
     * @param status
     */
    protected void changeStatusStyle(ViewHolder holder,int status){
        if (status != 0) {
            holder.mTitle.setTextColor(GlobalData.mContext.getResources().getColor(R.color.self_black));
            holder.mTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            holder.mStatus.setTextColor(GlobalData.mContext.getResources().getColor(R.color.hc_indicator));
        } else {
            holder.mTitle.setTextColor(GlobalData.mContext.getResources().getColor(R.color.hc_self_gray_hint));
            holder.mTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            holder.mStatus.setTextColor(GlobalData.mContext.getResources().getColor(R.color.self_black));
        }
    }

    protected class ViewHolder {
        TextView mTitle;
        TextView mStatus;
    }
}
