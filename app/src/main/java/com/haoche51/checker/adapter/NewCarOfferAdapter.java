package com.haoche51.checker.adapter;

import android.content.Context;

import com.haoche51.checker.R;
import com.haoche51.checker.entity.OfferEntity;

import java.util.List;

/**
 * 新车报价数据解析
 * Created by wfx on 16/8/9.
 */
public class NewCarOfferAdapter extends HCCommonAdapter<OfferEntity> {

    public NewCarOfferAdapter(Context context, List<OfferEntity> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void fillViewData(HCCommonViewHolder holder, int position) {
        OfferEntity offerEntity = getItem(position);
        if (offerEntity == null) {
            return;
        }
        holder.setTextViewText(R.id.tv_title, offerEntity.getTitle());
        holder.setTextViewText(R.id.tv_price, offerEntity.getPrice() + "万元");
    }

}
