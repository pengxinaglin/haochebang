package com.haoche51.checker.activity.vehicle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonBaseFragmentActivity;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.VehicleSeriesEntity;
import com.haoche51.checker.fragment.vehicle.BrandFragment;
import com.haoche51.checker.fragment.vehicle.VehicleSubscribeSeriesListFragment;
import com.haoche51.checker.util.HCLogUtil;

/**
 * Created by yangming on 2015/12/4.
 */
public class VehicleSubBrandAddActivity extends CommonBaseFragmentActivity implements VehicleSubscribeSeriesListFragment.OnSeriesListItemClickListener, VehicleSubscribeSeriesListFragment.OnBackClickListener {

	private BrandFragment brandFragment;


	@Override
	protected void initView() {
		super.initView();
		setContentView(R.layout.activity_vehicle_sub_brand_add);
		registerTitleBack();
		setScreenTitle(R.string.vehicle_subscribe_brand_list_title);

		FragmentTransaction ft = this.getSupportFragmentManager()
			.beginTransaction();
		brandFragment = new BrandFragment();
		Bundle bundle = new Bundle();
		bundle.putBoolean(TaskConstants.BINDLE_UNLIMITED_CLASS, getIntent().getBooleanExtra(TaskConstants.BINDLE_UNLIMITED_CLASS, false));
		brandFragment.setArguments(bundle);
		ft.add(R.id.fl_vehicle_sub_brand_add_container, brandFragment);
		ft.commitAllowingStateLoss();

	}

	@Override
	protected void initData() {
		super.initData();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (BrandFragment.isCarSeriesShowing) {
				brandFragment.hideSeriesFragment();
			} else {
				finish();
			}
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public void onSeriesClick(VehicleSeriesEntity seriesEntity) {
		// TODO
		brandFragment.hideSeriesFragment();
		HCLogUtil.e("onSeriesClick");
		Intent intent = new Intent();
		intent.putExtra(VehicleSubScribeConditionActivity.KEY_INTENT_EXTRA_SERIES, seriesEntity);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	@Override
	public void onbackClick() {
		brandFragment.hideSeriesFragment();
	}
}
