package com.haoche51.checker.custom;

import android.content.Context;
import android.view.View;
import android.widget.RadioGroup;

import com.haoche51.checker.R;
import com.haoche51.checker.constants.TrimConstants;

public class TrimPopWindow extends ItemPopWindow {
	
	
	
	public TrimPopWindow(Context context, int status) {
		super(context, status);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.trim_normal:
			if (mListener != null) {
				mListener.statusChange(TrimConstants.NORMAL, position);
			}
			break;
		case R.id.trim_stain:
			if (mListener != null) {
				mListener.statusChange(TrimConstants.STAIN, position);
			}
			break;
		case R.id.trim_ageing:
			if (mListener != null) {
				mListener.statusChange(TrimConstants.AGEING, position);
			}
			break;
		case R.id.trim_damaged:
			if (mListener != null) {
				mListener.statusChange(TrimConstants.DAMAGED, position);
			}
			break;
		}
		dismiss();
	}

	@Override
	protected void initView(View popView, int status) {
		mpopGroup = (RadioGroup) popView.findViewById(R.id.pop_trim);
		switch (status){
		case TrimConstants.NORMAL:
			mpopGroup.check(R.id.trim_normal);
			break;
		case TrimConstants.STAIN:
			mpopGroup.check(R.id.trim_stain);
			break;
		case TrimConstants.AGEING:
			mpopGroup.check(R.id.trim_ageing);
			break;
		case TrimConstants.DAMAGED:
			mpopGroup.check(R.id.trim_damaged);
			break;
		}
	}

	@Override
	protected int getRes() {
		return R.layout.pop_trim_layout;
	}

}
