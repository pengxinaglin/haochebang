package com.haoche51.checker.custom;

import android.content.Context;
import android.view.View;
import android.widget.RadioGroup;

import com.haoche51.checker.R;
import com.haoche51.checker.constants.OutlookConstants;

public class CoverPopWindow extends ItemPopWindow {
	
	
	
	public CoverPopWindow(Context context, int status) {
		super(context, status);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.covering_normal:
			if (mListener != null) {
				mListener.statusChange(OutlookConstants.COVERING_NORMAL, position);
			}
			break;
		case R.id.covering_painting:
			if (mListener != null) {
				mListener.statusChange(OutlookConstants.COVERING_PAINTING, position);
			}
			break;
		case R.id.covering_metal_repair:
			if (mListener != null) {
				mListener.statusChange(OutlookConstants.COVERING_METALREPAIR, position);
			}
			break;
		case R.id.covering_replace:
			if (mListener != null) {
				mListener.statusChange(OutlookConstants.COVERING_REPLACE, position);
			}
			break;
		}
		dismiss();
	}

	@Override
	protected void initView(View popView, int status) {
		mpopGroup = (RadioGroup) popView.findViewById(R.id.pop_covering);
		switch (status){
		case OutlookConstants.COVERING_NORMAL:
			mpopGroup.check(R.id.covering_normal);
			break;
		case OutlookConstants.COVERING_PAINTING:
			mpopGroup.check(R.id.covering_painting);
			break;
		case OutlookConstants.COVERING_METALREPAIR:
			mpopGroup.check(R.id.covering_metal_repair);
			break;
		case OutlookConstants.COVERING_REPLACE:
			mpopGroup.check(R.id.covering_replace);
			break;
		}
	}

	@Override
	protected int getRes() {
		return R.layout.pop_covering_layout;
	}

}
