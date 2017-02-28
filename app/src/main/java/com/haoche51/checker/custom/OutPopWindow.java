package com.haoche51.checker.custom;

import android.content.Context;
import android.view.View;
import android.widget.RadioGroup;

import com.haoche51.checker.R;
import com.haoche51.checker.constants.OutlookConstants;

public class OutPopWindow extends ItemPopWindow {

	public OutPopWindow(Context context, int status) {
		super(context, status);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId){
		case R.id.outer_normal:
			if (mListener != null) {
				mListener.statusChange(OutlookConstants.OUTLOOK_NORMAL, position);
			}
			break;
		case R.id.outer_scratch:
			if (mListener != null) {
				mListener.statusChange(OutlookConstants.OUTLOOK_SCRATCH, position);
			}
			break;
		case R.id.outer_broken:
			if (mListener != null) {
				mListener.statusChange(OutlookConstants.OUTLOOK_BROKEN, position);
			}
			break;
		case R.id.outer_panting:
			if (mListener != null) {
				mListener.statusChange(OutlookConstants.OUTLOOK_PAINTING, position);
			}
			break;
		case R.id.outer_replace:
			if (mListener != null) {
				mListener.statusChange(OutlookConstants.OUTLOOK_REPLACE, position);
			}
			break;
		}
		dismiss();
	}

	@Override
	protected void initView(View popView, int status) {
		mpopGroup = (RadioGroup) popView.findViewById(R.id.pop_outer);
		switch (status) {
		case OutlookConstants.OUTLOOK_NORMAL:
			mpopGroup.check(R.id.outer_normal);
			break;
		case OutlookConstants.OUTLOOK_SCRATCH:
			mpopGroup.check(R.id.outer_scratch);
			break;
		case OutlookConstants.OUTLOOK_BROKEN:
			mpopGroup.check(R.id.outer_broken);
			break;
		case OutlookConstants.OUTLOOK_PAINTING:
			mpopGroup.check(R.id.outer_panting);
			break;
		case OutlookConstants.OUTLOOK_REPLACE:
			mpopGroup.check(R.id.outer_replace);
			break;
		}
			
	}

	@Override
	protected int getRes() {
		return R.layout.pop_out_layout;
	}
	
	

}
