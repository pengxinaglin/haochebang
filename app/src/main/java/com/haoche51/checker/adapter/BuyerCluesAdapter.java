package com.haoche51.checker.adapter;

import android.content.Context;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.entity.BuyerCluesEntity;
import com.haoche51.checker.util.ControlDisplayUtil;
import com.haoche51.checker.util.UnixTimeUtil;

import java.util.List;

public class BuyerCluesAdapter extends HCCommonAdapter<BuyerCluesEntity> {

	public BuyerCluesAdapter(Context context, List<BuyerCluesEntity> data, int layoutId) {
		super(context, data, layoutId);
	}

	@Override
	public void fillViewData(HCCommonViewHolder holder, int position) {
		BuyerCluesEntity entity = mList.get(position);

		holder.setTextViewText(R.id.tv_buyerphone, entity.getBuyer_phone());
		holder.setTextViewText(R.id.tv_buyerinfo, entity.getComment());
		holder.setTextViewText(R.id.tv_createtime, UnixTimeUtil.format(entity.getCreate_time(), "MM-dd HH:mm"));
		ControlDisplayUtil.getInstance().setTextAndColorByCluesStatus(entity.getStatus(), (TextView) holder.findTheView(R.id.tv_state));
	}
}
