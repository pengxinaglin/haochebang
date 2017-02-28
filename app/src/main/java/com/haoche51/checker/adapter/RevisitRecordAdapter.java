package com.haoche51.checker.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.haoche51.checker.R;
import com.haoche51.checker.entity.RevisitRecordEntity;
import com.haoche51.checker.util.HCArithUtil;

import java.util.List;

/**
 * 回访记录数据解析
 * Created by wfx on 16/07/20.
 */
public class RevisitRecordAdapter extends HCCommonAdapter<RevisitRecordEntity> {

    public RevisitRecordAdapter(Context context, List<RevisitRecordEntity> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void fillViewData(HCCommonViewHolder holder, int position) {
        RevisitRecordEntity revisitRecord = getItem(position);
        if (revisitRecord == null) {
            return;
        }
        if (!TextUtils.isEmpty(revisitRecord.getVisit_user())) {

            holder.setTextViewText(R.id.tv_revisitor, revisitRecord.getVisit_user());
        } else {
            holder.setTextViewText(R.id.tv_revisitor, "--");
        }

        if (!TextUtils.isEmpty(revisitRecord.getVisit_time())) {
            holder.setTextViewText(R.id.tv_revisit_time, revisitRecord.getVisit_time());
        } else {
            holder.setTextViewText(R.id.tv_revisit_time, "--");
        }

        if (!TextUtils.isEmpty(revisitRecord.getReason())) {
            holder.setTextViewText(R.id.tv_sell_reason, revisitRecord.getReason());
        } else {
            holder.setTextViewText(R.id.tv_sell_reason, "--");
        }

        if (!TextUtils.isEmpty(revisitRecord.getSold_time())) {
            holder.setTextViewText(R.id.tv_sell_time, revisitRecord.getSold_time());
        } else {
            holder.setTextViewText(R.id.tv_sell_time, "--");
        }

        if (!TextUtils.isEmpty(revisitRecord.getDecide_user())) {
            holder.setTextViewText(R.id.tv_sell_decider, revisitRecord.getDecide_user());
        } else {
            holder.setTextViewText(R.id.tv_sell_decider, "--");
        }

        if (revisitRecord.getExpect_price() != 0) {
            holder.setTextViewText(R.id.tv_expected_price, HCArithUtil.div(revisitRecord.getExpect_price(), 10000) + "万元");
        } else {
            holder.setTextViewText(R.id.tv_expected_price, "--");
        }

        if (revisitRecord.getPeer_price() != 0) {

            holder.setTextViewText(R.id.tv_offer_price, HCArithUtil.div(revisitRecord.getPeer_price(), 10000) + "万元");
        } else {
            holder.setTextViewText(R.id.tv_offer_price, "--");
        }

        if (!TextUtils.isEmpty(revisitRecord.getOther())) {
            holder.setTextViewText(R.id.tv_other, revisitRecord.getOther());
        } else {
            holder.setTextViewText(R.id.tv_other, "--");
        }

    }

}
