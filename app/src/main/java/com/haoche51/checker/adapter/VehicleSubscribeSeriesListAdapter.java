package com.haoche51.checker.adapter;

import android.content.Context;

import com.haoche51.checker.R;
import com.haoche51.checker.entity.VehicleSeriesEntity;

import java.util.List;

/**
 * Created by yangming on 2015/11/2.
 */
public class VehicleSubscribeSeriesListAdapter extends HCCommonAdapter<VehicleSeriesEntity> {

  public VehicleSubscribeSeriesListAdapter(Context context, List data, int layoutId) {
    super(context, data, layoutId);
  }

  @Override
  public void fillViewData(HCCommonViewHolder holder, int position) {
    holder.setTextViewText(R.id.text_view_series_list_item_series, mList.get(position).getName());
  }

}
