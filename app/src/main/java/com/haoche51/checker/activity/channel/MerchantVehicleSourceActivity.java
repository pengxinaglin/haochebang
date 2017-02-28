package com.haoche51.checker.activity.channel;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonBaseFragmentActivity;
import com.haoche51.checker.fragment.channel.MerchantVehicleSourceFragment;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 商家车源
 * Created by PengXianglin on 16/3/4.
 */
public class MerchantVehicleSourceActivity extends CommonBaseFragmentActivity implements TextView.OnEditorActionListener {

	@ViewInject(R.id.rl_titlebar)
	private RelativeLayout rl_titlebar;

	@ViewInject(R.id.et_trans_search)
	private EditText et_trans_search;//搜索内容

	@ViewInject(R.id.view_mask)
	private View view_mask;//遮罩

	@ViewInject(R.id.tv_name)
	private TextView tv_name;

	@ViewInject(R.id.tv_sell_car)
	private TextView tv_sell_car;

	@ViewInject(R.id.tv_title_add)
	private TextView addBtn;

	private int id;//商家id
	private String name;//商家名称
	private int sell_car;//在售车源数量

	private final int REQUESTCODE_ADDSOURCE = 216;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_merch_vehiclesource);
		x.view().inject(this);
		setScreenTitle("商家车源");
		registerTitleBack();//设置返回
		//监听键盘点击搜索
		et_trans_search.setOnEditorActionListener(this);
		et_trans_search.setHint(getString(R.string.hc_channel_vehicle_source_search));
		addBtn.setVisibility(View.VISIBLE);
		tv_name.setText(getIntent().getStringExtra("name"));
		tv_sell_car.setText(getIntent().getIntExtra("sell_car", 0) + "");
	}

	private MerchantVehicleSourceFragment sourceFragment;
	@Override
	protected void initData() {
		FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
//		sourceFragment = new MerchantVehicleSourceFragment(getIntent().getIntExtra("id", 0));
		sourceFragment=new MerchantVehicleSourceFragment();
		Bundle bundle=new Bundle();
		bundle.putInt("id", getIntent().getIntExtra("id", 0));
		sourceFragment.setArguments(bundle);
		ft.add(R.id.fl_content, sourceFragment);
		ft.commitAllowingStateLoss();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 显示搜索框
	 */
	@Event(R.id.tv_right_fuction)
	private void showSearchBox(final View v) {
		int width = getWindowManager().getDefaultDisplay().getWidth();
		doAnimator(width, 0, true);
	}

	/**
	 * 添加车源
	 */
	@Event(R.id.tv_title_add)
	private void showAddTaskPage(final View v) {
		Intent intent = new Intent(this, AddVehicleSourceActivity.class);
		intent.putExtra("dealer_id", getIntent().getIntExtra("id", 0));
		intent.putExtra(AddDealerActivity.INTENT_ADD_DEALER, true);
		startActivityForResult(intent, REQUESTCODE_ADDSOURCE);
	}

	/**
	 * 隐藏搜索框
	 */
	@Event(R.id.view_mask)
	private void clickMask(View v) {
		hideSearchBox(v);
	}

	/**
	 * 隐藏搜索框
	 */
	@Event(R.id.tv_search_cancel)
	private void hideSearchBox(View v) {
		//清空搜索
		if (et_trans_search != null)
			et_trans_search.setText("");
		if (sourceFragment != null)
				sourceFragment.doingFilter(new Bundle());
		//隐藏搜索框
		int width = getWindowManager().getDefaultDisplay().getWidth();
		doAnimator(0, width, false);
	}

	@Event(R.id.iv_search)
	private void onSearchClick(View v) {
		if (!TextUtils.isEmpty(et_trans_search.getText().toString().trim())) {
			commit(et_trans_search.getText().toString());
		}
	}

	private void doAnimator(int targetWidth, int startWdith, final boolean enable) {
		final ViewGroup.LayoutParams layoutParams = rl_titlebar.getLayoutParams();
		ValueAnimator va = ValueAnimator.ofInt(targetWidth, startWdith);
		va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator va) {
				layoutParams.width = (Integer) va.getAnimatedValue();
				rl_titlebar.setLayoutParams(layoutParams);
			}
		});
		va.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animator) {

			}

			@Override
			public void onAnimationEnd(Animator animator) {
				//激活输入框
				if (enable) {
					//弹出键盘
					et_trans_search.setFocusable(true);
					et_trans_search.setFocusableInTouchMode(true);
					et_trans_search.requestFocus();
					//让软键盘延时弹出，以更好的加载Activity
					et_trans_search.postDelayed(new Runnable() {
						@Override
						public void run() {
							InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							inputManager.showSoftInput(et_trans_search, 0);
						}
					}, 100);
					//弹出半透明效果
					view_mask.setVisibility(View.VISIBLE);
				} else {
					//关闭遮罩
					view_mask.setVisibility(View.GONE);
					//关闭键盘
					et_trans_search.clearFocus();
					InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(et_trans_search.getWindowToken(), 0);
				}
			}

			@Override
			public void onAnimationCancel(Animator animator) {

			}

			@Override
			public void onAnimationRepeat(Animator animator) {

			}
		});
		va.setDuration(400);
		va.start();
	}

	@Override
	public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
		//监听点击输入法的“搜索”
		if (actionId == EditorInfo.IME_ACTION_SEARCH || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_SEARCH)) {
			//当前页面进行搜索
			commit(et_trans_search.getText().toString());
			return true;
		}
		return false;
	}

	private void commit(String search_field) {
		//当前页面进行搜索
		if (sourceFragment != null) {
			Bundle where = new Bundle();
			where.putString("keyword", search_field);
			sourceFragment.doingFilter(where);
		}
		//关闭遮罩
		view_mask.setVisibility(View.GONE);
		//关闭键盘
		et_trans_search.clearFocus();
		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(et_trans_search.getWindowToken(), 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case REQUESTCODE_ADDSOURCE:
					sourceFragment.doingFilter(null);
					break;
			}
		}
	}
}
