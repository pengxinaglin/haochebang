package com.haoche51.checker.custom;

import android.content.Context;
import android.view.View;
import android.widget.RadioGroup;

import com.haoche51.checker.R;
import com.haoche51.checker.constants.CompreConstants;

public class ComprePopWindow extends ItemPopWindow {

	public ComprePopWindow(Context context, int status) {
		super(context, status);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(checkedId){
		case R.id.compre_ok:
			if (mListener != null) {
				mListener.statusChange(CompreConstants.COMPRE_OK, position);
			}
			break;
		case R.id.compre_no:
			if (mListener != null) {
				mListener.statusChange(CompreConstants.COMPRE_NO, position);
			}
			break;
		}
		dismiss();
	}

	@Override
	protected void initView(View popView, int status) {
		mpopGroup = (RadioGroup) popView.findViewById(R.id.pop_compre);
		switch (status){
		case CompreConstants.COMPRE_OK:
			mpopGroup.check(R.id.compre_ok);
			break;
		case CompreConstants.COMPRE_NO:
			mpopGroup.check(R.id.compre_no);
			break;
		}
	}

	@Override
	protected int getRes() {
		return R.layout.pop_compredetection_layout;
	}
	
}
