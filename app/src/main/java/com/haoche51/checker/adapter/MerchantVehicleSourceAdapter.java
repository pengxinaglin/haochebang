package com.haoche51.checker.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.haoche51.checker.R;
import com.haoche51.checker.entity.MerchantVehicleSourceEntity;
import com.haoche51.checker.helper.ImageLoaderHelper;

import java.util.List;

/**
 * Created by PengXianglin on 16/3/4.
 */
public class MerchantVehicleSourceAdapter extends HCCommonAdapter<MerchantVehicleSourceEntity> {
	public MerchantVehicleSourceAdapter(Context context, List<MerchantVehicleSourceEntity> data, int layoutId) {
		super(context, data, layoutId);
	}

	@Override
	public void fillViewData(HCCommonViewHolder holder, int position) {
		MerchantVehicleSourceEntity item = getItem(position);
		ImageView imageView = holder.findTheView(R.id.iv_cover_img);
		if (!TextUtils.isEmpty(item.getCover_img()))
		ImageLoaderHelper.displayImage(item.getCover_img(),imageView);
		holder.setTextViewText(R.id.tv_title, item.getTitle());
		holder.setTextViewText(R.id.tv_status, item.getStatus_text());
		holder.setTextViewText(R.id.tv_id, item.getId() + "");
		holder.setTextViewText(R.id.tv_price, item.getPrice() + "万元");
		holder.setTextViewText(R.id.tv_up_time, item.getUp_time());
	}
}
