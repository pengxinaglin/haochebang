package com.haoche51.checker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.entity.TempTaskEntity;

import java.util.List;

/**
 * 临时任务地址列表适配器
 * Created by wufx on 2016/2/24.
 */
public class TempTaskAdapter extends ArrayAdapter<TempTaskEntity> {
  private ViewHolder holder;
  private View view;

  public TempTaskAdapter(Context context, int resource, List<TempTaskEntity> tempTaskList) {
    super(context, resource, tempTaskList);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      view = LayoutInflater.from(GlobalData.mContext).inflate(R.layout.item_location_popup, parent, false);
      holder = new ViewHolder();
      holder.tv_key = (TextView) view.findViewById(R.id.tv_key);
      holder.tv_city = (TextView) view.findViewById(R.id.tv_city);
      view.setTag(holder);
    } else {
      view = convertView;
      holder = (ViewHolder) view.getTag();
    }
    TempTaskEntity tempTaskEntity = getItem(position);
    holder.tv_key.setText(tempTaskEntity.getKey());
    holder.tv_city.setText(tempTaskEntity.getCity());
    return view;
  }


  private static class ViewHolder {
    private TextView tv_key;
    private TextView tv_city;
  }

}
