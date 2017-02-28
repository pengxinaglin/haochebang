package com.haoche51.checker.adapter;

import android.content.Context;

import com.haoche51.checker.R;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.VehicleRecomEntity;
import com.haoche51.checker.util.UnixTimeUtil;

import java.util.List;

/**
 * 车源推荐数据解析
 * Created by wfx on 15/12/21.
 */
public class VehicleRecomAdapter extends HCCommonAdapter<VehicleRecomEntity> {

	public VehicleRecomAdapter(Context context, List<VehicleRecomEntity> data, int layoutId) {
		super(context, data, layoutId);
	}

	@Override
	public void fillViewData(HCCommonViewHolder holder, int position) {
		VehicleRecomEntity recomEntity = getItem(position);
		holder.setTextViewText(R.id.tv_check_vehicle,recomEntity.getBrand_name().concat(" ").concat(recomEntity.getClass_name()));
		holder.setTextViewText(R.id.tv_check_status,changeVehicleRecomStatus(recomEntity.getStatus()));
		holder.setTextViewText(R.id.tv_item_check_seller_name,recomEntity.getName());
		holder.setTextViewText(R.id.tv_item_check_seller_phone,recomEntity.getPhone());
		holder.setTextViewText(R.id.tv_item_check_time, UnixTimeUtil.formatYearDotMounth(recomEntity.getCreate_time()));
	}

	/**
	 * 解析车源推荐状态
	 * 0待审核 1线索无效 2待处理 3成功返利
	 */
	private String changeVehicleRecomStatus(int status){
		String result= "";
		switch (status){
			case 0:
				result= TaskConstants.VEHICLE_STATUS_CHECK_PENDING;
				break;
			case 1:
				result= TaskConstants.VEHICLE_STATUS_CLUE_INVALID;
				break;
			case 2:
				result= TaskConstants.VEHICLE_STATUS_TO_OPERATE;
				break;
			case 3:
				result= TaskConstants.VEHICLE_STATUS_SUCCESS_RETURN;
				break;
		}
		return result;
	}
}
