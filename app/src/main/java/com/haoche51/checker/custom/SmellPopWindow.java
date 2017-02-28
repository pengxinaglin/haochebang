package com.haoche51.checker.custom;

import com.haoche51.checker.R;

import android.content.Context;
import android.view.View;
import android.widget.RadioGroup;

public class SmellPopWindow extends ItemPopWindow {

	public SmellPopWindow(Context context, int status) {
		super(context, status);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.smell_normal:
			if (mListener != null) {
				mListener.statusChange(0, 0);
			}
			break;
		case R.id.smell_serious:
			if (mListener != null) {
				mListener.statusChange(1, 0);
			}
			break;
		}
		dismiss();
	}

	@Override
	protected void initView(View popView, int status) {
		mpopGroup = (RadioGroup) popView.findViewById(R.id.pop_smell);
		switch (status) {
		case 0:
			mpopGroup.check(R.id.smell_normal);
			break;
		case 1:
			mpopGroup.check(R.id.smell_serious);
			break;
		}
	}

	@Override
	protected int getRes() {
		return R.layout.pop_smell_layout;
	}
	

}
