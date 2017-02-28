package com.haoche51.checker.custom;

import android.content.Context;
import android.view.View;
import android.widget.RadioGroup;

import com.haoche51.checker.R;
import com.haoche51.checker.constants.OutlookConstants;

public class GlassPopWindow extends ItemPopWindow {

	public GlassPopWindow(Context context, int status) {
		super(context, status);
		
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId){
		case R.id.glass_normal:
			if (mListener != null) {
				mListener.statusChange(OutlookConstants.GLASS_NORMAL, position);
			}
			break;
		case R.id.glass_cracks:
			if (mListener != null) {
				mListener.statusChange(OutlookConstants.GLASS_CRACKS, position);
			}
			break;
		case R.id.glass_broken:
			if (mListener != null) {
				mListener.statusChange(OutlookConstants.GLASS_BROKEN, position);
			}
			break;
		case R.id.glass_change:
			if (mListener != null) {
				mListener.statusChange(OutlookConstants.GLASS_CHANGE, position); 
			}
			break;
		}
		dismiss();
	}

	@Override
	protected void initView(View popView, int status) {
		mpopGroup = (RadioGroup) popView.findViewById(R.id.pop_glass);
		switch (status){
		case OutlookConstants.GLASS_NORMAL:
			mpopGroup.check(R.id.glass_normal);
			break;
		case OutlookConstants.GLASS_CRACKS:
			mpopGroup.check(R.id.glass_cracks);
			break;
		case OutlookConstants.GLASS_BROKEN:
			mpopGroup.check(R.id.glass_broken);
			break;
		case OutlookConstants.GLASS_CHANGE:
			mpopGroup.check(R.id.glass_change);
			break;
		}
	}

	@Override
	protected int getRes() {
		return R.layout.pop_glass_layout;
	}
	
	

}
