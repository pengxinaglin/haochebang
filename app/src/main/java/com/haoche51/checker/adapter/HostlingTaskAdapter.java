package com.haoche51.checker.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.HostlingTaskShortEntity;
import com.haoche51.checker.helper.ImageLoaderHelper;

import java.util.List;

/**
 * Created by mac on 16/1/12.
 */
public class HostlingTaskAdapter extends HCCommonAdapter<HostlingTaskShortEntity> {

  private List adapterTask;
  private int type;

  public HostlingTaskAdapter(Context context, List<HostlingTaskShortEntity> data, int layoutId, int type) {
    super(context, data, layoutId);
    this.adapterTask = data;
    this.type = type;
  }

  @Override
  public void fillViewData(HCCommonViewHolder holder, int position) {
    HostlingTaskShortEntity item = getItem(position);
    holder.setTextViewText(R.id.tv_title, item.getTitle());
    holder.setTextViewText(R.id.tv_task_num, item.getTask_num());
    holder.setTextViewText(R.id.tv_plate_number, item.getPlate_number());
    ImageView iv_head = holder.findTheView(R.id.iv_head);
    if (!TextUtils.isEmpty(item.getHead_url())) {
      iv_head.setBackgroundResource(0);
      ImageLoaderHelper.displayImage(item.getHead_url(), iv_head);
    }
    TextView tv_status = holder.findTheView(R.id.tv_status);
    tv_status.setVisibility(View.VISIBLE);
    tv_status.setText(item.getRepair_status_text());

    switch (type) {
      case TaskConstants.REQUEST_HOSTLING_DEFAULT:
        holder.findTheView(R.id.rl_fee).setVisibility(View.INVISIBLE);
        holder.findTheView(R.id.rl_time).setVisibility(View.INVISIBLE);
        break;
      case TaskConstants.REQUEST_HOSTLING_UNDERWAY:
        holder.setTextViewText(R.id.tv_fee, item.getExpect_price() + "元");
        holder.setTextViewText(R.id.tv_time, item.getPick_up_time());
        break;
      case TaskConstants.REQUEST_HOSTLING_COMPLETE:

        holder.setTextViewText(R.id.tv_item_fee_label, mContext.getString(R.string.hc_hostling_real_price));
        holder.setTextViewText(R.id.tv_item_time_label, mContext.getString(R.string.hc_hostling_repair_over_time));
        holder.setTextViewText(R.id.tv_fee, item.getReal_price() + "元");
        holder.setTextViewText(R.id.tv_time, item.getRepair_over_time());
        break;
    }
  }
}
