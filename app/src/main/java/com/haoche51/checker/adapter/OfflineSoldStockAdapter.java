package com.haoche51.checker.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.haoche51.checker.R;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.OfflineShortEntity;
import com.haoche51.checker.helper.ImageLoaderHelper;

import java.util.List;

/**
 * 线下售出—库存中数据适配器
 * Created by wufx on 16/1/12.
 */
public class OfflineSoldStockAdapter extends HCCommonAdapter<OfflineShortEntity> {

    private int type;

    public OfflineSoldStockAdapter(Context context, List<OfflineShortEntity> data, int layoutId, int type) {
        super(context, data, layoutId);
        this.type = type;
    }

    @Override
    public void fillViewData(HCCommonViewHolder holder, int position) {
        OfflineShortEntity item = getItem(position);
        if (!TextUtils.isEmpty(item.getHead_url())) {
            ImageView iv_head = holder.findTheView(R.id.iv_head);
            iv_head.setBackgroundResource(0);
            ImageLoaderHelper.displayImage(item.getHead_url(),iv_head);
        }

        holder.setTextViewText(R.id.tv_title, item.getTitle());
        holder.setTextViewText(R.id.tv_task_num, item.getTask_num());
        holder.setTextViewText(R.id.tv_plate_number, item.getPlate_number());
        switch (type) {
            case TaskConstants.REQUEST_OFFLINESOLD_STOCK:
                holder.setTextViewText(R.id.tv_msg, item.getPlate_time() + "·" + item.getMile());
                holder.setTextViewText(R.id.tv_price_type, mContext.getString(R.string.hc_offlinesold_price));
                holder.setTextViewText(R.id.tv_price, item.getPrice() + "元");
                break;
            case TaskConstants.REQUEST_OFFLINESOLD_SALE:
            case TaskConstants.REQUEST_OFFLINESOLD_COMPLETE:
                holder.setTextViewText(R.id.tv_price_type, mContext.getString(R.string.hc_offlinesold_sold_price));
                holder.setTextViewText(R.id.tv_price, item.getSold_price() + "元");
                holder.findTheView(R.id.rl_msg).setVisibility(View.GONE);
                ((LinearLayout.LayoutParams) holder.findTheView(R.id.iv_head).getLayoutParams()).weight = 2;
                break;
        }
        if (!TextUtils.isEmpty(item.getStatus_text())) {
            holder.setTextViewText(R.id.tv_status, item.getStatus_text());
            holder.findTheView(R.id.tv_status).setVisibility(View.VISIBLE);
        } else if(item.getIs_self()==1){
            holder.setTextViewText(R.id.tv_status, "本人收购");
            holder.findTheView(R.id.tv_status).setVisibility(View.VISIBLE);
        }else {
            holder.findTheView(R.id.tv_status).setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(item.getTag())) {
            holder.setTextViewText(R.id.tv_tag, item.getTag());
            holder.findTheView(R.id.tv_tag).setVisibility(View.VISIBLE);
        } else {
            holder.findTheView(R.id.tv_tag).setVisibility(View.GONE);
        }


    }
}
