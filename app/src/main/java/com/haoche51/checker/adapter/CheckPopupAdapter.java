package com.haoche51.checker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.entity.CheckPopupEntity;
import com.haoche51.checker.util.UnixTimeUtil;

import java.util.List;

/**
 * PopupWindow中ListView数据适配器
 * Created by wufx on 2016/2/24.
 */
public class CheckPopupAdapter extends BaseAdapter {
  private List<CheckPopupEntity> popupEntityList;
  private Context context;
  private ViewHolder holder;
  private View view;

  public CheckPopupAdapter(Context context, List<CheckPopupEntity> popupEntityList) {
    this.context = context;
    this.popupEntityList = popupEntityList;
  }

  @Override
  public int getCount() {
    return popupEntityList.size();
  }

  @Override
  public Object getItem(int position) {
    return popupEntityList.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      view = LayoutInflater.from(context).inflate(R.layout.item_check_popup, parent, false);
      holder = new ViewHolder();
      holder.tv_popup_date = (TextView) view.findViewById(R.id.tv_popup_date);
      holder.tv_popup_online_num = (TextView) view.findViewById(R.id.tv_popup_online_num);
      holder.tv_popup_purchase_num = (TextView) view.findViewById(R.id.tv_popup_purchase_num);
      holder.tv_popup_sold_num = (TextView) view.findViewById(R.id.tv_popup_sold_num);
      holder.tv_popup_sold_rate = (TextView) view.findViewById(R.id.tv_popup_sold_rate);
      view.setTag(holder);
    } else {
      view = convertView;
      holder = (ViewHolder) view.getTag();
    }
    CheckPopupEntity checkPopupEntity = popupEntityList.get(position);
    holder.tv_popup_date.setText(UnixTimeUtil.format(checkPopupEntity.getTime(), UnixTimeUtil.YEAR_MONTH_PATTERN_NEW));
    holder.tv_popup_online_num.setText(String.valueOf(checkPopupEntity.getOnline()));
    holder.tv_popup_purchase_num.setText(String.valueOf(checkPopupEntity.getBuy_back()));
    holder.tv_popup_sold_num.setText(String.valueOf(checkPopupEntity.getSold()));
    holder.tv_popup_sold_rate.setText(String.valueOf(checkPopupEntity.getSold_rate()));
    return view;
  }


  private static class ViewHolder {
    private TextView tv_popup_date;
    private TextView tv_popup_online_num;
    private TextView tv_popup_sold_rate;
    private TextView tv_popup_sold_num;
    private TextView tv_popup_purchase_num;
  }

}
