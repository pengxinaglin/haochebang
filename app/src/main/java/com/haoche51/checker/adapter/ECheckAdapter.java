package com.haoche51.checker.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.constants.EquipmentConstants;
import com.haoche51.checker.item.ECheckItem;
import com.haoche51.checker.util.StatusInfoUtils;

import java.util.List;

public class ECheckAdapter extends BaseAdapter {
    protected Context mContext;
    protected List<ECheckItem> checkList = null;
    protected LayoutInflater mInflater = null;
    protected int type = 0;// 0 equipment 1, safty 2 light

    public ECheckAdapter(Context context, List<ECheckItem> list, int type) {
        mContext = context;
        checkList = list;
        mInflater = LayoutInflater.from(context);
        this.type = type;
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
            convertView = mInflater.inflate(R.layout.item_feature, parent, false);
            mHolder.mTitle = (TextView) convertView.findViewById(R.id.eq_label);
            mHolder.hasSwitch = (Switch) convertView.findViewById(R.id.sw_has);
            mHolder.mStatus = (TextView) convertView.findViewById(R.id.eq_item_status);
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
        switch (type) {
            case 0:
                holder.mTitle.setText(EquipmentConstants.EQUIPMENT_PART[position]);
                break;
            case 1:
                holder.mTitle.setText(EquipmentConstants.SAFTY_PART[position]);
                break;
            case 2:
                holder.mTitle.setText(EquipmentConstants.LIGHT_PART[position]);
                break;
        }
        holder.hasSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    holder.hasSwitch.setChecked(true);
                    checkList.get(position).setHas(EquipmentConstants.HAS_EQUIPMENT);
                } else {
                    holder.hasSwitch.setChecked(false);
                    checkList.get(position).setHas(EquipmentConstants.NO_EQUIPMENT);
                }
            }

        });
        if (checkList.get(position).getHas() == EquipmentConstants.HAS_EQUIPMENT) {
            holder.hasSwitch.setChecked(true);
        } else {
            holder.hasSwitch.setChecked(false);
        }
        if (!checkList.get(position).isExtra()) {
            //holder.hasSwitch.setEnabled(false);
            holder.hasSwitch.setVisibility(View.GONE);
        } else {
            //holder.hasSwitch.setEnabled(true);
            holder.hasSwitch.setVisibility(View.VISIBLE);
        }
        int status = checkList.get(position).getStatus();
        holder.mStatus.setText(StatusInfoUtils.getEquipmentStatus(status));
        if (status != EquipmentConstants.EQUIPMENT_NORMAL) {
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
        Switch hasSwitch;
        TextView mStatus;
    }
}
