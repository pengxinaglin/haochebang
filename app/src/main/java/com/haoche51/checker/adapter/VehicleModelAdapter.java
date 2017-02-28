package com.haoche51.checker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.haoche51.checker.R;
import com.haoche51.checker.entity.VehicleModelEntity;

import java.util.List;

public class VehicleModelAdapter extends BaseAdapter {

	private List<VehicleModelEntity> mList = null;
	private Context mContext = null;
	private LayoutInflater  mInflater = null;
	private int checkedIndex = -1;
	public VehicleModelAdapter(Context context, List<VehicleModelEntity> mList) {
			mContext  = context;
			mInflater = LayoutInflater.from(mContext);
			this.mList = mList;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
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
			convertView = mInflater.inflate(R.layout.item_filter_result, null);
			mHolder.mText = (CheckedTextView) convertView.findViewById(R.id.vehicle_name);
			convertView.setTag(mHolder);
		}else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		initData(position,  mHolder);
		
		return convertView;
	}
	
	private void initData(int position, ViewHolder mHolder){
		mHolder.mText.setText(mList.get(position).getModel_name()+"|" + mList.get(position).getQuoto_price());
		if (checkedIndex == position) {
			mHolder.mText.setChecked(true);
		}else {
			mHolder.mText.setChecked(false);
		}
	}
	private class ViewHolder {
		CheckedTextView mText;
	}
	public void setmList(List<VehicleModelEntity> mList) {
		this.mList = mList;
	}

	public int getCheckedIndex() {
		return checkedIndex;
	}

	public void setCheckedIndex(int checkedIndex) {
		this.checkedIndex = checkedIndex;
	}

}
