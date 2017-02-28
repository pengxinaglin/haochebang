package com.haoche51.checker.adapter;

import android.content.Context;

import com.haoche51.checker.entity.SaleChannelEntity;

import java.util.List;

/**
 *  出售渠道适配器
 * Created by wfx on 2016/11/30.
 */
public class SaleChannelAdapter extends HCCommonAdapter<SaleChannelEntity> {

    public SaleChannelAdapter(Context context, List<SaleChannelEntity> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void fillViewData(HCCommonViewHolder holder, int position) {
        SaleChannelEntity saleChannelEntity = getItem(position);
        if(saleChannelEntity==null){
            return;
        }
        holder.setTextViewText(android.R.id.text1, saleChannelEntity.getValue());
    }
}
