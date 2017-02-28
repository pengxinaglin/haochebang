package com.haoche51.checker.custom;

import com.haoche51.checker.R;
import com.haoche51.checker.constants.EngineConstants;

import android.content.Context;
import android.view.View;
import android.widget.RadioGroup;

public class EnginePopWindow extends ItemPopWindow {

	public EnginePopWindow(Context context, int status) {
		super(context, status);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(checkedId){
		case R.id.engine_normal:
			if (mListener != null) {
				mListener.statusChange(EngineConstants.ENGINE_NORMAL, position);
			}
			break;
		case R.id.engine_slight:
			if (mListener != null) {
				mListener.statusChange(EngineConstants.ENGINE_SLIGHT, position);
			}
			break;
		case R.id.engine_serious:
			if (mListener != null) {
				mListener.statusChange(EngineConstants.ENGINE_SERIOUS, position);
			}
			break;
		}
		dismiss();
	}

	@Override
	protected void initView(View popView, int status) {
		mpopGroup = (RadioGroup) popView.findViewById(R.id.pop_engine);
		switch (status){
		case EngineConstants.ENGINE_NORMAL:
			mpopGroup.check(R.id.engine_normal);
			break;
		case EngineConstants.ENGINE_SLIGHT:
			mpopGroup.check(R.id.engine_slight);
			break;
		case EngineConstants.ENGINE_SERIOUS:
			mpopGroup.check(R.id.engine_serious);
			break;
		}
	}

	@Override
	protected int getRes() {
		return R.layout.pop_engine_layout;
	}
	
}
