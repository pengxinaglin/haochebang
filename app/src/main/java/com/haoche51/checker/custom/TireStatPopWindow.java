package com.haoche51.checker.custom;

import android.content.Context;
import android.view.View;
import android.widget.RadioGroup;

import com.haoche51.checker.R;
import com.haoche51.checker.constants.EquipmentConstants;

/**
 * 轮胎状况
 * Created by wfx on 2016/7/2.
 */
public class TireStatPopWindow extends ItemPopWindow {

    public TireStatPopWindow(Context context, int status) {
        super(context, status);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_tire_no:
                if (mListener != null) {
                    mListener.statusChange(EquipmentConstants.TIRE_NO, position);
                }
                break;
            case R.id.rb_tire_new:
                if (mListener != null) {
                    mListener.statusChange(EquipmentConstants.TIRE_NIEW, position);
                }
                break;
            case R.id.rb_tire_normal_wear:
                if (mListener != null) {
                    mListener.statusChange(EquipmentConstants.TIRE_NORMAL_WEAR, position);
                }
                break;
            case R.id.rb_tire_heavy_wear:
                if (mListener != null) {
                    mListener.statusChange(EquipmentConstants.TIRE_HEAVY_WEAR, position);
                }
                break;
        }
        dismiss();
    }

    @Override
    protected void initView(View popView, int status) {
        mpopGroup = (RadioGroup) popView.findViewById(R.id.pop_glass);
        switch (status) {
            case EquipmentConstants.TIRE_NO:
                mpopGroup.check(R.id.rb_tire_no);
                break;
            case EquipmentConstants.TIRE_NIEW:
                mpopGroup.check(R.id.rb_tire_new);
                break;
            case EquipmentConstants.TIRE_NORMAL_WEAR:
                mpopGroup.check(R.id.rb_tire_normal_wear);
                break;
            case EquipmentConstants.TIRE_HEAVY_WEAR:
                mpopGroup.check(R.id.rb_tire_heavy_wear);
                break;
        }

    }

    @Override
    protected int getRes() {
        return R.layout.pop_tire_stat_layout;
    }


}
