package com.haoche51.checker.adapter;

import android.content.Context;

import com.haoche51.checker.R;
import com.haoche51.checker.entity.VehicleTypeEntity;

import java.util.List;

/**
 * 车型列表
 * Created by wufx on 2016/1/23
 */
public class VehicleTypeListAdapter extends HCCommonAdapter<VehicleTypeEntity> {

  public VehicleTypeListAdapter(Context context, List data, int layoutId) {
    super(context, data, layoutId);
  }

  @Override
  public void fillViewData(HCCommonViewHolder holder, int position) {
    holder.setTextViewText(R.id.text_view_series_list_item_series, mList.get(position).getName());
  }

}
