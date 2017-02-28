package com.haoche51.checker.custom;

import com.haoche51.checker.R;
import com.haoche51.checker.constants.StartupConstants;

import android.content.Context;
import android.view.View;
import android.widget.RadioGroup;

public class StartupPopWindow extends ItemPopWindow {

	public StartupPopWindow(Context context, int status) {
		super(context, status);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.startup_normal:
			if (mListener != null) {
				mListener.statusChange(StartupConstants.STARTUP_NORMAL, position);
			}
			break;
		case R.id.startup_abnormal:
			if (mListener != null) {
				mListener.statusChange(StartupConstants.STARTUP_ABNORMAL, position);
			}
			break;
		}
		dismiss();
	}

	@Override
	protected void initView(View popView, int status) {
		mpopGroup = (RadioGroup) popView.findViewById(R.id.pop_startup);
		switch (status){
		case StartupConstants.STARTUP_NORMAL:
			mpopGroup.check(R.id.startup_normal);
			break;
		case StartupConstants.STARTUP_ABNORMAL:
			mpopGroup.check(R.id.startup_abnormal);
			break;
		}
	}

	@Override
	protected int getRes() {
		return R.layout.pop_startup_layout;
	}
	
	

}
