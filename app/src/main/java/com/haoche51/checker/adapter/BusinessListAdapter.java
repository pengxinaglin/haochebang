package com.haoche51.checker.adapter;

import android.content.Context;

import com.haoche51.checker.R;
import com.haoche51.checker.entity.BusinessShortEntity;

import java.util.List;

/**
 * Created by mac on 16/3/1.
 */
public class BusinessListAdapter extends HCCommonAdapter<BusinessShortEntity> {

	public BusinessListAdapter(Context context, List<BusinessShortEntity> data, int layoutId) {
		super(context, data, layoutId);
	}

	@Override
	public void fillViewData(HCCommonViewHolder holder, int position) {
		BusinessShortEntity item = getItem(position);
		holder.setTextViewText(R.id.tv_business_name, item.getName());
		holder.setTextViewText(R.id.tv_contact_name, item.getContact_name());
		holder.setTextViewText(R.id.tv_contact_phone, item.getContact_phone());
		holder.setTextViewText(R.id.tv_business_address, item.getAddress());
		holder.setTextViewText(R.id.tv_business_maintain_time, item.getMaintain_time());
		holder.setTextViewText(R.id.tv_business_sell_car, item.getSell_car() + "辆");
		holder.setTextViewText(R.id.tv_business_crm_user_name, item.getCrm_user_name() + " " + item.getCrm_user_phone());
		holder.setTextViewText(R.id.tv_business_user_remark, item.getRemark());
	}
}
