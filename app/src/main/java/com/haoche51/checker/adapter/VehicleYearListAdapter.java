package com.haoche51.checker.adapter;

import android.content.Context;

import com.haoche51.checker.R;
import com.haoche51.checker.entity.VehicleSourceEntity;

import java.util.List;

/**
 * 年份列表
 * Created by wufx on 2016/1/23
 */
public class VehicleYearListAdapter extends HCCommonAdapter<VehicleSourceEntity> {

  public VehicleYearListAdapter(Context context, List data, int layoutId) {
    super(context, data, layoutId);
  }

  @Override
  public void fillViewData(HCCommonViewHolder holder, int position) {
    holder.setTextViewText(R.id.text_view_series_list_item_series, mList.get(position).getYear());
  }

}
